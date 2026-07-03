import { Injectable, signal } from '@angular/core';
import { MusicItemDto } from './dto';

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
    this.audio.addEventListener('play', () => this.playing.set(true));
    this.audio.addEventListener('playing', () => this.playing.set(true));
    this.audio.addEventListener('pause', () => this.playing.set(false));
    this.audio.addEventListener('error', () => this.playing.set(false));
    this.audio.addEventListener('ended', () => this.onEnded());
    this.audio.addEventListener('timeupdate', () => this.currentTime.set(this.audio.currentTime));
    this.audio.addEventListener('durationchange', () => this.updateDuration());
    this.audio.addEventListener('loadedmetadata', () => this.updateDuration());
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
    this.duration.set(isNaN(this.audio.duration) ? 0 : this.audio.duration);
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
