import { Injectable } from '@angular/core';
import { supportsBackdropFilter } from './browser-capabilities';

@Injectable({
  providedIn: 'root',
})
export class BackgroundImageService {
  // What each target currently carries. Re-reading a container reports the same artwork, and
  // reassigning the identical background makes the browser decode and repaint it again - which
  // is visible as a flicker. Nothing else writes these elements, so remembering it here is safe.
  private applied = new Map<string, string>();

  /**
   * @return false when the element already carries this url, so the caller can skip the work that
   * would follow.
   */
  private apply(elementId: string, url: string): boolean {
    const element = document.getElementById(elementId);
    if (!supportsBackdropFilter || !element) {
      return false;
    }
    if (this.applied.get(elementId) === url) {
      return false;
    }
    this.applied.set(elementId, url);
    // Empty url clears the inline override so the base (neutral dark) shows through.
    element.style.backgroundImage = url ? 'url("' + url + '")' : '';
    return true;
  }

  public setBackgroundImageMainScreen(url: string): void {
    this.apply('main-screen', url);
  }

  public setFooterBackgroundImage(url: string): void {
    this.apply('footer-background', url);
  }

  public setDisplayContainerHeaderImage(url: string): void {
    this.apply('header-background', url);
  }

  /**
   * Extract the artwork's dominant (vibrant-weighted) colour and publish it as
   * the CSS variable `--ambient-color` — used to tint the sidebar reliably (the
   * full-screen wash leaves the far-left sidebar grey for dark-edged covers).
   * Needs a CORS-readable image; on any failure it clears the variable and the
   * sidebar falls back to neutral.
   */
  public applyAmbientTint(url: string): void {
    if (!supportsBackdropFilter) {
      return;
    }
    if (!url) {
      // No artwork at all — drop the tint so the sidebar goes neutral.
      document.documentElement.style.removeProperty('--ambient-color');
      return;
    }
    this.extractTintColor(url).then((tint) => {
      if (tint) {
        document.documentElement.style.setProperty('--ambient-color', tint);
      } else {
        document.documentElement.style.removeProperty('--ambient-color');
      }
    });
  }

  /**
   * Reads one artwork's dominant (vibrant-weighted) colour.
   *
   * Resolves to null rather than rejecting for every way this can come up empty
   * — no url, a broken image, a cross-origin canvas, near-greyscale artwork — so
   * callers can simply drop what they did not get. Needs a CORS-readable image.
   */
  public extractTintColor(url: string): Promise<string | null> {
    if (!url) {
      return Promise.resolve(null);
    }
    return new Promise((resolve) => {
      const img = new Image();
      img.crossOrigin = 'anonymous';
      img.onload = () => {
        this.readTint(img).then(resolve, () => resolve(null));
      };
      img.onerror = () => resolve(null);
      img.src = url;
    });
  }

  // Covers are read down to this many pixels per side before they are averaged.
  private static readonly SAMPLE_SIZE = 16;

  /**
   * Averages one already-loaded artwork down to a single tint.
   *
   * Scaling a full-size cover is the expensive half of this, and it used to run
   * straight inside the image's load handler — tens of milliseconds of main
   * thread right when the browse result wanted to paint its tiles. createImageBitmap
   * does that resize off the main thread, which leaves only the average over a
   * 16x16 buffer here. Where it is unavailable (or refuses the image) the old
   * drawImage path still applies.
   */
  private async readTint(img: HTMLImageElement): Promise<string | null> {
    const s = BackgroundImageService.SAMPLE_SIZE;
    let source: HTMLImageElement | ImageBitmap = img;
    if (typeof createImageBitmap === 'function') {
      try {
        source = await createImageBitmap(img, {
          resizeWidth: s,
          resizeHeight: s,
          resizeQuality: 'low',
        });
      } catch {
        source = img;
      }
    }
    try {
      const canvas = document.createElement('canvas');
      canvas.width = s;
      canvas.height = s;
      const ctx = canvas.getContext('2d', { willReadFrequently: true });
      if (!ctx) {
        return null;
      }
      ctx.drawImage(source, 0, 0, s, s);
      const data = ctx.getImageData(0, 0, s, s).data;
      let r = 0;
      let g = 0;
      let b = 0;
      let wsum = 0;
      for (let i = 0; i < data.length; i += 4) {
        const R = data[i];
        const G = data[i + 1];
        const B = data[i + 2];
        const mx = Math.max(R, G, B);
        const mn = Math.min(R, G, B);
        const sat = mx === 0 ? 0 : (mx - mn) / mx;
        // Weight vibrant, bright pixels so busy covers don't average to mud.
        const weight = sat * sat * (mx / 255) + 0.03;
        r += R * weight;
        g += G * weight;
        b += B * weight;
        wsum += weight;
      }
      return this.toTintColor(r / wsum, g / wsum, b / wsum);
    } catch {
      // Cross-origin (tainted) canvas.
      return null;
    } finally {
      if (source !== img) {
        (source as ImageBitmap).close();
      }
    }
  }

  // Normalise an averaged RGB to a clear, mid-lightness tint (hsl). Returns null
  // for near-greyscale artwork so B&W covers don't get a random invented hue.
  private toTintColor(r: number, g: number, b: number): string | null {
    r /= 255;
    g /= 255;
    b /= 255;
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    const l = (max + min) / 2;
    const d = max - min;
    let h = 0;
    let sat = 0;
    if (d !== 0) {
      sat = l > 0.5 ? d / (2 - max - min) : d / (max + min);
      switch (max) {
        case r:
          h = (g - b) / d + (g < b ? 6 : 0);
          break;
        case g:
          h = (b - r) / d + 2;
          break;
        default:
          h = (r - g) / d + 4;
          break;
      }
      h /= 6;
    }
    if (sat < 0.12) {
      return null;
    }
    const outS = Math.min(1, Math.max(sat, 0.5));
    return `hsl(${Math.round(h * 360)}, ${Math.round(outS * 100)}%, 50%)`;
  }
}
