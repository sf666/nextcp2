import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { UpdateAlbumArtComponent } from 'src/app/popup/update-album-art/update-album-art.component';

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
  it('uses the video object ID for playlist and cover actions without a song ID', () => {
    const item = {
      objectID: 'video-link',
      objectClass: 'object.item.videoItem',
      mediaServerUDN: 'ums',
      title: 'Video',
      albumArtUrl: '/video-cover.jpg',
    };
    const currentContainer = { id: 'playlist', objectClass: 'object.container.playlistContainer' };
    const dialogRef = { afterClosed: () => of(undefined) };
    const openPlaylist = vi.fn(() => dialogRef);
    const openDialog = vi.fn(() => dialogRef);
    const menu = Object.create(SongOptionsComponent.prototype) as SongOptionsComponent;
    Object.assign(menu, {
      item,
      currentContainer,
      deviceService: {
        selectedMediaServerDevice: () => ({ udn: 'ums', extendedApi: true }),
        hasFeature: () => true,
      },
      dialog: { open: openDialog },
      defaultPlaylistService: { openAddGlobalPlaylistDialogWithBackdrop: openPlaylist },
      _matDialogRef: { close: vi.fn() },
    });

    expect(menu.hasValidSongId()).toBe(false);
    expect(menu.isWebRadio()).toBe(false);
    expect(menu.canEditOnServer).toBe(true);
    menu.openAddToPlaylistDialog();
    menu.updateAlbumArt();

    expect(openPlaylist).toHaveBeenCalledExactlyOnceWith(item, currentContainer);
    expect(openDialog).toHaveBeenCalledExactlyOnceWith(
      UpdateAlbumArtComponent,
      expect.objectContaining({
        data: { objectId: 'video-link', previousAlbumArtUri: '/video-cover.jpg', containerId: 'playlist' },
      }),
    );
  });

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

    expect(menu.canRemoveFromPlaylist).toBe(true);
    expect(menu.hasValidSongId()).toBe(false);
    menu.deleteFromPlaylist();
    expect(deleteObject).not.toHaveBeenCalled();

    menu.deleteFromPlaylist();
    expect(deleteObject).toHaveBeenCalledExactlyOnceWith('video-link');
  });
});
