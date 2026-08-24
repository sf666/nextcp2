import { ChangeDetectionStrategy, Component, ElementRef, OutputEmitterRef, ViewContainerRef, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
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
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import {
  InputPopupComponent,
  InputPopupData,
} from 'src/app/util/comp/input-popup/input-popup/input-popup.component';
import { PopupService } from 'src/app/util/popup.service';

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
  serverPlaylistService = inject(ServerPlaylistService);
  private configurationService = inject(ConfigurationService);
  deviceService = inject(DeviceService);
  private popupService = inject(PopupService);

  readonly inputDialog = inject(MatDialog);
  readonly cdsUpdateService = inject(CdsUpdateService);

  private readonly _matDialogRef: MatDialogRef<DisplayHeaderOptionsComponent>;
  private addToPlaylistOutput: OutputEmitterRef<ContainerDto>;
  private currentContainer: ContainerDto;
  private triggerElementRef: ElementRef;
  private mediaPlayerConfigDto: MediaPlayerConfigDto;

  /** Folders carry their like here instead of next to the title. */
  canLike = false;
  isLiked = false;

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<DisplayHeaderOptionsComponent>>(MatDialogRef);
    const data = inject<{
      trigger: ElementRef;
      event: PointerEvent;
      viewContainerRef: ViewContainerRef;
      currentContainer: ContainerDto;
      addToPlaylistOutput: OutputEmitterRef<ContainerDto>;
      canLike: boolean;
      isLiked: boolean;
    }>(MAT_DIALOG_DATA);

    this._matDialogRef = _matDialogRef;
    this.addToPlaylistOutput = data.addToPlaylistOutput;
    this.currentContainer = data.currentContainer;
    this.canLike = data.canLike ?? false;
    this.isLiked = data.isLiked ?? false;
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
   * Names the action after what it acts on. Not "dislike": this app has a real
   * disliked state (rating 0, see the rating filter), and removing a like only
   * clears the rating — it does not store a dislike.
   */
  get likeLabel(): string {
    const noun = this.isPlaylist()
      ? 'playlist'
      : this.isFolder()
        ? 'folder'
        : 'item';
    return this.isLiked ? `Unlike ${noun}` : `Like ${noun}`;
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
        'Enter full album art URL. Reload parent container after update.',
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
        );
        this.close();
      }
    });
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

  selectPlayerFolderSidebar(): void {
    this.configurationService.updateServerPlaylistId(
      this.deviceService.selectedMediaServerDevice().udn,
      this.getCurrentContainerIdDto().id,
    );
    this.serverPlaylistService.updateServerAccessiblePlaylists();
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
    console.log(
      'is folder : ' +
        this.currentContainer.objectClass.startsWith(
          'object.container.storageFolder',
        ),
    );
    return this.currentContainer.objectClass.startsWith(
      'object.container.storageFolder',
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
