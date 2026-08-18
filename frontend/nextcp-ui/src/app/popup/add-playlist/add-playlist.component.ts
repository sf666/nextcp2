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
  /** One width for every mode — only the height follows the content. */
  private static readonly DIALOG_WIDTH = 560;
  private static readonly LIST_HEIGHT = 560;
  private static readonly CREATE_HEIGHT = 284;

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
    this.anchorDialog();
    if (data.item != undefined && data.item.objectID.length > 0) {
      this.musicItemToAdd.set(data.item);
      this.setMode(PlaylistMode.Add);
    } else {
      this.setMode(PlaylistMode.Create);
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
          // Drop the row right away instead of waiting for the refetch. Has to
          // be a new object: writing into the signal's current value leaves the
          // computed lists untouched, so the deleted playlist stayed on screen.
          this.serverPlaylistService.serverPl.update((pl) => ({
            ...pl,
            serverPlaylists: pl.serverPlaylists.filter(
              (entry) => entry.playlistId !== serverPlaylist.playlistId,
            ),
          }));
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
    this.setMode(PlaylistMode.Add);
  }

  newPlaylistClick(): void {
    this.setMode(PlaylistMode.Create);
  }

  deletePlaylistClick(): void {
    this.setMode(PlaylistMode.Delete);
  }

  private setMode(mode: PlaylistMode): void {
    this.playlistMode.set(mode);
    this.applyDialogSize(mode);
  }

  /**
   * The three modes hold very different amounts of content: browsing playlists
   * needs a tall panel with room to scroll, naming a new one needs a small
   * form. So the dialog follows the mode — but only on one axis. The width
   * stays put and the top edge is pinned (see anchorDialog), so switching tabs
   * grows or shrinks the panel downwards instead of resizing it in every
   * direction at once.
   *
   * Heights are explicit pixels rather than `auto` on purpose: CSS cannot
   * animate to or from `auto`, and the create form is a fixed set of
   * single-line rows, so its height is known. If it ever outgrows the box the
   * body scrolls.
   */
  private applyDialogSize(mode: PlaylistMode): void {
    this.dialogRef.updateSize(
      `${AddPlaylistComponent.DIALOG_WIDTH}px`,
      mode === PlaylistMode.Create
        ? `${AddPlaylistComponent.CREATE_HEIGHT}px`
        : `${AddPlaylistComponent.LIST_HEIGHT}px`,
    );
  }

  /**
   * Pins the top edge where the tall variant would be centred, so the tab bar
   * stays exactly where it is while the panel below it changes height.
   */
  private anchorDialog(): void {
    const tall = Math.min(
      AddPlaylistComponent.LIST_HEIGHT,
      window.innerHeight * 0.9,
    );
    const top = Math.max(16, Math.round((window.innerHeight - tall) / 2));
    this.dialogRef.updatePosition({ top: `${top}px` });
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

  /** Enter in the name field creates the playlist, unless the name is empty. */
  createOnEnter(): void {
    if (!this.addDisabled()) {
      this.createPlaylistClicked();
    }
  }

  createPlaylistClicked(): void {
    this.serverPlaylistService
      .createPlaylist(this.newPlaylistName(), this.addToContainer.id)
      .subscribe((newId) => this.playlistCreated(newId));
    this.close();
  }

  cancelClicked(): void {
    (this.newPlaylistName.set(''), this.close());
  }

  /**
   * The dialog reaches the create form two ways: from a song ("add to playlist"
   * → "create playlist"), where the new playlist is meant to hold that song,
   * and from the sidebar, where there is no song at all. Only the first case
   * has something to file — filing the empty item id made the media server
   * answer "The specified ObjectID is invalid" and raised an error toast on
   * every plain create.
   *
   * The sidebar list is already refetched by the service, so nothing else is
   * needed here.
   */
  private playlistCreated(newId: string): void {
    if (!newId) {
      // Create failed; the backend has already reported why.
      return;
    }
    if (this.musicItemToAddExists) {
      this.serverPlaylistService.addSongToServerPlaylist(
        this.musicItemToAdd().objectID,
        newId,
      );
    }
  }

  get musicItemToAddExists(): boolean {
    return this.musicItemToAdd()?.objectID?.length > 0;
  }
}
