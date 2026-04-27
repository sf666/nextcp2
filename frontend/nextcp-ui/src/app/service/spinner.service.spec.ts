import { fakeAsync, TestBed, tick } from '@angular/core/testing';

import { SpinnerService } from './spinner.service';

describe('SpinnerService', () => {
  let service: SpinnerService;
  let latestVisibility: boolean | null = null;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpinnerService);
    service.visibility.subscribe((visible) => {
      latestVisibility = visible;
    });
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should show spinner only after the configured delay', fakeAsync(() => {
    service.requestStarted();

    tick(1499);
    expect(latestVisibility).not.toBeTrue();

    tick(1);
    expect(latestVisibility).toBeTrue();

    service.requestEnded();
    expect(latestVisibility).toBeFalse();
  }));

  it('should not show spinner for quick requests', fakeAsync(() => {
    service.requestStarted();

    tick(400);
    service.requestEnded();

    tick(2000);
    expect(latestVisibility).not.toBeTrue();
  }));

  it('should stay visible until all requests are done', fakeAsync(() => {
    service.requestStarted();
    service.requestStarted();

    tick(1500);
    expect(latestVisibility).toBeTrue();

    service.requestEnded();
    expect(latestVisibility).toBeTrue();

    service.requestEnded();
    expect(latestVisibility).toBeFalse();
  }));
});
