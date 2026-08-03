import { fakeAsync, TestBed, tick } from '@angular/core/testing';

import { SpinnerService } from './spinner.service';

describe('SpinnerService', () => {
  let service: SpinnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpinnerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should show spinner only after the configured delay', fakeAsync(() => {
    service.requestStarted();

    tick(1499);
    expect(service.isLoading()).toBe(false);

    tick(1);
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(false);
  }));

  it('should not show spinner for quick requests', fakeAsync(() => {
    service.requestStarted();

    tick(400);
    service.requestEnded();

    tick(2000);
    expect(service.isLoading()).toBe(false);
  }));

  it('should stay visible until all requests are done', fakeAsync(() => {
    service.requestStarted();
    service.requestStarted();

    tick(1500);
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(true);

    service.requestEnded();
    expect(service.isLoading()).toBe(false);
  }));
});
