import { Injectable, inject, signal } from '@angular/core';
import { MusicItemDto } from './dto';
import { PersistenceService } from './persistence/persistence.service';

/** Snapshot persisted to localStorage so the queue and playback position survive a page reload. */
interface PersistedPlayerState {
  sourceQueue: MusicItemDto[];
  queue: MusicItemDto[];
  currentIndex: number;
  position: number;
  playing: boolean;
  shuffle: boolean;
  repeat: boolean;
}

/**
 * Plays audio directly in the browser via an HTML5 audio element, used when the synthetic
 * "This Browser" renderer is selected instead of a real UPnP media renderer. Holds a simple in-memory
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

  // A hard page reload tears down the <audio> element, so playback cannot literally continue across
  // it. Instead the queue, current track and position are persisted here and restored on startup (and
  // auto-resumed when the browser renderer is still selected). See persistState()/restoreState().
  private static readonly STORAGE_KEY = 'nextcp.localPlayer.state.v1';
  // UDN of the synthetic "This Browser" renderer (mirrors DeviceService.LOCAL_BROWSER_UDN); used to
  // avoid auto-resuming local audio when a real UPnP renderer is the one selected after a reload.
  private static readonly LOCAL_BROWSER_UDN = 'nextcp-local-browser';
  private lastPersistMs = 0;

  // Playback state, consumed by RendererService so the footer now-playing/transport reflects the
  // local browser player when the "This Browser" renderer is selected.
  public readonly playing = signal<boolean>(false);
  public readonly currentItem = signal<MusicItemDto | null>(null);
  public readonly currentTime = signal<number>(0);
  public readonly duration = signal<number>(0);
  // When true, playback restarts from the first queued track after the last one finishes.
  public readonly repeat = signal<boolean>(false);
  // When true, the active queue is randomized.
  public readonly shuffle = signal<boolean>(false);

  constructor() {
    this.audio.addEventListener('play', () => { this.playing.set(true); this.persistState(); });
    this.audio.addEventListener('playing', () => { this.playing.set(true); this.persistState(); });
    this.audio.addEventListener('pause', () => { this.playing.set(false); this.persistState(); });
    this.audio.addEventListener('error', () => {
      console.warn('[local-player] audio "error" event', {
        error: this.audio.error,
        currentSrc: this.audio.currentSrc,
        readyState: this.audio.readyState,
        networkState: this.audio.networkState,
      });
      this.playing.set(false);
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
      this.audio.addEventListener(evt, () =>
        console.debug(`[local-player] audio "${evt}" event`, {
          currentTime: this.audio.currentTime,
          duration: this.audio.duration,
          readyState: this.audio.readyState,
          networkState: this.audio.networkState,
        }),
      ),
    );
    this.audio.addEventListener('timeupdate', () => {
      this.currentTime.set(this.audio.currentTime);
      this.persistPositionThrottled();
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
      return;
    }
    this.sourceQueue = playable.slice();
    this.shuffle.set(shuffle);
    this.queue = shuffle ? this.shuffleArray(playable) : playable.slice();
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
    this.playIndex(start);
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
    this.persistState();
  }

  public resume(): void {
    this.audio.play().catch((err) => console.error('local browser playback failed', err));
  }

  public pause(): void {
    this.audio.pause();
  }

  public stop(): void {
    this.audio.pause();
    this.audio.removeAttribute('src');
    this.audio.load();
    this.sourceQueue = [];
    this.queue = [];
    this.currentIndex = -1;
    this.currentItem.set(null);
    this.currentTime.set(0);
    this.duration.set(0);
    this.shuffle.set(false);
    this.clearPersistedState();
  }

  public seek(secondsAbsolute: number): void {
    if (!isNaN(this.audio.duration)) {
      this.audio.currentTime = secondsAbsolute;
    }
  }

  public setVolume(volumePercent: number): void {
    this.audio.volume = Math.max(0, Math.min(1, volumePercent / 100));
  }

  private playIndex(index: number): void {
    if (index < 0 || index >= this.queue.length) {
      return;
    }
    this.currentIndex = index;
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
    // Auto-advance to the next queued track; at the end, restart from the top when repeat is on,
    // otherwise stop.
    if (this.currentIndex + 1 < this.queue.length) {
      this.playIndex(this.currentIndex + 1);
    } else if (this.repeat() && this.queue.length > 0) {
      this.playIndex(0);
    } else {
      this.playing.set(false);
    }
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
      const state: PersistedPlayerState = {
        sourceQueue: this.sourceQueue,
        queue: this.queue,
        currentIndex: this.currentIndex,
        position: Number.isFinite(this.audio.currentTime) ? this.audio.currentTime : 0,
        playing: this.playing(),
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
    this.currentIndex = state.currentIndex;
    this.shuffle.set(!!state.shuffle);
    this.repeat.set(!!state.repeat);

    const item = this.queue[this.currentIndex];
    this.currentItem.set(item);

    const resumePosition = Number.isFinite(state.position) && state.position > 0 ? state.position : 0;
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
      this.audio.play().catch((err) =>
        console.debug('[local-player] auto-resume after reload blocked; waiting for user gesture', err),
      );
    }
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
