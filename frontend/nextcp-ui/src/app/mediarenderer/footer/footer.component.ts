import { TrackDetailsData } from './../../popup/track-details/track-details-config.d';
import { TrackDetailsComponent } from './../../popup/track-details/track-details.component';
import { SongOptionsServiceService } from './../../mediaserver/popup/song-options/song-options-service.service';
import { AvailableServerComponent } from './../../popup/available-server/available-server.component';
import { VolumeControlComponent } from './../../popup/volume-control/volume-control.component';
import { MatDialog } from '@angular/material/dialog';
import { AvailableRendererComponent } from './../../popup/available-renderer/available-renderer.component';
import { PlaylistService } from './../../service/playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { UpnpAvTransportState } from './../../service/dto.d';
import { TransportService as TransportService } from '../../service/transport.service';
import { Component, ElementRef } from '@angular/core';

@Component({
  selector: 'renderer-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  currentMediaRendererName: string;

  constructor(
    private dialog: MatDialog,
    public transportService: TransportService,
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    public songOptionsServiceService: SongOptionsServiceService,
    public rendererService: RendererService) {
    deviceService.mediaRendererChanged$.subscribe(data => this.currentMediaRendererName = data.friendlyName);
  }

  public rendererClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(AvailableRendererComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }

  public serverClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(AvailableServerComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }

  public volumeClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(VolumeControlComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }


  public get avTransportState(): UpnpAvTransportState {
    return this.transportService.upnpAvTransportState;
  }

  public getImgSrc(): string {
    return this.rendererService.getImgSrc();
  }

  public getCurrentSongTitle(): string {
    return this.rendererService.getCurrentSongTitle();
  }

  //
  // Footer right : device audio and power control
  //

  hasDeviceDriver(): boolean {
    return this.rendererService.deviceDriverState?.hasDeviceDriver;
  }

  powerClicked(): void {
    this.rendererService.powerPressed();
  }

  public getStandbyClass(): string {
    if (this.rendererService.deviceDriverState.standby) {
      return "standbyOn";
    }
    else {
      return "standbyOff";
    }
  }

  volChanged(event): void {
    this.rendererService.setVolume(event);
  }

  streaming(): boolean {
    return this.rendererService.streaming();
  }

  getFinishTime(): string {
    return this.rendererService.getFinishTime();
  }

  trackTimePercent(): number {
    return this.streaming() ? 0 : this.rendererService.trackTime.percent;
  }

  //
  // styling of elements depending on state information
  // =========================================================================================================

  get infoSongClass(): string {
    if (this.rendererService.trackInfoAvailable) {
      return ""
    } else {
      return "disabled";
    }
  }

  get shuffleClass(): string {
    if (!this.rendererService.canShuffle()) {
      return "disabled";
    }
    if (this.rendererService.isShuffle()) {
      return "active"
    }
    return "";
  }

  get repeatClass(): string {
    if (!this.rendererService.canRepeat()) {
      return "disabled";
    }
    if (this.rendererService.isRepeat()) {
      return "active"
    }
    return "";
  }

  showSongPopup(event: MouseEvent): void {
    if (this.rendererService.trackInfo?.currentTrack?.streamingURL) {
      this.songOptionsServiceService.openOptionsDialog(event, this.rendererService.trackInfo.currentTrack, null);
    }
  }

  currentInputSourceVisible(): boolean {
    return this.currentInputSource() != "";
  }

  currentInputSource(): string {
    if (this.deviceService.selectedMediaRendererDevice?.currentSource?.Name) {
      return this.deviceService.selectedMediaRendererDevice?.currentSource.Name;
    }
    return "";
  }

  openInfoPopup(event: Event): void {
    if (this.rendererService.trackInfoAvailable) {
      let trackData: TrackDetailsData = {
        albumartUri: this.getImgSrc(),
        inputSource: this.currentInputSource(),
        currentTrack: this.rendererService.getCurrentTrack()
      }

      this.dialog.open(TrackDetailsComponent, {
        data: trackData,
        hasBackdrop: true
      });
    }
  }
}
