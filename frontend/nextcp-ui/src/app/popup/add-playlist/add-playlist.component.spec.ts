import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of } from 'rxjs';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { DeviceService } from 'src/app/service/device.service';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { AddPlaylistComponent } from './add-playlist.component';

describe('Add a playlist as an entry', () => {
  it('sends the source playlist ID through the existing add action once', () => {
    const source = {
      ...new DtoGeneratorService().generateEmptyContainerDto(),
      id: 'source',
      mediaServerUDN: 'ums',
      objectClass: 'object.container.playlistContainer',
    };
    const sourceEntry = { playlistId: 'source', playlistName: 'Source' };
    const destination = { playlistId: 'target', playlistName: 'Destination' };
    const addSongToServerPlaylist = vi.fn();
    const dialog = { updatePosition: vi.fn(), updateSize: vi.fn(), close: vi.fn() };

    TestBed.configureTestingModule({
      imports: [AddPlaylistComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: { container: source, sourcePlaylist: source } },
        { provide: MatDialogRef, useValue: dialog },
        { provide: DeviceService, useValue: { selectedMediaServerDevice: () => ({ udn: 'ums' }) } },
        { provide: ContentDirectoryService, useValue: {
          searchPlaylistsMetadataOnly: () => of({ playlistItems: [] }),
          browseChildCount: () => of(0),
        } },
        { provide: ServerPlaylistService, useValue: {
          serverPl: () => ({ serverPlaylists: [sourceEntry, destination] }),
          recentServerPl: () => ({ serverPlaylists: [] }),
          addSongToServerPlaylist,
        } },
      ],
    });
    const fixture = TestBed.createComponent(AddPlaylistComponent);
    fixture.detectChanges();

    const destinations: NodeListOf<HTMLButtonElement> = fixture.nativeElement.querySelectorAll('.row-action button');
    expect(destinations).toHaveLength(1);
    destinations[0].click();

    expect(addSongToServerPlaylist).toHaveBeenCalledExactlyOnceWith('source', 'target');
    expect(dialog.close).toHaveBeenCalledOnce();
  });
});
