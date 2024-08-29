import { ChangeDetectionStrategy, Component, ElementRef, Inject, Output, OutputEmitterRef, ViewContainerRef, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { ContainerDto, ContainerIdDto, MediaPlayerConfigDto } from 'src/app/service/dto';
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
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 150);
  }

  addToPlaylist(): void {
    this.addToPlaylistOutput.emit(this.currentContainer);
    this.close();
  }

  selectPlayerFolder(): void {
    this.mediaPlayerConfigDto.addToFolderId = this.getCurrentContainerIdDto();
    this.saveConfig();
    this.close();
  }

  selectPlayerPlaylist(): void {
    this.mediaPlayerConfigDto.addToPlaylistId = this.getCurrentContainerIdDto();
    this.saveConfig();
    this.close();
  }

  isPlaylist(): boolean {
    return this.currentContainer.objectClass.startsWith('object.container.playlistContainer');
  }

  isFolder(): boolean {
    return this.currentContainer.objectClass.startsWith('object.container.storageFolder');
  }

  public getCurrentContainerIdDto(): ContainerIdDto {
    return {
      id: this.currentContainer.id,
      title: this.currentContainer.title
    }
  }  

  private saveConfig() {
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
  }

  private close() : void {
    this._matDialogRef.close(); // no return result ...   
  }
}
