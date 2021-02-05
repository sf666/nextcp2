import { TestBed } from '@angular/core/testing';

import { TrackQualityService } from './track-quality.service';

describe('TrackQualityService', () => {
  let service: TrackQualityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackQualityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
