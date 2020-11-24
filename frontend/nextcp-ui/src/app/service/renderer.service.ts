import { DtoGeneratorService } from './../util/dto-generator.service';
import { BackgroundImageService } from './../util/background-image.service';
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
        this.trackInfo = data;
        if (data.currentTrack?.albumArtUrl) {
          this.backgroundImageService.setFooterBackgroundImage(data.currentTrack?.albumArtUrl);
        }
        if (data.duration) {
          this.trackTime.durationDisp = data.duration;
        }
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
}
