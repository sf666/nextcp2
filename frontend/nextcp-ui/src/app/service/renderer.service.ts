import { DtoGeneratorService } from './../util/dto-generator.service';
import { BackgroundImageService } from './../util/background-image.service';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { DeviceDriverState, MediaRendererSwitchPower, MediaRendererSetVolume, MediaRendererDto, TrackInfoDto, TrackTimeDto, InputSourceDto, MusicItemDto, AudioFormat, InputSourceChangeDto, TransportServiceStateDto } from './dto.d';
import { SseService } from './sse/sse.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

/**
 * This services connects to renderer device services.
 */
export class RendererService {

  baseUri = '/DeviceRendererService';

  public trackInfo: TrackInfoDto;
  public trackTime: TrackTimeDto;
  public transportServiceStateDto: TransportServiceStateDto;
  public inputSourceList: InputSourceDto;
  public deviceDriverState: DeviceDriverState = { hasDeviceDriver: false, standby: true, volume: 0, rendererUDN: '' };

  constructor(
    sseService: SseService,
    private deviceService: DeviceService,
    private dtoGeneratorService: DtoGeneratorService,
    private backgroundImageService: BackgroundImageService,
    private httpService: HttpService) {

    this.trackInfo = this.dtoGeneratorService.emptyTrackInfo();
    this.trackTime = this.dtoGeneratorService.emptyTrackTime();

    sseService.mediaRendererDeviceDriverStateChanged$.subscribe(data => this.updateRenderDeviceDriverState(data));
  
    sseService.mediaRendererTrackInfoChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        // console.log("updating trackInfo : " + data.currentTrack?.artistName);
        this.trackInfo = data;
        if (data.currentTrack?.albumArtUrl) {
          this.backgroundImageService.setFooterBackgroundImage(data.currentTrack?.albumArtUrl);
        }
        if (data.duration) {
//          this.trackTime.durationDisp = data.duration;
        }
      }
    });

    sseService.mediaRendererPositionChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        //        console.log(data);
        this.trackTime = data;
      }
    });

    sseService.mediaRendererTransportStateChanged$.subscribe(data => this.updateTransportState(data));
    this.deviceService.mediaRendererChanged$.subscribe(data => this.renderDeviceChanged(data));
  }

  private updateTransportState(state: TransportServiceStateDto) {
    if (state.udn == this.deviceService.selectedMediaRendererDevice.udn) {
      console.log("new transport state : " + state.transportState);
      this.transportServiceStateDto = state;
    }
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    this.readDeviceDriverState(device);
    this.readTrackInfoState(device);
    this.readTransportServiceState(device)
  }

  private readTrackInfoState(device: MediaRendererDto) {
    const uri = '/getCurrentSourceTrackInfo';
    this.httpService.post<TrackInfoDto>(this.baseUri, uri, device).subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.trackInfo = data
      }
    });
  }

  private readTransportServiceState(device: MediaRendererDto) {
    const uri = '/getDeviceTransportServiceState';
    this.httpService.post<TransportServiceStateDto>(this.baseUri, uri, device).subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.udn)) {
        this.transportServiceStateDto = data
      }
    });
  }

  private readDeviceDriverState(device: MediaRendererDto) {
    const uri = '/getDeviceState';
    this.httpService.post<DeviceDriverState>(this.baseUri, uri, device).subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.rendererUDN)) {
        console.log("updated device driver state for " + data.rendererUDN + " to " + data.hasDeviceDriver);
        this.deviceDriverState = data;
      }
    });
  }

  private updateRenderDeviceDriverState(data: DeviceDriverState) {
    if (this.deviceService.isMediaRendererSelected(data.rendererUDN)) {
      this.deviceDriverState = data;
    }
  }

  public get trackInfoAvailable(): boolean {
    return this.trackInfo?.currentTrack?.title?.length > 0;
  }
  
  public isPlaying(): boolean {
    const playing: boolean = this.transportServiceStateDto?.transportState.toUpperCase() === 'PLAYING';
    return playing;      
  }

  //
  // Renderer actions
  // ================================================================================================================

  /**
   * power button pressed
   */
  public powerPressed() {
    const newPowerState = !this.deviceDriverState.standby;

    const uri = '/setStandby';
    let request: MediaRendererSwitchPower = {
      rendererUDN: this.deviceService.selectedMediaRendererDevice.udn,
      standby: !this.deviceDriverState.standby
    };
    this.httpService.post(this.baseUri, uri, request, "power switch");
  }

  /**
   * Set's volume in percent 
   */
  public setVolume(vol: number) {
    const uri = '/setVolume';
    let request: MediaRendererSetVolume = {
      rendererUDN: this.deviceService.selectedMediaRendererDevice.udn,
      volume: vol
    };
    this.httpService.post(this.baseUri, uri, request, "volume control");
  }

  //
  // Renderer transport services
  // ================================================================================================================
  public pause() {
    const uri = '/pause';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice.udn, "pause");
  }

  public stop() {
    const uri = '/stop';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice.udn, "stop");
  }

  public play() {
    const uri = '/play';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice.udn, "play");
  }

  public next() {
    const uri = '/next';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice.udn, "next");
  }


  //
  // Renderer : information about the current played song
  // ================================================================================================================

  streaming() {
    let streaming: boolean;
    streaming = this.trackTime?.streaming;
    return streaming;
  }

  getFinishTime(): string {
    if (this.trackTime?.durationDisp) {
      return this.trackTime.durationDisp;
    } else {
      return "00:00";
    }
  }

  public getImgSrc(): string {
    if (this.trackInfo?.currentTrack?.albumArtUrl) {
      return this.trackInfo?.currentTrack?.albumArtUrl;
    }
    else {
      return "";
    }
  }

  public isHifi() : boolean {
    let bps = this.getBitsPerSample();
    let sFreq = this.getSampleFreq();
    if (bps >= 16 && sFreq>= 44100) { // CD Quality
      return true;
    }
    return false;
  }

  public getHifiString() : string {
    let bps = this.getBitsPerSample();
    let sFreq = this.getSampleFreq();
    if (!this.isHifi) {
      return "low";
    } else if (bps == 16 && sFreq == 44100) {
      return "CD"
    } else if (bps > 16 && sFreq == 44100) {
      return "HIFI"
    } else if (bps >= 24 && sFreq > 44100) {
      return "Hi-Res"
    }
  }

  public getBitrate(): number {
    if (this.trackInfo?.currentTrack?.audioFormat?.bitrate) {
      return this.trackInfo?.currentTrack?.audioFormat?.bitrate / 125
    } else {
      return 0;
    }
  }

  public getBitsPerSample(): number {
    if (this.trackInfo?.currentTrack?.audioFormat?.bitsPerSample) {
      return this.trackInfo?.currentTrack?.audioFormat?.bitsPerSample
    } else {
      return 0;
    }
  }

  public getSampleFreq(): number {
    if (this.trackInfo?.currentTrack?.audioFormat?.sampleFrequency) {
      return this.trackInfo?.currentTrack?.audioFormat?.sampleFrequency / 1000
    } else {
      return 0;
    }
  }

  public getCurrentSongTitle(): string {
    if (this.trackInfoAvailable) {
      return this.trackInfo?.currentTrack?.title;
    }
    else {
      return "no track info available";
    }
  }
}
