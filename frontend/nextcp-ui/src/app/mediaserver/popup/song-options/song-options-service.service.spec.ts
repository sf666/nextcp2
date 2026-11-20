import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { SongOptionsServiceService } from './song-options-service.service';
import { SongOptionsComponent } from './song-options/song-options.component';

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

describe('playlist item options', () => {
  it('uses the object ID to remove a video only after confirmation', () => {
    const deleteObject = vi.fn();
    const afterClosed = vi.fn()
      .mockReturnValueOnce(of(undefined))
      .mockReturnValueOnce(of(true));
    const menu = Object.create(SongOptionsComponent.prototype) as SongOptionsComponent;
    Object.assign(menu, {
      item: { objectID: 'video-link', title: 'Video' },
      currentContainer: { objectClass: 'object.container.playlistContainer' },
      dialog: { open: () => ({ afterClosed }) },
      serverPlaylistService: { deleteObject },
      defaultPlaylistService: { close: vi.fn() },
      _matDialogRef: { close: vi.fn() },
    });

    expect(menu.isParentPlaylist).toBe(true);
    expect(menu.hasValidSongId()).toBe(false);
    menu.deleteFromPlaylist();
    expect(deleteObject).not.toHaveBeenCalled();

    menu.deleteFromPlaylist();
    expect(deleteObject).toHaveBeenCalledExactlyOnceWith('video-link');
  });
});
