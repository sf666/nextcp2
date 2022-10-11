import { SongOptionsServiceService } from './../../mediaserver/popup/song-options/song-options-service.service';
import { AvailableServerComponent } from './../../popup/available-server/available-server.component';
import { VolumeControlComponent } from './../../popup/volume-control/volume-control.component';
import { MatDialog } from '@angular/material/dialog';
import { AvailableRendererComponent } from './../../popup/available-renderer/available-renderer.component';
import { PlaylistService } from './../../service/playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { UpnpAvTransportState } from './../../service/dto.d';
import { AvtransportService } from './../../service/avtransport.service';
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
    public avtransportService: AvtransportService,
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
    return this.avtransportService.upnpAvTransportState;
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

  //
  // for demontration purpose : locally provided actions intended to be used by the template
  // =========================================================================================================

  streaming(): boolean {
    return this.rendererService.streaming();
  }

  getFinishTime(): string {
    return this.rendererService.getFinishTime();
  }

  //
  // styling of elements depending on state information
  // =========================================================================================================

  get shuffleClass(): string {
    if (this.playlistService.playlistState.Shuffle) {
      return "active"
    }
    return "";
  }

  get repeatClass(): string {
    if (this.playlistService.playlistState.Repeat) {
      return "active"
    }
    return "";
  }

  showSongPopup(event: MouseEvent): void {
    if (this.rendererService.trackInfo?.currentTrack?.streamingURL) {
      this.songOptionsServiceService.openOptionsDialog(event, this.rendererService.trackInfo.currentTrack, null);
    }
  }

  currentInputSource() : string {
    console.log(this.deviceService.selectedMediaRendererDevice);
    if (this.deviceService.selectedMediaRendererDevice?.currentSource?.Name) {
      return this.deviceService.selectedMediaRendererDevice?.currentSource.Name;
    }
    console.log(this.deviceService.selectedMediaRendererDevice.currentSource);
    console.log(this.deviceService.selectedMediaRendererDevice.currentSource.Name);
    return "";
  }
}
