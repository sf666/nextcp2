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
  private static readonly RELOAD_ON_FOCUS_STORAGE_KEY = 'reloadOnFocusEnabled';

  readonly isVisible = signal<boolean>(!this.document.hidden);
  readonly reloadOnFocusEnabled = signal<boolean>(false);
  private initialized = false;

  private readonly listener = () => {
    this.isVisible.set(!this.document.hidden);
  };

  constructor() {
    this.reloadOnFocusEnabled.set(this.readReloadOnFocusFlag());
    this.document.addEventListener('visibilitychange', this.listener);

    effect(() => {
      const visible = this.isVisible();
      const reloadEnabled = this.reloadOnFocusEnabled();

      // skip initial effect run to avoid unnecessary route reload on app start
      if (!this.initialized) {
        this.initialized = true;
        return;
      }

      if (visible && reloadEnabled) {
        untracked(() => this.reloadCurrentRoute());
      }
    });
  }

  setReloadOnFocusEnabled(enabled: boolean): void {
    this.reloadOnFocusEnabled.set(enabled);
    this.writeReloadOnFocusFlag(enabled);
  }

  toggleReloadOnFocus(): void {
    this.setReloadOnFocusEnabled(!this.reloadOnFocusEnabled());
  }

  private readReloadOnFocusFlag(): boolean {
    const storage = this.document.defaultView?.localStorage;
    if (!storage) {
      return false;
    }

    const rawValue = storage.getItem(AppVisibilityService.RELOAD_ON_FOCUS_STORAGE_KEY);
    return rawValue === 'true';
  }

  private writeReloadOnFocusFlag(enabled: boolean): void {
    const storage = this.document.defaultView?.localStorage;
    if (!storage) {
      return;
    }

    storage.setItem(AppVisibilityService.RELOAD_ON_FOCUS_STORAGE_KEY, String(enabled));
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