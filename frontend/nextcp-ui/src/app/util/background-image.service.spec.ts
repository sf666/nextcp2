import { TestBed } from '@angular/core/testing';

import { BackgroundImageService } from './background-image.service';

describe('BackgroundImageService', () => {
  let service: BackgroundImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BackgroundImageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
