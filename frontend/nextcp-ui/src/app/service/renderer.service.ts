import { DtoGeneratorService } from './../util/dto-generator.service';
import { BackgroundImageService } from './../util/background-image.service';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { DeviceDriverState, MediaRendererSwitchPower, MediaRendererSetVolume, MediaRendererDto, TrackInfoDto, TrackTimeDto, InputSourceDto, MusicItemDto, AudioFormat, InputSourceChangeDto, TransportServiceStateDto } from './dto.d';
import { SseService } from './sse/sse.service';
import { Injectable, computed, signal } from '@angular/core';
import { GenericResultService } from './generic-result.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root'
})

/**
 * This services connects to renderer device services.
 */
export class RendererService {

  private baseUri = '/DeviceRendererService';

  trackInfo = signal<TrackInfoDto>(this.dtoGeneratorService.emptyTrackInfo());
  trackTime = signal<TrackTimeDto>(this.dtoGeneratorService.emptyTrackTime());
  transportServiceStateDto = signal<TransportServiceStateDto>(this.dtoGeneratorService.generateEmptyTransportServiceStateDto());
  inputSourceList = signal<InputSourceDto>(this.dtoGeneratorService.emptyInputSourceDto());
  deviceDriverState = signal<DeviceDriverState>(this.dtoGeneratorService.emptyDeviceDriverState());

  trackInfoAvailable = computed(() => this.trackInfo().currentTrack?.objectID?.length > 0);
  isPlaying = computed(() => this.transportServiceStateDto().transportState.toUpperCase() === 'PLAYING');
  isShuffle = computed(() => this.transportServiceStateDto().shuffle);
  isRepeat = computed(() => this.transportServiceStateDto().repeat);
  canShuffle = computed(() => this.transportServiceStateDto().canShuffle);
  canPause = computed(() => this.transportServiceStateDto().canPause);
  canRepeat = computed(() => this.transportServiceStateDto().canRepeat);
  canSeek = computed(() => this.transportServiceStateDto().canSeek);
  canSkipNext = computed(() => this.transportServiceStateDto().canSkipNext);
  canSkipPrevious = computed(() => this.transportServiceStateDto().canSkipPrevious);

  isStreaming = computed(() => this.trackTime().streaming);

  currentTrack = computed(() => this.trackInfo().currentTrack);
  canCurrentTrackBeAddedToPlaylist = computed(() => this.trackInfo().currentTrack?.songId?.objectID?.length > 0);
  bitsPerSample = computed(() => this.trackInfo().currentTrack?.audioFormat?.bitsPerSample);
  sampleFreq = computed(() => this.trackInfo().currentTrack?.audioFormat?.sampleFrequency);
  currentSongTitle = computed(() => this.trackInfo().currentTrack?.title);
  bitrate = computed(() => this.trackInfo().currentTrack?.audioFormat?.bitrate);
  imgSrc = computed(() => {
    if (this.trackInfo().currentTrack?.albumArtUrl) {
      return this.trackInfo().currentTrack?.albumArtUrl;
    } else {
      return '/assets/images/folder-bg.png';
    }
  });
  isHifi = computed(() => {
    let bps = this.bitsPerSample();
    let sFreq = this.sampleFreq();
    if (bps >= 16 && sFreq >= 44100) { // CD Quality
      return true;
    }
    return false;
  });

  finishTime = computed(() => {
    if (this.trackTime().durationDisp) {
      return this.trackTime().durationDisp;
    } else {
      return "00:00";
    }
  });


  hifiString = computed(() => {
    let bps = this.bitsPerSample();
    let sFreq = this.sampleFreq();
    if (!this.isHifi) {
      return "low";
    } else if (bps == 16 && sFreq == 44100) {
      return "CD"
    } else if (bps > 16 && sFreq == 44100) {
      return "HIFI"
    } else if (bps >= 24 && sFreq > 44100) {
      return "Hi-Res"
    }
  });


  constructor(
    sseService: SseService,
    private deviceService: DeviceService,
    private dtoGeneratorService: DtoGeneratorService,
    private backgroundImageService: BackgroundImageService,
    private genericResultService: GenericResultService,
    private httpService: HttpService) {

    sseService.mediaRendererDeviceDriverStateChanged$.subscribe(data => this.updateRenderDeviceDriverState(data));

    sseService.mediaRendererTrackInfoChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        if (this.trackInfo().currentTrack?.albumArtUrl != data.currentTrack?.albumArtUrl) {
          // update background images
          console.log("updating background images");
          this.backgroundImageService.setFooterBackgroundImage(data.currentTrack?.albumArtUrl);
          this.backgroundImageService.setBackgroundImageMainScreen(data.currentTrack?.albumArtUrl);
        }
        this.trackInfo.set(data);
      }
    });

    sseService.mediaRendererPositionChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.trackTime.set(data);
      }
    });

    sseService.mediaRendererTransportStateChanged$.subscribe(data => this.updateTransportState(data));
    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(data => this.renderDeviceChanged(data));
  }

  private updateTransportState(state: TransportServiceStateDto) {
    if (state.udn == this.deviceService.selectedMediaRendererDevice().udn) {
      console.log("new transport state : " + state.transportState);
      this.transportServiceStateDto.set(state);
    }
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    console.log("renderDeviceChanged to : " + device.friendlyName);
    this.readDeviceDriverState(device);
    this.readTrackInfoState(device);
    this.readTransportServiceState(device)
  }

  private readTrackInfoState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getCurrentSourceTrackInfo';
      this.httpService.post<TrackInfoDto>(this.baseUri, uri, device).subscribe(data => {
        if (data && this.deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
          this.trackInfo.set(data);
        }
      });
    }
  }

  private readTransportServiceState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getDeviceTransportServiceState';
      this.httpService.post<TransportServiceStateDto>(this.baseUri, uri, device).subscribe(data => {
        if (this.deviceService.isMediaRendererSelected(data?.udn)) {
          this.transportServiceStateDto.set(data);
          console.log("readTransportServiceState shuffle : " + data.shuffle);
          console.log("readTransportServiceState : " + data);
        }
      });
    }
  }

  private readDeviceDriverState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getDeviceState';
      this.httpService.post<DeviceDriverState>(this.baseUri, uri, device).subscribe(data => {
        if (this.deviceService.isMediaRendererSelected(data?.rendererUDN)) {
          console.log("updated device driver state for " + data.rendererUDN + " to " + data.hasDeviceDriver);
          this.deviceDriverState.set(data);
        }
      });
    }
  }

  private updateRenderDeviceDriverState(data: DeviceDriverState) {
    if (this.deviceService.isMediaRendererSelected(data?.rendererUDN)) {
      this.deviceDriverState.set(data);
    }
  }

  //
  // Renderer actions
  // ================================================================================================================

  /**
   * power button pressed
   */
  public powerPressed() {
    const newPowerState = !this.deviceDriverState().standby;

    const uri = '/setStandby';
    let request: MediaRendererSwitchPower = {
      rendererUDN: this.deviceService.selectedMediaRendererDevice().udn,
      standby: !this.deviceDriverState().standby
    };
    this.httpService.post(this.baseUri, uri, request, "power switch");
  }

  /**
   * Set's volume in percent 
   */
  public setVolume(vol: number) {
    if (this.deviceService.selectedMediaRendererDevice().udn) {
      const uri = '/setVolume';
      let request: MediaRendererSetVolume = {
        rendererUDN: this.deviceService.selectedMediaRendererDevice().udn,
        volume: vol
      };
      this.httpService.post(this.baseUri, uri, request, "volume control");
    } else {
      this.genericResultService.displayGenericMessage("volume", "Cannot change volume. No media renderer selected.");
    }
  }

  //
  // Renderer transport services for selected renderer
  // ================================================================================================================
  public pause() {
    const uri = '/pause';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice().udn, "pause");
  }

  public stop() {
    const uri = '/stop';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice().udn, "stop");
  }

  public play() {
    const uri = '/play';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice().udn, "play");
  }

  public next() {
    const uri = '/next';
    this.httpService.post(this.baseUri, uri, this.deviceService.selectedMediaRendererDevice().udn, "next");
  }

  // Maintenance methods
  public initServices(udn: string) {
    const uri = '/initServices';
    this.httpService.post(this.baseUri, uri, udn, "init services");
  }

}
