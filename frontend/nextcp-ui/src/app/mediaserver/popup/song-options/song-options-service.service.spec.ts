import { TestBed } from '@angular/core/testing';

import { SongOptionsServiceService } from './song-options-service.service';

describe('SongOptionsServiceService', () => {
  let service: SongOptionsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SongOptionsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
