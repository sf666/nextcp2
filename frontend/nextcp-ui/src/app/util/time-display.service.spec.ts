import { TestBed } from '@angular/core/testing';

import { TimeDisplayService } from './time-display.service';

describe('TimeDisplayService', () => {
  let service: TimeDisplayService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeDisplayService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
