import { ChangeDetectionStrategy, Component, ElementRef, Inject, Output, OutputEmitterRef, ViewContainerRef, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, ContainerIdDto, MediaPlayerConfigDto } from 'src/app/service/dto';
import { MediaPlayerService } from 'src/app/service/media-player/media-player.service';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
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
    let height = 140;
    if (this.isFolder()) {
      height = height + 30;       // for set MY PLAYLISTS option
      if (this.deviceService.selectedMediaServerDevice().extendedApi) {
          height = height + 30;     // for set ARTIST FOLDER option (ums devices)
        if (this.mediaPlayerService.mediaPlayerExists()) {
          height = height + 30;     // for select player folder option
        }
      }
    }
    if(this.mediaPlayerService.mediaPlayerExists() && this.isPlaylist()) {
      height = height + 30; // for select player playlist option
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
