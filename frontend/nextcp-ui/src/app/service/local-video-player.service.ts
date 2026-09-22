import { Injectable, computed, inject, signal } from '@angular/core';

import { ItemDto } from 'src/app/service/dto';
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
  public readonly currentItem = signal<ItemDto | null>(null);

  /** HLS playlist URL of the current item, routed through the backend stream proxy. */
  public readonly sourceUrl = computed<string | null>(() => {
    const item = this.currentItem();
    if (!item) {
      return null;
    }
    // The media server announces the HLS rendition as a resource of its own. Only a server that
    // does not falls back to deriving the URL from the streaming one.
    const hlsUrl = item.video?.hlsUrl?.length
      ? item.video.hlsUrl
      : LocalVideoPlayerService.toHlsUrl(item.streamingURL);
    return '/LocalStream/stream?url=' + encodeURIComponent(hlsUrl);
  });

  public static isVideoItem(item: ItemDto | null | undefined): boolean {
    return item?.objectClass?.startsWith('object.item.videoItem') === true;
  }

  public open(item: ItemDto): void {
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

  /** Fallback for a media server that announces no HLS resource: the servlet keys HLS off this suffix. */
  private static toHlsUrl(streamingURL: string): string {
    return streamingURL.replace(/_transcoded_to\.[a-z0-9]+$/i, '') + '_transcoded_to.m3u8';
  }
}
