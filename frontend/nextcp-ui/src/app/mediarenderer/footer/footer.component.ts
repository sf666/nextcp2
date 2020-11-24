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
    if (this.currentMediaRendererName) {
      return this.currentMediaRendererName;
    }
    else {
      "select media renderer";
    }
  }

  public get avTransportState(): UpnpAvTransportState {
    return this.avtransportService.upnpAvTransportState;
  }

  public getBitrate(): number {
    if (this.rendererService?.trackInfo?.currentTrack?.audioFormat?.bitrate) {
      return this.rendererService?.trackInfo?.currentTrack?.audioFormat?.bitrate / 125
    } else {
      return 0;
    }
  }

  public getBitsPerSample(): number {
    if (this.rendererService?.trackInfo?.currentTrack?.audioFormat?.bitsPerSample) {
      return this.rendererService?.trackInfo?.currentTrack?.audioFormat?.bitsPerSample
    } else {
      return 0;
    }
  }

  public getSampleFreq(): number {
    if (this.rendererService?.trackInfo?.currentTrack?.audioFormat?.sampleFrequency) {
      return this.rendererService?.trackInfo?.currentTrack?.audioFormat?.sampleFrequency / 1000
    } else {
      return 0;
    }
  }

  public getImgSrc(): string {
    if (this.rendererService.trackInfo?.currentTrack?.albumArtUrl) {
      return this.rendererService.trackInfo?.currentTrack?.albumArtUrl;
    }
    else {
      return "";
    }
  }
  
  public getCurrentSongTitle(): string {
    if (this.rendererService.trackInfoAvailable) {
      return this.rendererService.trackInfo?.currentTrack?.title;
    }
    else {
      return "no track info available";
    }
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
    let streaming: boolean;
    streaming = this.rendererService?.trackTime?.streaming;
    return streaming;
  }

  getFinishTime(): string {
    if (this.rendererService.trackTime?.durationDisp) {
      return this.rendererService.trackTime.durationDisp;
    } else {
      return "00:00";
    }
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
