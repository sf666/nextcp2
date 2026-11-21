import { signal } from '@angular/core';
import { of } from 'rxjs';

import { DisplayHeaderOptionsComponent } from './display-header-options.component';

describe('container playlist menu', () => {
  it.each([
    ['object.container.album.musicAlbum', 'album', 'ums', true, true],
    ['object.container.album.musicAlbum', '0', 'ums', true, false],
    ['object.container.album.musicAlbum', 'album', 'other', true, false],
    ['object.container.album.musicAlbum', 'album', 'ums', false, false],
    ['object.container.person.musicArtist', 'artist', 'ums', true, true],
    ['object.container.person', 'person', 'ums', false, false],
  ])('checks playlist support for %s', (objectClass, id, udn, extendedApi, expected) => {
    const menu = Object.create(DisplayHeaderOptionsComponent.prototype) as DisplayHeaderOptionsComponent;
    Object.assign(menu, {
      currentContainer: { objectClass, id, mediaServerUDN: udn },
      deviceService: { selectedMediaServerDevice: () => ({ udn: 'ums', extendedApi }) },
    });

    expect(menu.showAddToServerPlaylist()).toBe(expected);
  });

  it('removes a container link even when its type cannot be added to a playlist', () => {
    const removePlaylistLink = vi.fn(() => of(undefined));
    const menu = Object.create(DisplayHeaderOptionsComponent.prototype) as DisplayHeaderOptionsComponent;
    Object.assign(menu, {
      currentContainer: {
        id: 'artist-link',
        objectClass: 'object.container.genre.musicGenre',
        mediaServerUDN: 'ums',
      },
      parentObjectClass: 'object.container.playlistContainer',
      deviceService: { selectedMediaServerDevice: () => ({ udn: 'ums', extendedApi: true }) },
      serverPlaylistService: { removePlaylistLink },
      inputDialog: { open: () => ({ afterClosed: () => of(true) }) },
      removingPlaylistLink: signal(false),
      _matDialogRef: { close: vi.fn() },
    });

    expect(menu.canRemovePlaylistLink).toBe(true);
    menu.removeFromPlaylist();

    expect(removePlaylistLink).toHaveBeenCalledExactlyOnceWith('artist-link', 'ums');
  });
});
