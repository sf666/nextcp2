import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { DeviceDriverState, MediaRendererSwitchPower, MediaRendererSetVolume, MediaRendererDto, TrackInfoDto, TrackTimeDto, InputSourceDto, MusicItemDto, AudioFormat } from './dto.d';
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
  public inputSourceList: InputSourceDto;
  public deviceDriverState: DeviceDriverState = { standby: true, volume: 0, rendererUDN: '' }; // default display values

  constructor(
    sseService: SseService,
    private deviceService: DeviceService,
    private httpService: HttpService) {

    this.trackInfo = this.emptyTrackInfo();
    this.trackTime = this.emptyTrackTime();

    sseService.mediaRendererDeviceDriverStateChanged$.subscribe(data => this.updateRenderDeviceDriverState(data));

    sseService.mediaRendererTrackInfoChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.trackInfo = data;
        this.trackTime.durationDisp = data.duration;
      };
    });

    sseService.mediaRendererPositionChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        //        console.log(data);
        this.trackTime = data;
      }
    });

    this.deviceService.mediaRendererChanged$.subscribe(data => this.renderDeviceChanged(data));
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    this.updateDeviceDriverState(device);
    this.updateTrackInfoState(device);
  }

  private updateTrackInfoState(device: MediaRendererDto) {
    const uri = '/getCurrentSourceTrackInfo';
    this.httpService.post<TrackInfoDto>(this.baseUri, uri, device).subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.trackInfo = data
      }
    });
  }

  private updateDeviceDriverState(device: MediaRendererDto) {
    const uri = '/getDeviceState';
    this.httpService.post<DeviceDriverState>(this.baseUri, uri, device).subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.rendererUDN)) {
        this.deviceDriverState = data;
      }
    }
    );
  }

  private updateRenderDeviceDriverState(data: DeviceDriverState) {
    if (this.deviceService.isMediaRendererSelected(data.rendererUDN)) {
      console.log("new device driver state : " + data);
      this.deviceDriverState = data;
    }
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
  // Empty Datastructures
  // ================================================================================================================

  emptyTrackTime(): TrackTimeDto {
    return {
      duration: 0,
      durationDisp: '00:00',
      mediaRendererUdn: '',
      percent: 0,
      seconds: 0,
      secondsDisp: '00:00',
      trackCount: 0
    }
  }

  emptyTrackInfo(): TrackInfoDto {
    return {
      mediaRendererUdn: '',
      streaming: false,
      codecName: '',
      detailsCount: 0,
      metadata: '',
      metatext: '',
      metatextCount: 0,
      trackCount: 0,
      uri: '',
      duration: '',
      currentTrack: this.emptyMusicItem(),
    }
  }

  emptyMusicItem(): MusicItemDto {
    return {
      album: '',
      albumArtUrl: '',
      artistName: '',
      audioFormat: this.emptyAudioFormat(),
      creator: '',
      currentTrackMetadata: '',
      date: '',
      mediaServerUDN: '',
      numberOfThisDisc: '',
      objectClass: '',
      objectID: '',
      originalTrackNumber: '',
      parentId: '',
      rating: 0,
      refId: '',
      streamingURL: '',
      title: '',
    }
  }

  emptyAudioFormat(): AudioFormat {
    return {
      bitrate: 0,
      bitsPerSample: 0,
      filetype: '',
      nrAudioChannels: 2,
      sampleFrequency: 0
    }
  }
}
