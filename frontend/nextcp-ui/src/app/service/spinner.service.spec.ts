import { TestBed } from '@angular/core/testing';

import { SpinnerService } from './spinner.service';

// The app is zoneless, so Angular's fakeAsync/tick (which need zone.js/testing)
// don't apply — we drive the 1.5 s delay with Vitest's fake timers, which control
// the setTimeout that RxJS `timer()` uses under the hood.
describe('SpinnerService', () => {
  let service: SpinnerService;

  beforeEach(() => {
    vi.useFakeTimers();
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpinnerService);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should show spinner only after the configured delay', () => {
    service.requestStarted();

    vi.advanceTimersByTime(1499);
    expect(service.isLoading()).toBe(false);

    vi.advanceTimersByTime(1);
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(false);
  });

  it('should not show spinner for quick requests', () => {
    service.requestStarted();

    vi.advanceTimersByTime(400);
    service.requestEnded();

    vi.advanceTimersByTime(2000);
    expect(service.isLoading()).toBe(false);
  });

  it('should stay visible until all requests are done', () => {
    service.requestStarted();
    service.requestStarted();

    vi.advanceTimersByTime(1500);
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(false);
  });
});
