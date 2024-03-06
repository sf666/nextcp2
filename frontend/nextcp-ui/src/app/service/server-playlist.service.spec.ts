import { TestBed } from '@angular/core/testing';

import { ServerPlaylistService } from './server-playlist.service';

describe('ServerPlaylistService', () => {
  let service: ServerPlaylistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServerPlaylistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
