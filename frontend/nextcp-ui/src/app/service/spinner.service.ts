import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
  visibility: BehaviorSubject<boolean>;
  private readonly showDelayMs = 1500;
  private pendingRequests = 0;
  private showTimer: ReturnType<typeof setTimeout> | null = null;
  private isVisible = false;

  constructor() {
    this.visibility = new BehaviorSubject(false);
  }

  requestStarted(): void {
    this.pendingRequests++;

    if (this.pendingRequests === 1) {
      this.scheduleShow();
    }
  }

  requestEnded(): void {
    if (this.pendingRequests === 0) {
      return;
    }

    this.pendingRequests--;

    if (this.pendingRequests === 0) {
      this.clearShowTimer();
      this.hide();
    }
  }

  show() {
    if (this.isVisible) {
      return;
    }

    this.isVisible = true;
    this.visibility.next(true);
  }

  hide() {
    if (!this.isVisible) {
      return;
    }

    this.isVisible = false;
    this.visibility.next(false);
  }

  private scheduleShow(): void {
    this.clearShowTimer();

    this.showTimer = setTimeout(() => {
      this.showTimer = null;

      if (this.pendingRequests > 0) {
        this.show();
      }
    }, this.showDelayMs);
  }

  private clearShowTimer(): void {
    if (this.showTimer) {
      clearTimeout(this.showTimer);
      this.showTimer = null;
    }
  }
}
