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


  constructor(
    public avtransportService: AvtransportService,
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    public rendererService: RendererService
  ) {
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
    if (this.rendererService.trackInfo?.currentTrack?.title) {
      return this.rendererService.trackInfo?.currentTrack?.title;
    }
    else {
      return "no track info available";
    }
  }


  //
  // for demontration purpose : locally provided actions intended to be used by the template
  // =========================================================================================================

  streaming() {
    return this.rendererService.trackInfo.streaming;
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
