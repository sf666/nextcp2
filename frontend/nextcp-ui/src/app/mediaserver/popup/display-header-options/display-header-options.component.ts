import { ChangeDetectionStrategy, Component, ElementRef, OutputEmitterRef, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { AddRadioStationComponent } from 'src/app/popup/add-radio-station/add-radio-station.component';
import { CdsUpdateService } from 'src/app/service/cds-update.service';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import {
  ContainerDto,
  ContainerIdDto,
  MediaPlayerConfigDto,
  MusicItemIdDto,
} from 'src/app/service/dto';
import { MediaPlayerService } from 'src/app/service/media-player/media-player.service';
import {
  InputPopupComponent,
  InputPopupData,
} from 'src/app/util/comp/input-popup/input-popup/input-popup.component';
import { PopupService } from 'src/app/util/popup.service';
import { ServerFeature } from 'src/app/service/server-feature';

/** Menu width in px. Keep in step with `.dialog-root` in the component's SCSS. */
const MENU_WIDTH = 320;

@Component({
  selector: 'app-display-header-options',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  templateUrl: './display-header-options.component.html',
  styleUrl: './display-header-options.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DisplayHeaderOptionsComponent implements OnInit {
  mediaPlayerService = inject(MediaPlayerService);
  private configurationService = inject(ConfigurationService);
  deviceService = inject(DeviceService);
  private popupService = inject(PopupService);

  readonly inputDialog = inject(MatDialog);
  readonly cdsUpdateService = inject(CdsUpdateService);

  private readonly _matDialogRef: MatDialogRef<DisplayHeaderOptionsComponent>;
  private addToPlaylistOutput: OutputEmitterRef<ContainerDto>;
  private currentContainer: ContainerDto;
  /** The container whose listing this menu was opened from - see the data below. */
  private listingContainerId: string;
  private triggerElementRef: ElementRef;
  private mediaPlayerConfigDto: MediaPlayerConfigDto;

  /** Folders carry their like here instead of next to the title. */
  canLike = false;
  isLiked = false;
  /** How many folders this container holds; albums and tracks do not count. */
  private childFolderCount = 0;

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<DisplayHeaderOptionsComponent>>(MatDialogRef);
    const data = inject<{
      trigger: ElementRef;
      currentContainer: ContainerDto;
      addToPlaylistOutput: OutputEmitterRef<ContainerDto>;
      canLike: boolean;
      isLiked: boolean;
      childFolderCount: number;
      /**
       * The container that is on screen. The header opens this menu on the container it is the
       * header of, a tile on one that is merely listed there - and what has to be re-read after a
       * change is the listing, not the entry. Defaults to the container itself.
       */
      listingContainerId?: string;
    }>(MAT_DIALOG_DATA);

    this._matDialogRef = _matDialogRef;
    this.addToPlaylistOutput = data.addToPlaylistOutput;
    this.currentContainer = data.currentContainer;
    this.listingContainerId =
      data.listingContainerId ?? data.currentContainer.id;
    this.canLike = data.canLike ?? false;
    this.isLiked = data.isLiked ?? false;
    this.childFolderCount = data.childFolderCount ?? 0;
    this.triggerElementRef = data.trigger;
    this.mediaPlayerConfigDto =
      this.configurationService.mediaPlayerConfigDto();
  }

  ngOnInit(): void {
    // Width only - the menu shows a different set of rows per container type, so
    // its height is whatever the rows add up to.
    this.popupService.configurePopupAtTrigger(
      this._matDialogRef,
      this.triggerElementRef,
      MENU_WIDTH,
    );
  }

  addToPlaylist(): void {
    this.addToPlaylistOutput.emit(this.currentContainer);
    this.close();
  }

  /**
   * Names the action after what it acts on. Taking a like back clears the rating,
   * which is all there is to it - the app knows liked and not liked, nothing else.
   */
  get likeLabel(): string {
    const noun = this.containerNoun();
    return this.isLiked ? `Unlike ${noun}` : `Like ${noun}`;
  }

  /** What the menu calls the container it was opened on. */
  private containerNoun(): string {
    if (this.isPlaylist()) {
      return 'playlist';
    }
    if (this.isFolder()) {
      return 'folder';
    }
    return this.isAlbum() ? 'album' : 'item';
  }

  /** Player rows exist for playlists and folders only - no header without rows. */
  showPlayerSection(): boolean {
    return (
      this.mediaPlayerService.mediaPlayerExists() &&
      (this.isPlaylist() || this.isFolder())
    );
  }

  /** Playlists only - a folder's like belongs under "General actions". */
  showPlaylistSection(): boolean {
    return this.isPlaylist() && (this.canLike || this.showAddRadioStation());
  }

  showAddRadioStation(): boolean {
    return (
      this.isPlaylist() && this.deviceService.hasFeature(ServerFeature.RADIO_BROWSER)
    );
  }

  /**
   * The album-artist folder is the one that holds the artist folders, so the row only makes sense
   * on a folder that has folders of its own - not on a playlist and not on a folder of tracks.
   */
  showArtistFolderRow(): boolean {
    return (
      this.deviceService.hasFeature(ServerFeature.ARTIST_FOLDER) &&
      this.isFolder() &&
      this.childFolderCount > 2
    );
  }

  /**
   * Reports the choice back to the header, which owns the container's rating
   * state and performs the call.
   */
  toggleLike(): void {
    this._matDialogRef.close('toggleLike');
  }

  updateAlbumArt(): void {
    const inputTextData: InputPopupData = {
      cancelText: 'cancel',
      inputText: '',
      inputTextExplanation:
        'Enter full album art URL.',
      labelInputText: '',
      okText: 'update',
      title: 'Update album art',
    };
    const dialogRef = this.inputDialog.open(InputPopupComponent, {
      width: '480px',
      maxWidth: '640px',
      panelClass: ['popup-glass'],
      data: inputTextData,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result !== undefined) {
        const musicItemId: MusicItemIdDto = {
          acoustID: '',
          musicBrainzIdTrackId: '',
          objectID: this.currentContainer.id,
        };

        this.cdsUpdateService.setNewAlbumArtUri(
          musicItemId,
          this.currentContainer.albumartUri,
          result,
          this.listingContainerId,
        );
        this.close();
      }
    });
  }

  /** Picks a station from radio-browser and appends it to this playlist. */
  addRadioStation(): void {
    this.inputDialog.open(AddRadioStationComponent, {
      width: '620px',
      maxWidth: '92vw',
      height: '640px',
      maxHeight: '90vh',
      panelClass: ['popup-glass'],
      data: { playlist: this.currentContainer },
    });
    this.close();
  }

  selectArtistFolder(): void {
    this.configurationService.setAlbumArtistFolder(
      this.deviceService.selectedMediaServerDevice().udn,
      this.getCurrentContainerIdDto().id,
    );
    this.close();
  }

  selectPlayerFolder(): void {
    this.mediaPlayerConfigDto.addToFolderId = this.getCurrentContainerIdDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
    this.close();
  }

  selectPlayerPlaylist(): void {
    this.mediaPlayerConfigDto.addToPlaylistId = this.getCurrentContainerIdDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
    this.close();
  }

  isPlaylist(): boolean {
    return this.currentContainer.objectClass.startsWith(
      'object.container.playlistContainer',
    );
  }

  isFolder(): boolean {
    return this.currentContainer.objectClass.startsWith(
      'object.container.storageFolder',
    );
  }

  isAlbum(): boolean {
    return this.currentContainer.objectClass.startsWith(
      'object.container.album',
    );
  }

  public getCurrentContainerIdDto(): ContainerIdDto {
    return {
      id: this.currentContainer.id,
      title: this.currentContainer.title,
    };
  }

  private close(): void {
    this._matDialogRef.close(); // no return result ...
  }
}
