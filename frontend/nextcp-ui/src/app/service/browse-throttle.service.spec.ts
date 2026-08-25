import { TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs';

import { BrowseThrottleService } from './browse-throttle.service';
import { ToastService } from './toast/toast.service';

describe('BrowseThrottleService', () => {
  let service: BrowseThrottleService;
  let toastTitles: string[];

  /** Schedules count requests and hands back the subjects that stand in for the pending responses. */
  function scheduleRequests(count: number): Subject<string>[] {
    const responses: Subject<string>[] = [];
    for (let i = 0; i < count; i++) {
      const response = new Subject<string>();
      responses.push(response);
      service.schedule(() => response, 'browsing folders').subscribe({ error: () => {} });
    }
    return responses;
  }

  beforeEach(() => {
    vi.useFakeTimers();
    toastTitles = [];
    TestBed.configureTestingModule({
      providers: [
        {
          provide: ToastService,
          useValue: { info: (_message: string, title: string) => toastTitles.push(title) },
        },
      ],
    });
    service = TestBed.inject(BrowseThrottleService);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('stays quiet while the queue keeps moving', () => {
    // Eight requests against five slots: three have to wait, which is the normal case when paging
    // through a container with hundreds of folders.
    const responses = scheduleRequests(8);
    expect(service.running()).toBe(BrowseThrottleService.MAX_CONCURRENT);
    expect(service.queued()).toBe(3);

    // Each running request answers quickly, so the queued ones get their slot right away.
    for (const response of responses) {
      vi.advanceTimersByTime(100);
      response.complete();
    }
    vi.advanceTimersByTime(30000);

    expect(service.queued()).toBe(0);
    expect(toastTitles).toEqual([]);
  });

  it('reports once when a request cannot get a slot', () => {
    scheduleRequests(8);

    vi.advanceTimersByTime(7000);
    expect(toastTitles).toEqual([]);

    vi.advanceTimersByTime(2000);
    expect(toastTitles).toEqual(['media server is busy']);

    // Still stuck: the user is not told again about the same pile up.
    vi.advanceTimersByTime(60000);
    expect(toastTitles).toEqual(['media server is busy']);
  });

  it('drops a queued request that is unsubscribed before it starts', () => {
    scheduleRequests(5);
    const late = new Subject<string>();
    const subscription = service.schedule(() => late, 'browsing').subscribe();
    expect(service.queued()).toBe(1);

    subscription.unsubscribe();
    expect(service.queued()).toBe(0);

    vi.advanceTimersByTime(30000);
    expect(toastTitles).toEqual([]);
  });
});
