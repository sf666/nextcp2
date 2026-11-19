import { DisplayHeaderOptionsComponent } from './display-header-options.component';

describe('container playlist menu', () => {
  it.each([
    ['object.container.album.musicAlbum', 'album', 'ums', true, true],
    ['object.container.album.musicAlbum', '0', 'ums', true, false],
    ['object.container.album.musicAlbum', 'album', 'other', true, false],
    ['object.container.album.musicAlbum', 'album', 'ums', false, false],
    ['object.container.person.musicArtist', 'artist', 'ums', true, false],
  ])('checks playlist support for %s', (objectClass, id, udn, extendedApi, expected) => {
    const menu = Object.create(DisplayHeaderOptionsComponent.prototype) as DisplayHeaderOptionsComponent;
    Object.assign(menu, {
      currentContainer: { objectClass, id, mediaServerUDN: udn },
      deviceService: { selectedMediaServerDevice: () => ({ udn: 'ums', extendedApi }) },
    });

    expect(menu.showAddToServerPlaylist()).toBe(expected);
  });
});
