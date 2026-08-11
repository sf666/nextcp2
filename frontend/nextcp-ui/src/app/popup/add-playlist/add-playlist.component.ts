import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import {
  MusicItemDto,
  ServerPlaylistDto,
  SearchResultDto,
  ContainerDto,
} from './../../service/dto.d';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  model,
  signal,
  inject,
} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { DeviceService } from 'src/app/service/device.service';
import { FormsModule } from '@angular/forms';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { PlaylistContainerComponent } from './playlist-container/playlist-container.component';
import {
  ConfirmPopupComponent,
  ConfirmPopupData,
} from 'src/app/util/comp/confirm-popup/confirm-popup.component';

export enum PlaylistMode {
  Add,
  Create,
  Delete,
}

@Component({
  selector: 'app-playlist-management',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, PlaylistContainerComponent],
  templateUrl: './add-playlist.component.html',
  styleUrl: './add-playlist.component.scss',
})
export class AddPlaylistComponent {
  serverPlaylistService = inject(ServerPlaylistService);
  private contentDirectoryService = inject(ContentDirectoryService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private confirmDialog = inject(MatDialog);
  dialogRef = inject<MatDialogRef<AddPlaylistComponent>>(MatDialogRef);

  PlaylistModeEnum: typeof PlaylistMode = PlaylistMode;
  addToContainer: ContainerDto;

  otherPlaylists = signal<ServerPlaylistDto[]>([]);
  playlistFilter = model<string>('');
  musicItemToAdd = signal<MusicItemDto>(
    this.dtoGeneratorService.emptyMusicItemDto(),
  );
  newPlaylistName = model<string>('');
  playlistMode = signal<PlaylistMode>(PlaylistMode.Add);

  filteredServerPlaylists = computed(() => {
    return this.serverPlaylistService
      .serverPl()
      .serverPlaylists.filter((pl) => {
        if (this.playlistFilter().length > 0) {
          return pl.playlistName
            .toLowerCase()
            .includes(this.playlistFilter().toLowerCase());
        } else {
          return true;
        }
      });
  });

  filteredOtherPlaylists = computed(() => {
    if (this.playlistFilter().length > 0) {
      return this.otherPlaylists().filter((pl) =>
        pl.playlistName
          .toLowerCase()
          .includes(this.playlistFilter().toLowerCase()),
      );
    } else {
      return this.otherPlaylists();
    }
  });

  filteredRecentPlaylists = computed(() => {
    return this.serverPlaylistService
      .recentServerPl()
      .serverPlaylists.filter((pl) => {
        console.log('Playlist name : ' + pl.playlistName);
        if (this.playlistFilter().length > 0) {
          return pl.playlistName
            .toLowerCase()
            .includes(this.playlistFilter().toLowerCase());
        } else {
          return true;
        }
      });
  });

  constructor() {
    const data = inject<{
      item: MusicItemDto | undefined;
      container: ContainerDto;
    }>(MAT_DIALOG_DATA);
    const deviceService = inject(DeviceService);
    const dtoGeneratorService = this.dtoGeneratorService;

    this.addToContainer = data.container;
    if (data.item != undefined && data.item.objectID.length > 0) {
      this.playlistMode.set(PlaylistMode.Add);
      this.musicItemToAdd.set(data.item);
    } else {
      this.playlistMode.set(PlaylistMode.Create);
    }
    let sr = dtoGeneratorService.generateEmptySearchRequestDto();
    sr.searchRequest = '';
    sr.mediaServerUDN = deviceService.selectedMediaServerDevice().udn;
    this.contentDirectoryService
      .searchAllPlaylist(sr)
      .subscribe((data) => this.updateOtherPlaylists(data));
  }

  private updateOtherPlaylists(data: SearchResultDto): void {
    let newPl: ServerPlaylistDto[] = [];

    let other = data.playlistItems.filter(
      (spe) =>
        !this.serverPlaylistService.playlistIdExistsInServerPlaylists(spe.id),
    );
    other?.forEach((pl) => {
      const entry = {} as ServerPlaylistDto;
      ((entry.albumArtUrl = pl.albumartUri),
        (entry.playlistId = pl.id),
        (entry.playlistName = pl.title),
        newPl.push(entry));
    });
    this.otherPlaylists.set(newPl);
  }

  public cancel(): void {
    this.dialogRef.close();
  }

  getRecentPlaylistsCount(): number {
    return this.filteredRecentPlaylists()?.length ?? 0;
  }

  getServerPlaylistsCount(): number {
    return this.serverPlaylistService.serverPl().serverPlaylists?.length ?? 0;
  }

  getOtherPlaylistsCount(): number {
    return this.otherPlaylists()?.length ?? 0;
  }

  addTo(serverPlaylist: ServerPlaylistDto) {
    this.serverPlaylistService.addSongToServerPlaylist(
      this.musicItemToAdd().objectID,
      serverPlaylist.playlistId,
    );
    this.close();
  }

  /**
   * Deleting a playlist on the server cannot be undone, and a whole row is an
   * easy mis-click — so ask first, naming the playlist.
   */
  deletePlaylist(serverPlaylist: ServerPlaylistDto) {
    const confirmData: ConfirmPopupData = {
      title: 'Delete playlist',
      message:
        'This deletes the playlist on the media server. It cannot be undone.',
      detail: serverPlaylist.playlistName,
      confirmText: 'delete',
      cancelText: 'cancel',
      danger: true,
    };
    const dialogRef = this.confirmDialog.open(ConfirmPopupComponent, {
      width: '420px',
      maxWidth: '90vw',
      panelClass: ['popup-glass'],
      data: confirmData,
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed === true) {
        this.doDeletePlaylist(serverPlaylist);
      }
    });
  }

  private doDeletePlaylist(serverPlaylist: ServerPlaylistDto) {
    this.serverPlaylistService
      .deleteObject(serverPlaylist.playlistId)
      .subscribe({
        next: (data) => {
          this.serverPlaylistService.serverPl().serverPlaylists =
            this.serverPlaylistService
              .serverPl()
              .serverPlaylists.filter(
                (pl) => pl.playlistId !== serverPlaylist.playlistId,
              );
        },
        error: (data) => {
          console.error(data);
        },
      });
  }

  close(): void {
    this.dialogRef.close();
  }

  addPlaylistClick(): void {
    this.playlistMode.set(PlaylistMode.Add);
  }

  newPlaylistClick(): void {
    this.playlistMode.set(PlaylistMode.Create);
  }

  deletePlaylistClick(): void {
    this.playlistMode.set(PlaylistMode.Delete);
  }

  isPlaylistMode(mode: PlaylistMode): boolean {
    if (mode.valueOf() === this.playlistMode().valueOf()) {
      return true;
    } else {
      return false;
    }
  }

  addDisabled(): boolean {
    return this.newPlaylistName().length == 0;
  }

  createPlaylistClicked(): void {
    this.serverPlaylistService
      .createPlaylist(this.newPlaylistName(), this.addToContainer.id)
      .subscribe((newId) => this.newPlaylistId(newId));
    this.close();
  }

  cancelClicked(): void {
    (this.newPlaylistName.set(''), this.close());
  }

  private newPlaylistId(newId: string) {
    this.addTo({
      playlistId: newId,
      albumArtUrl: '',
      playlistName: '',
      numberOfElements: 0,
      totalPlaytime: '',
    });
    this.serverPlaylistService.updateServerAccessiblePlaylists();
  }

  get musicItemToAddExists(): boolean {
    return this.musicItemToAdd()?.objectID?.length > 0;
  }
}
