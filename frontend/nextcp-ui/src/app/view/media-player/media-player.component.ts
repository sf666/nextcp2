import { DeviceService } from './../../service/device.service';
import { MusicLibraryService } from './../../service/music-library/music-library.service';
import { Event } from './../../../../node_modules/@types/ws/node_modules/undici-types/patch.d';
import { MediaPlayerConfigDto, ContainerItemDto, ContainerIdDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { ChangeDetectionStrategy, Component, model, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule, MatLabel, MatSuffix } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MediaPlayerService } from 'src/app/service/media-player/media-player.service';

@Component({
  selector: 'media-player-config',
  standalone: true,
  templateUrl: './media-player.component.html',
  styleUrl: './media-player.component.scss',
  changeDetection: ChangeDetectionStrategy.Default,
  imports: [
    MatFormFieldModule,
    MatLabel,
    MatSelectModule,
    FormsModule,
    MatOptionModule,
    MatInputModule,
    MatSuffix,
    MatButtonModule,
    MatSlideToggleModule,
    ReactiveFormsModule]
})

export class MediaPlayerComponent {


  playMonitoring = signal<boolean>(false);

  mediaPlayerConfigDto : MediaPlayerConfigDto;

  constructor(
    private configurationService: ConfigurationService, 
    private mediaPlayerService: MediaPlayerService,
    private deviceService: DeviceService,
    public musicLibraryService: MusicLibraryService,
  ) {

    this.mediaPlayerConfigDto = this.configurationService.mediaPlayerConfigDto();
    this.getPlayStatus();
  }

  saveConfig() {
    this.logConfigDto();
    this.configurationService.saveMediaPlayerConfig(this.mediaPlayerConfigDto);
  }

  private logConfigDto(): void {
    console.log("received server config : " +
      "overwrite " + this.mediaPlayerConfigDto.overwrite +
      ". playtype : " + this.mediaPlayerConfigDto.playType +
      ". workdir : " + this.mediaPlayerConfigDto.workdir +
      ". Script : " + this.mediaPlayerConfigDto.script);
  }

  updatePlaylistRecording(event: Event): void {
    this.saveConfig();
  }

  updateMonitoringState(event: Event): void {
    console.log("monitoring set to " + event);
    if (event) {
      this.mediaPlayerService.startPlayScreening();
    } else {
      this.mediaPlayerService.stopPlayScreening();
    }
    this.getPlayStatus();
  }

  getPlayStatus(): void {
    this.mediaPlayerService.isPlayScreening().subscribe(status => this.playMonitoring.set(status));
  }

  createFolder(): void {
    this.mediaPlayerService.createFolder();
  }

  upload(): void {
    this.mediaPlayerService.upload();
  }

  selectFolder() {
    this.mediaPlayerConfigDto.addToFolderId = this.getCurrentContainerIdDto();
    this.saveConfig();
  }

  selectPlaylist() {
    this.mediaPlayerConfigDto.addToPlaylistId = this.getCurrentContainerIdDto();
    this.saveConfig();
  }

  selectMediaServer() {
    this.mediaPlayerConfigDto.mediaServerUdn = this.deviceService.selectedMediaServerDevice().udn;
    this.saveConfig();
  }

  public getCurrentContainerIdDto(): ContainerIdDto {
    return {
      id: this.musicLibraryService.currentMediaLibraryFolder().currentContainer.id,
      title: this.musicLibraryService.currentMediaLibraryFolder().currentContainer.title
    }
  }
}
