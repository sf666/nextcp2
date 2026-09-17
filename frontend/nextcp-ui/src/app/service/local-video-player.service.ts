import { Injectable, computed, inject, signal } from '@angular/core';

import { MusicItemDto } from 'src/app/service/dto';
import { ToastService } from 'src/app/service/toast/toast.service';

/**
 * Holds what the browser is currently showing as video for the "This Browser" renderer.
 *
 * Audio and video are kept apart on purpose: {@link LocalPlayerService} owns a queue, a footer and
 * persisted state around a single HTMLAudioElement, none of which a video overlay wants. This
 * service only knows the one item on screen; the element itself lives in
 * LocalVideoPlayerComponent.
 */
@Injectable({ providedIn: 'root' })
export class LocalVideoPlayerService {
  private readonly toastService = inject(ToastService);

  /** The item on screen, or NULL while the overlay is closed. */
  public readonly currentItem = signal<MusicItemDto | null>(null);

  /** HLS playlist URL of the current item, routed through the backend stream proxy. */
  public readonly sourceUrl = computed<string | null>(() => {
    const item = this.currentItem();
    return item ? LocalVideoPlayerService.toHlsProxyUrl(item.streamingURL) : null;
  });

  public static isVideoItem(item: MusicItemDto | null | undefined): boolean {
    return item?.objectClass?.startsWith('object.item.videoItem') === true;
  }

  public open(item: MusicItemDto): void {
    if (!item?.streamingURL) {
      this.toastService.error(item?.title ?? 'video', 'no stream url');
      return;
    }
    this.currentItem.set(item);
  }

  public close(): void {
    this.currentItem.set(null);
  }

  public reportError(message: string): void {
    this.toastService.error(`${this.currentItem()?.title ?? 'video'}: ${message}.`, 'playback failed');
  }

  /**
   * Turns a media server URL into the HLS variant of the same resource, routed through the proxy.
   *
   * UMS picks the delivery format from the renderer profile, and the browse that produced this URL
   * ran under the control point's profile, not the web player's. The media servlet keys its HLS
   * branch off the file name, so asking for the HLS rendition means replacing the transcode suffix.
   */
  private static toHlsProxyUrl(streamingURL: string): string {
    const hlsUrl = streamingURL.replace(/_transcoded_to\.[a-z0-9]+$/i, '') + '_transcoded_to.m3u8';
    return '/LocalStream/stream?url=' + encodeURIComponent(hlsUrl);
  }
}
