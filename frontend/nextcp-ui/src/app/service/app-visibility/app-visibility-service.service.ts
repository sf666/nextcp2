import { Injectable, OnDestroy, inject, signal, effect, untracked } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router } from '@angular/router';

/**
 * This service tracks the visibility of the app and reloads the current route when the app becomes visible again.
 * This is useful to ensure that the app is up-to-date when the user switches back to it after being away for a while.
 * The route reload is skipped on the initial app load to avoid unnecessary reloads.
 * The service listens to the 'visibilitychange' event on the document and updates the isVisible signal accordingly.
 * When the app becomes visible, it triggers an effect that reloads the current route.
 */
@Injectable({ providedIn: 'root' })
export class AppVisibilityService implements OnDestroy {
  private readonly document = inject(DOCUMENT);
  private readonly router = inject(Router);

  readonly isVisible = signal<boolean>(!this.document.hidden);
  private initialized = false;

  private readonly listener = () => {
    this.isVisible.set(!this.document.hidden);
  };

  constructor() {
    this.document.addEventListener('visibilitychange', this.listener);

    effect(() => {
      const visible = this.isVisible();

      // skip initial effect run to avoid unnecessary route reload on app start
      if (!this.initialized) {
        this.initialized = true;
        return;
      }

      if (visible) {        
        untracked(() => this.reloadCurrentRoute());
      }
    });
  }

  private async reloadCurrentRoute(): Promise<void> {
    const currentUrl = this.router.url;
    console.log(`reload route: ${currentUrl}`);

    await this.router.navigateByUrl('/', { skipLocationChange: true });
    await this.router.navigateByUrl(currentUrl);
  }

  ngOnDestroy(): void {
    this.document.removeEventListener('visibilitychange', this.listener);
  }
}