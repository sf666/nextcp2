import { Injectable } from '@angular/core';
import { supportsBackdropFilter } from './browser-capabilities';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {


  public setBackgroundImageMainScreen(url: string): void {
    const element = document.getElementById('main-screen');
    if (supportsBackdropFilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setFooterBackgroundImage(url: string): void {
    const element = document.getElementById('footer-background');
    if (supportsBackdropFilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setDisplayContainerHeaderImage(url: string): void {
    if (supportsBackdropFilter && document.getElementById('header-background')) {
      let element = document.getElementById('header-background');
      if (element) {
        element.style.backgroundImage = 'url("' + url + '")';
      }
    }
  }

  /**
   * Extract the artwork's dominant (vibrant-weighted) colour and publish it as
   * the CSS variable `--ambient-color` — used to tint the sidebar reliably (the
   * full-screen wash leaves the far-left sidebar grey for dark-edged covers).
   * Needs a CORS-readable image; on any failure it clears the variable and the
   * sidebar falls back to neutral.
   */
  public applyAmbientTint(url: string): void {
    if (!supportsBackdropFilter || !url) {
      return;
    }
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.onload = () => {
      try {
        const s = 16;
        const canvas = document.createElement('canvas');
        canvas.width = s;
        canvas.height = s;
        const ctx = canvas.getContext('2d');
        if (!ctx) {
          return;
        }
        ctx.drawImage(img, 0, 0, s, s);
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
        const tint = this.toTintColor(r / wsum, g / wsum, b / wsum);
        if (tint) {
          document.documentElement.style.setProperty('--ambient-color', tint);
        } else {
          document.documentElement.style.removeProperty('--ambient-color');
        }
      } catch {
        // Cross-origin (tainted) canvas — leave the sidebar neutral.
        document.documentElement.style.removeProperty('--ambient-color');
      }
    };
    img.onerror = () =>
      document.documentElement.style.removeProperty('--ambient-color');
    img.src = url;
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

