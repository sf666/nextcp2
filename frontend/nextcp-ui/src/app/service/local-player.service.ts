import { Injectable, computed, inject, signal } from '@angular/core';
import { MusicItemDto } from './dto';
import { PersistenceService } from './persistence/persistence.service';
import { ToastService } from './toast/toast.service';

/** Snapshot persisted to localStorage so the queue and playback position survive a page reload. */
interface PersistedPlayerState {
  sourceQueue: MusicItemDto[];
  queue: MusicItemDto[];
  currentIndex: number;
  /** Playback position in seconds; always 0 for live streams, which cannot be resumed at an offset. */
  position: number;
  playing: boolean;
  shuffle: boolean;
  repeat: boolean;
}

/**
 * Plays audio directly in the browser via an HTML5 audio element, used when the synthetic
 * "This Device" renderer is selected instead of a real UPnP media renderer. Holds a simple in-memory
 * queue (for play-all / shuffle of a displayed track list), auto-advances to the next track, and
 * supports repeat and a shuffle toggle. Only the stream URL is needed; the browser fetches and
 * decodes the media itself.
 */
@Injectable({
  providedIn: 'root',
})
export class LocalPlayerService {
  private readonly audio = new Audio();
  // Original (unshuffled) order; the active playback order is derived from it.
  private sourceQueue: MusicItemDto[] = [];
  private queue: MusicItemDto[] = [];
  private currentIndex = -1;

  private readonly persistenceService = inject(PersistenceService);
  private readonly toastService = inject(ToastService);

  // A hard page reload tears down the <audio> element, so playback cannot literally continue across
  // it. Instead the queue, current track and position are persisted here and restored on startup (and
  // auto-resumed when the browser renderer is still selected). See persistState()/restoreState().
  private static readonly STORAGE_KEY = 'nextcp.localPlayer.state.v1';
  // UDN of the synthetic "This Device" renderer (mirrors DeviceService.LOCAL_BROWSER_UDN); used to
  // avoid auto-resuming local audio when a real UPnP renderer is the one selected after a reload.
  private static readonly LOCAL_BROWSER_UDN = 'nextcp-local-browser';
  private lastPersistMs = 0;

  // Whether playback is *meant* to be running. Tracked explicitly rather than read from the audio
  // element, because the browser fires "pause" while tearing the page down on a reload - persisting
  // that state would bring the session back paused even though the user never paused anything.
  private playIntent = false;
  // Set while a one-shot "resume on next user gesture" listener is armed (see armResumeOnUserGesture).
  private gestureResumeArmed = false;

  // Guards against advancing to the next track more than once for the same track: the "ended" event
  // and the near-end fallback (maybeAdvanceAtEnd) can both fire. Reset per track in playIndex().
  private trackEndHandled = false;
  // How close to the (metadata) duration counts as "reached the end" for the fallback advance.
  private static readonly END_EPSILON_SECONDS = 0.6;

  /** What the HTMLMediaElement error codes mean, in words the user can act on. */
  private static readonly MEDIA_ERROR_TEXT: Record<number, string> = {
    1: 'playback was aborted',
    2: 'the stream could not be reached or stopped sending',
    3: 'the stream could not be decoded',
    4: 'the stream was refused, or this browser cannot play its format',
  };

  // The error event can fire repeatedly for one source; the user needs to hear it once.
  private playbackErrorReported = false;

  // Playback state, consumed by RendererService so the footer now-playing/transport reflects the
  // local browser player when the "This Device" renderer is selected.
  public readonly playing = signal<boolean>(false);
  public readonly currentItem = signal<MusicItemDto | null>(null);
  public readonly currentTime = signal<number>(0);
  public readonly duration = signal<number>(0);
  // When true, playback restarts from the first queued track after the last one finishes.
  public readonly repeat = signal<boolean>(false);
  // When true, the active queue is randomized.
  public readonly shuffle = signal<boolean>(false);
  // The active playback order and the position within it, mirrored as signals so the player queue
  // view can render the browser queue the same way it renders a renderer's OpenHome playlist.
  public readonly queueItems = signal<MusicItemDto[]>([]);
  public readonly activeIndex = signal<number>(-1);
  // True while an endless / live source is loaded (web radio). Consumed by RendererService so the
  // footer knows there is no position to seek to.
  public readonly live = computed<boolean>(() => {
    const item = this.currentItem();
    // "Nothing loaded" is not a live stream: it must not hide the (idle) seek slider.
    return !!item && this.isLiveStream(item);
  });

  constructor() {
    this.audio.addEventListener('play', () => { this.playing.set(true); this.persistState(); });
    this.audio.addEventListener('playing', () => { this.playing.set(true); this.persistState(); });
    this.audio.addEventListener('pause', () => { this.playing.set(false); this.persistState(); this.maybeAdvanceAtEnd('pause'); });
    this.audio.addEventListener('error', () => {
      console.warn('[local-player] audio "error" event', {
        error: this.audio.error,
        currentSrc: this.audio.currentSrc,
        readyState: this.audio.readyState,
        networkState: this.audio.networkState,
      });
      this.playing.set(false);
      this.reportPlaybackError();
    });
    this.audio.addEventListener('ended', () => {
      // Diagnostic: auto-advance only works if the browser actually fires "ended". For streams with
      // an unknown length (audio.duration === Infinity) Chromium may fire stalled/suspend/error
      // instead and never reach here, leaving playback stuck on the last track.
      console.debug('[local-player] audio "ended" event → auto-advance', {
        currentIndex: this.currentIndex,
        queueLength: this.queue.length,
        duration: this.audio.duration,
      });
      this.onEnded();
    });
    // These fire when the browser cannot make progress on the stream; if one of them shows up at the
    // point the track should end (instead of "ended"), the proxied stream is the culprit, not the queue.
    (['stalled', 'suspend', 'waiting'] as const).forEach((evt) =>
      this.audio.addEventListener(evt, () => {
        console.debug(`[local-player] audio "${evt}" event`, {
          currentTime: this.audio.currentTime,
          duration: this.audio.duration,
          readyState: this.audio.readyState,
          networkState: this.audio.networkState,
        });
        this.maybeAdvanceAtEnd(evt);
      }),
    );
    this.audio.addEventListener('timeupdate', () => {
      this.currentTime.set(this.audio.currentTime);
      this.persistPositionThrottled();
      this.maybeAdvanceAtEnd('timeupdate');
    });
    this.audio.addEventListener('durationchange', () => this.updateDuration());
    this.audio.addEventListener('loadedmetadata', () => this.updateDuration());

    // Capture the exact position right before the page goes away so a reload resumes where it left off.
    // "pagehide" is preferred over "beforeunload" because it also fires when the page enters the bfcache.
    window.addEventListener('pagehide', () => this.persistState());

    this.restoreState();
  }

  /** Plays a single track (replaces the queue with just this item). */
  public play(item: MusicItemDto): void {
    this.playQueue(item ? [item] : [], false);
  }

  /** Loads the given tracks as the queue and starts playback, optionally shuffled. */
  public playQueue(items: MusicItemDto[], shuffle: boolean): void {
    const playable = (items ?? []).filter((item) => !!item && !!item.streamingURL);
    if (playable.length === 0) {
      this.reportNothingPlayable(items);
      return;
    }
    this.sourceQueue = playable.slice();
    this.shuffle.set(shuffle);
    this.queue = shuffle ? this.shuffleArray(playable) : playable.slice();
    this.publishQueue();
    this.playIndex(0);
  }

  /** Queues the given tracks (shown order) and starts at startItem, so the rest keeps playing after it. */
  public playQueueFrom(items: MusicItemDto[], startItem: MusicItemDto): void {
    const playable = (items ?? []).filter((item) => !!item && !!item.streamingURL);
    if (playable.length === 0 || !startItem) {
      this.play(startItem);
      return;
    }
    let start = playable.indexOf(startItem);
    if (start < 0 && startItem.streamingURL) {
      start = playable.findIndex((item) => item.streamingURL === startItem.streamingURL);
    }
    if (start < 0) {
      // Clicked item is not part of the displayed track list; just play it on its own.
      this.play(startItem);
      return;
    }
    this.sourceQueue = playable.slice();
    this.shuffle.set(false);
    this.queue = playable.slice();
    this.publishQueue();
    this.playIndex(start);
  }

  /**
   * Appends tracks to the queue without disturbing what is playing - the local counterpart of a
   * renderer's Playlist::Insert. Playback only starts when the queue was empty, so "add" never
   * hijacks the current track.
   */
  public enqueue(items: MusicItemDto[]): void {
    const playable = (items ?? []).filter((item) => !!item && !!item.streamingURL);
    if (playable.length === 0) {
      this.reportNothingPlayable(items);
      return;
    }
    const wasEmpty = this.queue.length === 0;
    // Both arrays get the same tracks, so `queue` stays a permutation of `sourceQueue` and
    // switching shuffle off later still yields the full set in server order.
    this.sourceQueue = this.sourceQueue.concat(playable);
    this.queue = this.queue.concat(
      this.shuffle() ? this.shuffleArray(playable) : playable,
    );
    this.publishQueue();
    if (wasEmpty) {
      this.playIndex(0);
      return;
    }
    this.persistState();
  }

  /**
   * Puts tracks directly after the one playing (Playlist::InsertNext). Falls back to appending
   * while nothing is loaded.
   */
  public enqueueNext(items: MusicItemDto[]): void {
    if (this.currentIndex < 0 || this.queue.length === 0) {
      this.enqueue(items);
      return;
    }
    const playable = (items ?? []).filter((item) => !!item && !!item.streamingURL);
    if (playable.length === 0) {
      this.reportNothingPlayable(items);
      return;
    }
    const current = this.queue[this.currentIndex];
    this.queue.splice(this.currentIndex + 1, 0, ...playable);
    // Keep the unshuffled order in step. After a reload the two arrays are deserialized
    // separately, so identity alone does not find the current track (see removeQueueIndex).
    let sourceIndex = this.sourceQueue.indexOf(current);
    if (sourceIndex < 0) {
      sourceIndex = this.sourceQueue.findIndex(
        (item) => item.streamingURL === current.streamingURL,
      );
    }
    this.sourceQueue.splice(
      sourceIndex < 0 ? this.sourceQueue.length : sourceIndex + 1,
      0,
      ...playable,
    );
    this.publishQueue();
    this.persistState();
  }

  /** Starts the queue entry at the given position (used by the player queue view). */
  public playQueueIndex(index: number): void {
    this.playIndex(index);
  }

  /**
   * Removes one entry from the queue. Removing the track that is currently playing continues with
   * the one that takes its place, or stops when the queue runs empty.
   */
  public removeQueueIndex(index: number): void {
    if (index < 0 || index >= this.queue.length) {
      return;
    }
    const removed = this.queue[index];
    this.queue.splice(index, 1);
    // Drop it from the unshuffled order too, or turning shuffle off would bring it back. After a
    // reload the two arrays are deserialized separately, so identity alone is not enough to find it.
    let sourceIndex = this.sourceQueue.indexOf(removed);
    if (sourceIndex < 0) {
      sourceIndex = this.sourceQueue.findIndex(
        (item) => item.streamingURL === removed.streamingURL,
      );
    }
    if (sourceIndex >= 0) {
      this.sourceQueue.splice(sourceIndex, 1);
    }
    this.publishQueue();
    if (index < this.currentIndex) {
      this.currentIndex--;
      this.activeIndex.set(this.currentIndex);
      this.persistState();
      return;
    }
    if (index > this.currentIndex) {
      this.persistState();
      return;
    }
    if (this.queue.length === 0) {
      this.stop();
      return;
    }
    this.playIndex(Math.min(index, this.queue.length - 1));
  }

  /** Drops the whole queue and stops playback. */
  public clearQueue(): void {
    this.stop();
  }

  /**
   * Says so when there is nothing to play. Silence is the worst answer here: the click looked like it
   * did nothing at all. A missing stream URL means the media server offered this entry without a
   * usable resource - typical for web radio entries whose content format it does not declare.
   */
  private reportNothingPlayable(items: MusicItemDto[]): void {
    const requested = (items ?? []).find((item) => !!item);
    console.warn('[local-player] nothing playable in request', items);
    this.toastService.error(
      `${this.describe(requested)} has no stream URL the player can use.`,
      'cannot play',
    );
  }

  /** Reports a source the browser refused - a dead stream, a codec it cannot decode, or a 401/403. */
  private reportPlaybackError(): void {
    if (this.playbackErrorReported || !this.currentItem()) {
      return;
    }
    this.playbackErrorReported = true;
    const item = this.currentItem();
    const code = this.audio.error?.code ?? 0;
    const fallback =
      LocalPlayerService.MEDIA_ERROR_TEXT[code] ?? 'the stream could not be played';
    const source = this.audio.currentSrc;
    // The audio element only reports "cannot play this format" even when the source answered with an
    // error status, so ask the source itself what happened.
    this.probeSourceFailure(source).then((httpReason) => {
      const reason = httpReason ?? fallback;
      this.toastService.error(`${this.describe(item)}: ${reason}.`, 'playback failed');
    });
  }

  /** @returns why the source rejected the request, or null if it did not (or cannot be asked). */
  private async probeSourceFailure(source: string): Promise<string | null> {
    if (!source) {
      return null;
    }
    try {
      const response = await fetch(source, { headers: { Range: 'bytes=0-0' }, cache: 'no-store' });
      if (response.ok || response.status === 206) {
        return null;
      }
      const upstream = response.headers.get('X-Upstream-Status');
      const detail = upstream && upstream !== String(response.status) ? ` (media server: ${upstream})` : '';
      return `${LocalPlayerService.httpReason(response.status)}${detail}`;
    } catch {
      return null;
    }
  }

  private static httpReason(status: number): string {
    switch (status) {
      case 401:
      case 403:
        return `the source refused access (HTTP ${status}), check the subscription or credentials`;
      case 404:
      case 410:
        return `the source is gone (HTTP ${status})`;
      case 502:
      case 504:
        return `the media server could not read the source (HTTP ${status})`;
      case 503:
        return 'the source is unavailable right now (HTTP 503)';
      default:
        return `the source answered HTTP ${status}`;
    }
  }

  private describe(item: MusicItemDto | null | undefined): string {
    return item?.title ? `"${item.title}"` : 'This entry';
  }

  private publishQueue(): void {
    this.queueItems.set(this.queue.slice());
  }

  public next(): void {
    if (this.currentIndex + 1 < this.queue.length) {
      this.playIndex(this.currentIndex + 1);
    } else if (this.repeat() && this.queue.length > 0) {
      this.playIndex(0);
    } else {
      this.stop();
    }
  }

  public previous(): void {
    if (this.currentIndex > 0) {
      this.playIndex(this.currentIndex - 1);
    }
  }

  public toggleRepeat(): void {
    this.repeat.set(!this.repeat());
    this.persistState();
  }

  /**
   * Toggles shuffle on the active queue without interrupting the current track: turning it on keeps
   * the current track playing and randomizes the rest; turning it off restores the original order.
   */
  public toggleShuffle(): void {
    const on = !this.shuffle();
    this.shuffle.set(on);
    if (this.queue.length === 0) {
      return;
    }
    const current = this.currentItem();
    if (on) {
      const rest = this.sourceQueue.filter((item) => item !== current);
      const shuffled = this.shuffleArray(rest);
      this.queue = current ? [current, ...shuffled] : shuffled;
      this.currentIndex = current ? 0 : Math.max(0, this.currentIndex);
    } else {
      this.queue = this.sourceQueue.slice();
      const idx = current ? this.queue.indexOf(current) : -1;
      this.currentIndex = idx >= 0 ? idx : 0;
    }
    this.publishQueue();
    this.activeIndex.set(this.currentIndex);
    this.persistState();
  }

  public resume(): void {
    this.playIntent = true;
    this.audio.play().catch((err) => console.error('local browser playback failed', err));
  }

  public pause(): void {
    this.playIntent = false;
    this.audio.pause();
  }

  public stop(): void {
    this.playIntent = false;
    this.audio.pause();
    this.audio.removeAttribute('src');
    this.audio.load();
    this.sourceQueue = [];
    this.queue = [];
    this.publishQueue();
    this.currentIndex = -1;
    this.activeIndex.set(-1);
    this.currentItem.set(null);
    this.currentTime.set(0);
    this.duration.set(0);
    this.shuffle.set(false);
    this.clearPersistedState();
  }

  public seek(secondsAbsolute: number): void {
    // A live stream has an empty seekable range; assigning currentTime there kills playback instead
    // of moving it. The previous guard missed this, because Infinity passes an isNaN() check.
    if (this.live()) {
      return;
    }
    // A transcoded stream also reports a non-finite duration, but its length is known from the DIDL
    // metadata and the backend proxy serves it with real range support - so it stays seekable.
    if (!Number.isFinite(this.audio.duration) && this.duration() <= 0) {
      return;
    }
    this.audio.currentTime = secondsAbsolute;
  }

  public setVolume(volumePercent: number): void {
    this.audio.volume = Math.max(0, Math.min(1, volumePercent / 100));
  }

  private playIndex(index: number): void {
    if (index < 0 || index >= this.queue.length) {
      return;
    }
    this.currentIndex = index;
    this.activeIndex.set(index);
    this.trackEndHandled = false;
    this.playbackErrorReported = false;
    this.playIntent = true;
    const item = this.queue[index];
    this.currentItem.set(item);
    this.currentTime.set(0);
    this.duration.set(0);
    this.audio.src = this.toProxyUrl(item.streamingURL);
    this.audio.play().catch((err) => console.error('local browser playback failed', err));
    this.persistState();
  }

  /**
   * Routes the media server URL through the backend stream proxy instead of hitting the media
   * server directly. The proxy tags the request as a browser renderer so the media server (UMS,
   * via nextcp2webplayer.conf) transcodes formats the browser cannot decode, and it avoids CORS /
   * mixed-content problems when the UI is served over HTTPS.
   */
  private toProxyUrl(streamingURL: string): string {
    return '/LocalStream/stream?url=' + encodeURIComponent(streamingURL);
  }

  private onEnded(): void {
    // Advance at most once per track (the "ended" event and the near-end fallback may both fire).
    if (this.trackEndHandled) {
      return;
    }
    this.trackEndHandled = true;
    // Auto-advance to the next queued track; at the end, restart from the top when repeat is on,
    // otherwise stop.
    if (this.currentIndex + 1 < this.queue.length) {
      this.playIndex(this.currentIndex + 1);
    } else if (this.repeat() && this.queue.length > 0) {
      this.playIndex(0);
    } else {
      this.playIntent = false;
      this.playing.set(false);
      this.persistState();
    }
  }

  /**
   * Fallback auto-advance for streams whose length the browser does not know (transcoded / chunked,
   * i.e. audio.duration === Infinity). For those the reliable "ended" event often never fires, so
   * playback would simply stop on the current track. Once the position reaches the metadata duration
   * we advance ourselves. Finite-duration media (native or fully pre-transcoded) is left entirely to
   * the real "ended" event, so its exact tail is never clipped.
   */
  private maybeAdvanceAtEnd(reason: string): void {
    if (Number.isFinite(this.audio.duration)) {
      return;
    }
    const dur = this.duration();
    if (dur > 0 && this.audio.currentTime >= dur - LocalPlayerService.END_EPSILON_SECONDS) {
      console.debug(`[local-player] near-end fallback advance (trigger: ${reason})`, {
        currentTime: this.audio.currentTime,
        duration: dur,
        currentIndex: this.currentIndex,
        queueLength: this.queue.length,
      });
      this.onEnded();
    }
  }

  /**
   * True for endless / live sources (web radio such as SomaFM, AudioAddict channels): they have no
   * meaningful playback position, so it is neither persisted nor restored. Seeking such a stream to a
   * stored offset kills playback altogether, because its seekable range is empty. Also treats media
   * without a known length as non-seekable, which is the safe assumption.
   */
  private isLiveStream(item: MusicItemDto | null | undefined): boolean {
    if (!item) {
      return true;
    }
    if (item.objectClass?.startsWith('object.item.audioItem.audioBroadcast')) {
      return true;
    }
    if (item.audioFormat?.isStreaming === true) {
      return true;
    }
    return !((item.audioFormat?.durationInSeconds ?? 0) > 0);
  }

  private updateDuration(): void {
    const rawDuration = this.audio.duration;
    // The browser reports a non-finite duration (NaN before metadata, Infinity for streams whose
    // length the server never announces, e.g. chunked / on-the-fly transcoded audio without a
    // Content-Length). Formatting such a value produces "Infinity:NaN:NaN". UMS still ships the real
    // track length in the DLNA/DIDL metadata (audioFormat.durationInSeconds, the same value the track
    // list shows), so fall back to that instead of hiding the end time.
    if (!Number.isFinite(rawDuration)) {
      const metadataDuration = this.currentItem()?.audioFormat?.durationInSeconds ?? 0;
      console.debug(
        '[local-player] non-finite audio duration from stream (transcoded / no Content-Length); using DIDL metadata duration instead',
        {
          audioDuration: rawDuration,
          metadataDuration,
          currentSrc: this.audio.currentSrc,
          readyState: this.audio.readyState,
          networkState: this.audio.networkState,
        },
      );
      this.duration.set(Number.isFinite(metadataDuration) && metadataDuration > 0 ? metadataDuration : 0);
      return;
    }
    this.duration.set(rawDuration);
  }

  /** Writes the current queue/track/position to localStorage so it can be restored after a reload. */
  private persistState(): void {
    this.lastPersistMs = Date.now();
    try {
      if (this.currentIndex < 0 || this.queue.length === 0) {
        localStorage.removeItem(LocalPlayerService.STORAGE_KEY);
        return;
      }
      const item = this.queue[this.currentIndex];
      const seekable = !this.isLiveStream(item) && Number.isFinite(this.audio.currentTime);
      const state: PersistedPlayerState = {
        sourceQueue: this.sourceQueue,
        queue: this.queue,
        currentIndex: this.currentIndex,
        position: seekable ? this.audio.currentTime : 0,
        playing: this.playIntent,
        shuffle: this.shuffle(),
        repeat: this.repeat(),
      };
      localStorage.setItem(LocalPlayerService.STORAGE_KEY, JSON.stringify(state));
    } catch {
      // localStorage may be unavailable or over quota (e.g. a very large queue); being unable to
      // restore later is non-fatal, so ignore.
    }
  }

  /** Throttled variant for the high-frequency timeupdate event (persists the position ~every 5s). */
  private persistPositionThrottled(): void {
    if (Date.now() - this.lastPersistMs >= 5000) {
      this.persistState();
    }
  }

  private clearPersistedState(): void {
    try {
      localStorage.removeItem(LocalPlayerService.STORAGE_KEY);
    } catch {
      // ignore
    }
  }

  /**
   * Restores a persisted queue/track/position on startup so an accidental page reload does not lose
   * the session. The saved track is cued and seeked to the stored position; playback is auto-resumed
   * only when it was running AND the browser renderer is still the selected one. Autoplay may still be
   * blocked by the browser (no user gesture carries across a reload), in which case the track stays
   * paused and a single click on play resumes it.
   */
  private restoreState(): void {
    let raw: string | null = null;
    try {
      raw = localStorage.getItem(LocalPlayerService.STORAGE_KEY);
    } catch {
      return;
    }
    if (!raw) {
      return;
    }
    let state: PersistedPlayerState;
    try {
      state = JSON.parse(raw) as PersistedPlayerState;
    } catch {
      this.clearPersistedState();
      return;
    }
    if (!state?.queue?.length || state.currentIndex < 0 || state.currentIndex >= state.queue.length) {
      this.clearPersistedState();
      return;
    }

    this.sourceQueue = state.sourceQueue?.length ? state.sourceQueue : state.queue.slice();
    this.queue = state.queue;
    this.publishQueue();
    this.currentIndex = state.currentIndex;
    this.activeIndex.set(state.currentIndex);
    this.shuffle.set(!!state.shuffle);
    this.repeat.set(!!state.repeat);

    const item = this.queue[this.currentIndex];
    this.currentItem.set(item);

    // A live stream always resumes at the live edge; only real tracks carry a position.
    const resumePosition =
      !this.isLiveStream(item) && Number.isFinite(state.position) && state.position > 0 ? state.position : 0;
    this.currentTime.set(resumePosition);
    // Show the metadata length straight away (transcoded streams report Infinity, see updateDuration()).
    const metaDuration = item?.audioFormat?.durationInSeconds ?? 0;
    this.duration.set(metaDuration > 0 ? metaDuration : 0);

    // Cue the saved track and seek to the stored position once its metadata is available. Seeking may
    // be ignored for transcoded streams without range support, in which case playback restarts at 0.
    this.audio.src = this.toProxyUrl(item.streamingURL);
    if (resumePosition > 0) {
      const seekOnce = () => {
        this.audio.removeEventListener('loadedmetadata', seekOnce);
        try {
          this.audio.currentTime = resumePosition;
        } catch {
          // seeking not supported on this stream; keep playing from the start
        }
      };
      this.audio.addEventListener('loadedmetadata', seekOnce);
    }

    const browserRendererSelected = this.persistenceService.isCurrentMediaRenderer(LocalPlayerService.LOCAL_BROWSER_UDN);
    if (state.playing && browserRendererSelected) {
      this.playIntent = true;
      this.audio.play().catch((err) => {
        console.debug('[local-player] auto-resume after reload blocked; waiting for user gesture', err);
        this.armResumeOnUserGesture();
      });
    }
  }

  /**
   * Browsers refuse programmatic playback without a user gesture, and a reload does not carry one
   * over (Chrome only lets it through for sites with a high media engagement score). When the
   * auto-resume is rejected, the restored track is already cued - so arm a one-shot listener that
   * starts it on the very next click or key press anywhere in the app, instead of making the user
   * hunt for the play button.
   */
  private armResumeOnUserGesture(): void {
    if (this.gestureResumeArmed) {
      return;
    }
    this.gestureResumeArmed = true;
    const armedItem = this.currentItem();
    const events = ['pointerdown', 'keydown', 'touchstart'] as const;
    const onGesture = () => {
      events.forEach((evt) => window.removeEventListener(evt, onGesture, true));
      this.gestureResumeArmed = false;
      // Skip if the user's gesture was itself a transport action that already started something else.
      if (!this.playing() && this.currentItem() === armedItem) {
        this.resume();
      }
    };
    events.forEach((evt) => window.addEventListener(evt, onGesture, true));
  }

  /** Fisher-Yates shuffle, returns a new array. */
  private shuffleArray(items: MusicItemDto[]): MusicItemDto[] {
    const arr = items.slice();
    for (let i = arr.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      const tmp = arr[i];
      arr[i] = arr[j];
      arr[j] = tmp;
    }
    return arr;
  }
}
