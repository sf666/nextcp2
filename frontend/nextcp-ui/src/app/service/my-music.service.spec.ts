import { TestBed } from '@angular/core/testing';

import { MyMusicService } from './my-music.service';

describe('MyMusicService', () => {
  let service: MyMusicService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyMusicService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
