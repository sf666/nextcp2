import {
  Directive,
  ElementRef,
  HostListener,
  computed,
  inject,
  input,
} from '@angular/core';

/** Stand-in artwork, used wherever a cover cannot be shown. */
export const DEFAULT_ARTWORK = '/assets/images/folder-bg.png';

@Directive({
  selector: 'img[imgFallback]',
})
/**
 * Puts a default image in place when the source cannot be loaded, instead of
 * leaving the browser's own broken-image glyph on the page.
 *
 * An empty url is already handled where the url is built; this is the other case:
 * a media server announcing artwork it then fails to serve. The renderer keeps
 * reporting that url for the whole track, so the broken glyph would stay on
 * screen until the next song.
 */
export class ImgFallbackDirective {
  /** Optional override; the attribute may also be used on its own. */
  readonly imgFallback = input<string>('');

  private readonly fallbackUrl = computed(
    () => this.imgFallback() || DEFAULT_ARTWORK,
  );

  private readonly image = inject<ElementRef<HTMLImageElement>>(ElementRef);

  @HostListener('error')
  onError(): void {
    const element = this.image.nativeElement;
    const fallback = this.fallbackUrl();
    // Compare the attribute rather than the resolved src property: if the
    // fallback itself is missing, setting it again would loop on its own error.
    if (element.getAttribute('src') !== fallback) {
      element.src = fallback;
    }
  }
}
