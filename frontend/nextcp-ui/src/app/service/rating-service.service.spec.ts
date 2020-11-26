import { TestBed } from '@angular/core/testing';

import { RatingServiceService } from './rating-service.service';

describe('RatingServiceService', () => {
  let service: RatingServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RatingServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
