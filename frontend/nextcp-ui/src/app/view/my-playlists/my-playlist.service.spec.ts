import { TestBed } from '@angular/core/testing';

import { MyPlaylistService } from './my-playlist.service';

describe('MyPlaylistService', () => {
  let service: MyPlaylistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyPlaylistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
