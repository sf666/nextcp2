import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ElementRef,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import Hls from 'hls.js';

import { LocalVideoPlayerService } from 'src/app/service/local-video-player.service';

/**
 * Full-screen overlay that plays the video of the "This Browser" renderer.
 *
 * The stream arrives as HLS because that is the only way a browser can seek in something the media
 * server transcodes on the fly. Safari plays HLS itself; everyone else needs hls.js.
 */
@Component({
  selector: 'local-video-player',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [],
  templateUrl: './local-video-player.component.html',
})
export class LocalVideoPlayerComponent {
  readonly videoPlayer = inject(LocalVideoPlayerService);

  private readonly videoElement = viewChild<ElementRef<HTMLVideoElement>>('video');
  private hls: Hls | null = null;

  constructor() {
    inject(DestroyRef).onDestroy(() => this.detach());
    effect(() => {
      const source = this.videoPlayer.sourceUrl();
      const element = this.videoElement()?.nativeElement;
      if (!element) {
        // The overlay is closed, so the element is gone and hls.js has to let go of it.
        this.detach();
        return;
      }
      this.attach(element, source);
    });
  }

  public close(): void {
    this.videoPlayer.close();
  }

  /** Closing must not depend on hitting the button; a video fills the screen. */
  public onOverlayKeydown(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.close();
    }
  }

  private attach(element: HTMLVideoElement, source: string | null): void {
    this.detach();
    if (!source) {
      element.removeAttribute('src');
      return;
    }
    if (Hls.isSupported()) {
      const hls = new Hls({ enableWorker: true });
      this.hls = hls;
      hls.on(Hls.Events.ERROR, (_event, data) => {
        // Only a fatal error ends playback; hls.js recovers from the rest on its own.
        if (data.fatal) {
          this.videoPlayer.reportError(data.details);
          this.detach();
        }
      });
      hls.loadSource(source);
      hls.attachMedia(element);
      hls.on(Hls.Events.MANIFEST_PARSED, () => {
        element.play().catch((err) => console.error('local browser video playback failed', err));
      });
      return;
    }
    // Safari and iOS play HLS natively and have no hls.js support to fall back on.
    element.src = source;
    element.play().catch((err) => console.error('local browser video playback failed', err));
  }

  private detach(): void {
    this.hls?.destroy();
    this.hls = null;
  }
}
