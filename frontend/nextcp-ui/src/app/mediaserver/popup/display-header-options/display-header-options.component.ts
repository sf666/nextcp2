import { ChangeDetectionStrategy, Component, ElementRef, Inject, Output, OutputEmitterRef, ViewContainerRef, OnInit, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { CdsUpdateService } from 'src/app/service/cds-update.service';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, ContainerIdDto, MediaPlayerConfigDto, MusicItemIdDto } from 'src/app/service/dto';
import { MediaPlayerService } from 'src/app/service/media-player/media-player.service';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { InputPopupComponent, InputPopupData } from 'src/app/util/comp/input-popup/input-popup/input-popup.component';
import { PopupService } from 'src/app/util/popup.service';

@Component({
  selector: 'app-display-header-options',
  standalone: true,
  imports: [
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './display-header-options.component.html',
  styleUrl: './display-header-options.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DisplayHeaderOptionsComponent implements OnInit {

  readonly inputDialog = inject(MatDialog);
  readonly cdsUpdateService = inject(CdsUpdateService);

  private readonly _matDialogRef: MatDialogRef<DisplayHeaderOptionsComponent>;
  private addToPlaylistOutput: OutputEmitterRef<ContainerDto>;
  private currentContainer: ContainerDto;
  private triggerElementRef: ElementRef;
  private mediaPlayerConfigDto: MediaPlayerConfigDto;

  constructor(
    public mediaPlayerService: MediaPlayerService,
    public serverPlaylistService: ServerPlaylistService,
    private configurationService: ConfigurationService,
    public deviceService: DeviceService,
    private popupService: PopupService,
    _matDialogRef: MatDialogRef<DisplayHeaderOptionsComponent>,
    @Inject(MAT_DIALOG_DATA) data: {
      trigger: ElementRef,
      event: PointerEvent,
      viewContainerRef: ViewContainerRef,
      currentContainer: ContainerDto,
      addToPlaylistOutput: OutputEmitterRef<ContainerDto>,
    },
  ) {
    this._matDialogRef = _matDialogRef;
    this.addToPlaylistOutput = data.addToPlaylistOutput;
    this.currentContainer = data.currentContainer;
    this.triggerElementRef = data.trigger;
    this.mediaPlayerConfigDto = this.configurationService.mediaPlayerConfigDto();
  }

  ngOnInit(): void {
    let height = 210;
    if (this.isFolder()) {
      height = height + 30;       // for set MY PLAYLISTS option
      height = height + 30;       // for update album art option
      if (this.deviceService.selectedMediaServerDevice().extendedApi) {
          console.log('extended API supported, adding options for artist folder and player folder');
          height = height + 30;     // for set ARTIST FOLDER option (ums devices)
        if (this.mediaPlayerService.mediaPlayerExists()) {
          console.log('media player exists, adding option for player folder');
          height = height + 30;     // for select player folder option
        }
      }
    }
    if(this.mediaPlayerService.mediaPlayerExists() && this.isPlaylist()) {
      height = height + 30; // for select player playlist option
      console.log('playlist detected, adding option for select player playlist');
    }

    if (this.isPlaylist()) {
      height = height + 30; // for add to playlist option
    }

    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, height);
  }

  addToPlaylist(): void {
    this.addToPlaylistOutput.emit(this.currentContainer);
    this.close();
  }

  updateAlbumArt(): void {
    const inputTextData: InputPopupData = {
      cancelText: 'cancel',
      inputText: '',
      inputTextExplanation: 'Enter full album art URL. Reload parent container after update.',
      labelInputText: '',
      okText: 'update',
      title: 'Update album art',
    };
    const dialogRef = this.inputDialog.open(InputPopupComponent, {
      width: '480px',
      maxWidth: '640px',
      data: inputTextData,
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        const musicItemId: MusicItemIdDto = {
          acoustID: '',
          musicBrainzIdTrackId: '',
          objectID: this.currentContainer.id,
        };

        this.cdsUpdateService.setNewAlbumArtUri(musicItemId, this.currentContainer.albumartUri, result);
        this.close();
      }
    });
  }

  selectArtistFolder(): void {
    this.configurationService.setAlbumArtistFolder(this.deviceService.selectedMediaServerDevice().udn, this.getCurrentContainerIdDto().id);
    this.close();
  }

  selectPlayerFolder(): void {
    this.mediaPlayerConfigDto.addToFolderId = this.getCurrentContainerIdDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
    this.close();
  }

  selectPlayerFolderSidebar(): void {
    this.configurationService.updateServerPlaylistId(this.deviceService.selectedMediaServerDevice().udn, this.getCurrentContainerIdDto().id);;
    this.serverPlaylistService.updateServerAccessiblePlaylists();
    this.close();
  }

  selectPlayerPlaylist(): void {
    this.mediaPlayerConfigDto.addToPlaylistId = this.getCurrentContainerIdDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
    this.close();
  }

  isPlaylist(): boolean {
    return this.currentContainer.objectClass.startsWith('object.container.playlistContainer');
  }

  isFolder(): boolean {
    console.log('is folder : ' + this.currentContainer.objectClass.startsWith('object.container.storageFolder'));
    return this.currentContainer.objectClass.startsWith('object.container.storageFolder');
  }

  public getCurrentContainerIdDto(): ContainerIdDto {
    return {
      id: this.currentContainer.id,
      title: this.currentContainer.title
    }
  }  

  private close() : void {
    this._matDialogRef.close(); // no return result ...   
  }
}
