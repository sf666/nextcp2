import { Injectable, inject, signal } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { ToastService } from './toast/toast.service';

/**
 * Limits how many browse requests are in flight at the same time, application wide.
 *
 * A browser opens only a handful of connections per origin. Every view brings its own
 * ContentDirectoryService and each of those pages a browse result in parallel, so a few open views
 * are enough to occupy every connection - and then nothing gets through any more, not the server
 * sent events, not a play command, not the settings. Browsing a media server can legitimately take
 * a while, so the answer is not a shorter timeout but fewer requests at once.
 *
 * When requests do pile up the user is told once, together with what the server is working on, so a
 * slow media server looks busy instead of broken.
 */
@Injectable({
  providedIn: 'root',
})
export class BrowseThrottleService {
  /** Leaves room for the event stream and for the calls that have to stay responsive. */
  public static readonly MAX_CONCURRENT = 3;

  private toastService = inject(ToastService);

  /** Requests currently on the wire, for anybody who wants to show an indicator. */
  public readonly running = signal<number>(0);
  /** Requests waiting for a free slot. */
  public readonly queued = signal<number>(0);

  private queue: QueuedBrowse[] = [];
  private runningLabels: string[] = [];
  private waitReported = false;

  /**
   * Runs the given request when a slot is free.
   *
   * @param work creates the request; it is called at the moment the slot is granted, never earlier
   * @param label what this request is for, shown to the user while others are waiting
   * @returns the result of the request, cancellable while it is still waiting for its slot
   */
  public schedule<T>(work: () => Observable<T>, label = 'browsing'): Observable<T> {
    return new Observable<T>((subscriber) => {
      const entry: QueuedBrowse = { cancelled: false, label, start: () => {} };
      let inner: Subscription | undefined;
      let released = false;

      const release = () => {
        if (released) {
          return;
        }
        released = true;
        const index = this.runningLabels.indexOf(label);
        if (index > -1) {
          this.runningLabels.splice(index, 1);
        }
        this.running.update((count) => count - 1);
        this.startNext();
      };

      entry.start = () => {
        if (entry.cancelled) {
          release();
          return;
        }
        inner = work().subscribe({
          next: (value) => subscriber.next(value),
          error: (err) => {
            release();
            subscriber.error(err);
          },
          complete: () => {
            release();
            subscriber.complete();
          },
        });
      };

      if (this.running() < BrowseThrottleService.MAX_CONCURRENT) {
        this.take(entry);
      } else {
        this.queue.push(entry);
        this.queued.set(this.queue.length);
        this.reportWaiting();
      }

      return () => {
        entry.cancelled = true;
        if (inner) {
          inner.unsubscribe();
          release();
        } else {
          // Still waiting for a slot: drop it, nothing was started and nothing has to be released.
          this.queue = this.queue.filter((queued) => queued !== entry);
          this.queued.set(this.queue.length);
        }
      };
    });
  }

  private take(entry: QueuedBrowse) {
    this.running.update((count) => count + 1);
    this.runningLabels.push(entry.label);
    entry.start();
  }

  private startNext() {
    const next = this.queue.shift();
    this.queued.set(this.queue.length);
    if (next) {
      this.take(next);
    } else if (this.running() === 0) {
      // Everything is through, so the next pile up is worth reporting again.
      this.waitReported = false;
    }
  }

  /**
   * Tells the user once per pile up that the media server is the bottleneck, and what it is chewing
   * on. Reporting every queued request would bury the screen in toasts.
   */
  private reportWaiting() {
    if (this.waitReported) {
      return;
    }
    this.waitReported = true;
    const busyWith = this.runningLabels[0] ?? 'browsing';
    const waiting = this.queue.length;
    this.toastService.info(
      `${busyWith} - ${waiting} more request${waiting === 1 ? '' : 's'} waiting`,
      'media server is busy',
    );
  }
}

interface QueuedBrowse {
  cancelled: boolean;
  label: string;
  start: () => void;
}
