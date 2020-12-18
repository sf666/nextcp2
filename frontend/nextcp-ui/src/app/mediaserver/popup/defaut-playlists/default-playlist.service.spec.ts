import { TestBed } from '@angular/core/testing';

import { DefaultPlaylistService } from './default-playlist.service';

describe('DefaultPlaylistService', () => {
  let service: DefaultPlaylistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultPlaylistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
