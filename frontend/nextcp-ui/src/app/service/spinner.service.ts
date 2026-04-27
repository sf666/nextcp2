import { Injectable, Signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { distinctUntilChanged, map, of, Subject, switchMap, timer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
  private pendingRequests = 0;
  private readonly pending$ = new Subject<number>();

  /**
   * Signal that becomes `true` only after 1.5 s of continuous pending requests.
   * Quick responses never show the spinner.
   * Becomes `false` immediately when all requests complete.
   */
  readonly isLoading: Signal<boolean> = toSignal(
    this.pending$.pipe(
      map(count => count > 0),
      distinctUntilChanged(),
      switchMap(busy => busy ? timer(1500).pipe(map(() => true)) : of(false)),
      distinctUntilChanged()
    ),
    { initialValue: false }
  );

  requestStarted(): void {
    this.pendingRequests++;
    this.pending$.next(this.pendingRequests);
  }

  requestEnded(): void {
    if (this.pendingRequests > 0) {
      this.pendingRequests--;
      this.pending$.next(this.pendingRequests);
    }
  }
}
