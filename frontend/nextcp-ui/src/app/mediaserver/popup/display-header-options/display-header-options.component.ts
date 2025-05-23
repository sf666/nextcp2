import { ChangeDetectionStrategy, Component, ElementRef, Inject, Output, OutputEmitterRef, ViewContainerRef, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ConfigurationService } from 'src/app/service/configuration.service';
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
    if (this.isFolder()) {
      this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 180);
    } else if (this.isPlaylist()) {
      // Playlist 
      this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 150);
    } else {
      console.log("ERROR: unknown type");
      this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 150);
    }
  }

  addToPlaylist(): void {
    this.addToPlaylistOutput.emit(this.currentContainer);
    this.close();
  }

  selectPlayerFolder(): void {
    this.mediaPlayerConfigDto.addToFolderId = this.getCurrentContainerIdDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
    this.close();
  }

  selectPlayerFolderSidebar(): void {
    this.configurationService.applicationConfig.myPlaylistFolderName = this.getCurrentContainerIdDto().id;
    this.configurationService.saveApplicationConfig();
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
