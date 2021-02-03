import { MatSliderChange } from '@angular/material/slider';
import { PlaylistService } from './../../service/playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { UpnpAvTransportState } from './../../service/dto.d';
import { AvtransportService } from './../../service/avtransport.service';
import { Component } from '@angular/core';

@Component({
  selector: 'renderer-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  private baseURI = 'DeviceRendererService';

  currentMediaRendererName: string;

  constructor(
    public avtransportService: AvtransportService,
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    public rendererService: RendererService) {
    deviceService.mediaRendererChanged$.subscribe(data => this.currentMediaRendererName = data.friendlyName);
  }

  public getCurrentMediaRendererName() {
    if (this.currentMediaRendererName?.length > 0) {
      return this.currentMediaRendererName;
    }
    else {
      return "select media renderer";
    }
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

  powerClicked() {
    this.rendererService.powerPressed();
  }

  volChanged(event: MatSliderChange) {
    this.rendererService.setVolume(event.value);
  }

  public getStandbyClass() {
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

  streaming() {
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
}
