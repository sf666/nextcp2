import { DtoGeneratorService } from './../util/dto-generator.service';
import { BackgroundImageService } from './../util/background-image.service';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { DeviceDriverState, MediaRendererSwitchPower, MediaRendererSetVolume, MediaRendererDto, TrackInfoDto, TrackTimeDto, InputSourceDto, TransportServiceStateDto } from './dto.d';
import { SseService } from './sse/sse.service';
import { Injectable, computed, signal, inject } from '@angular/core';
import { GenericResultService } from './generic-result.service';
import { LocalPlayerService } from './local-player.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})

/**
 * This services connects to renderer device services.
 */
export class RendererService {
  private deviceService = inject(DeviceService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private backgroundImageService = inject(BackgroundImageService);
  private genericResultService = inject(GenericResultService);
  private httpService = inject(HttpService);
  private localPlayer = inject(LocalPlayerService);

  private baseUri = '/DeviceRendererService';

  // Raw state from the selected UPnP renderer (set from SSE / device reads).
  private trackInfoUpnp = signal<TrackInfoDto>(this.dtoGeneratorService.emptyTrackInfo());
  private trackTimeUpnp = signal<TrackTimeDto>(this.dtoGeneratorService.emptyTrackTime());
  private transportStateUpnp = signal<TransportServiceStateDto>(
    this.dtoGeneratorService.generateEmptyTransportServiceStateDto(),
  );
  inputSourceList = signal<InputSourceDto>(
    this.dtoGeneratorService.emptyInputSourceDto(),
  );
  deviceDriverState = signal<DeviceDriverState>(
    this.dtoGeneratorService.emptyDeviceDriverState(),
  );

  // Local browser player state, mapped to the same DTO shapes so the footer can render it unchanged.
  private localTrackInfo = computed<TrackInfoDto>(() => {
    const info = this.dtoGeneratorService.emptyTrackInfo();
    const item = this.localPlayer.currentItem();
    if (item) {
      info.currentTrack = item;
    }
    return info;
  });
  private localTrackTime = computed<TrackTimeDto>(() => {
    const time = this.dtoGeneratorService.emptyTrackTime();
    const cur = this.localPlayer.currentTime();
    const dur = this.localPlayer.duration();
    time.seconds = Math.floor(cur);
    time.secondsDisp = RendererService.formatTime(cur);
    time.duration = Math.floor(dur);
    time.durationDisp = dur > 0 ? RendererService.formatTime(dur) : '00:00';
    time.percent = dur > 0 ? Math.min(100, Math.round((cur / dur) * 100)) : 0;
    return time;
  });
  private localTransportState = computed<TransportServiceStateDto>(() => {
    const state = this.dtoGeneratorService.generateEmptyTransportServiceStateDto();
    state.transportState = this.localPlayer.playing() ? 'PLAYING' : 'PAUSED';
    state.canPause = true;
    state.canSeek = true;
    state.canRepeat = true;
    state.repeat = this.localPlayer.repeat();
    state.canShuffle = true;
    state.shuffle = this.localPlayer.shuffle();
    return state;
  });

  // Public state: the local browser player when "This Device" is selected, else the UPnP renderer.
  trackInfo = computed<TrackInfoDto>(() =>
    this.deviceService.isLocalBrowserSelected() ? this.localTrackInfo() : this.trackInfoUpnp(),
  );
  trackTime = computed<TrackTimeDto>(() =>
    this.deviceService.isLocalBrowserSelected() ? this.localTrackTime() : this.trackTimeUpnp(),
  );
  transportServiceStateDto = computed<TransportServiceStateDto>(() =>
    this.deviceService.isLocalBrowserSelected() ? this.localTransportState() : this.transportStateUpnp(),
  );

  trackInfoAvailable = computed(
    () => this.trackInfo().currentTrack?.objectID?.length > 0,
  );
  isPlaying = computed(
    () =>
      this.transportServiceStateDto().transportState.toUpperCase() ===
      'PLAYING',
  );
  isShuffle = computed(() => this.transportServiceStateDto().shuffle);
  isRepeat = computed(() => this.transportServiceStateDto().repeat);
  canShuffle = computed(() => this.transportServiceStateDto().canShuffle);
  canPause = computed(() => this.transportServiceStateDto().canPause);
  canRepeat = computed(() => this.transportServiceStateDto().canRepeat);
  canSeek = computed(() => this.transportServiceStateDto().canSeek);
  canSkipNext = computed(() => this.transportServiceStateDto().canSkipNext);
  canSkipPrevious = computed(
    () => this.transportServiceStateDto().canSkipPrevious,
  );

  isStreaming = computed(() => this.trackTime().streaming);

  currentTrack = computed(() => this.trackInfo().currentTrack);
  canCurrentTrackBeAddedToPlaylist = computed(
    () => this.trackInfo().currentTrack?.songId?.objectID?.length > 0,
  );
  bitsPerSample = computed(
    () => this.trackInfo().currentTrack?.audioFormat?.bitsPerSample,
  );
  sampleFreq = computed(
    () => this.trackInfo().currentTrack?.audioFormat?.sampleFrequency,
  );
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
    if (bps >= 16 && sFreq >= 44100) {
      // CD Quality
      return true;
    }
    return false;
  });

  finishTime = computed(() => {
    if (this.trackTime().durationDisp) {
      return this.trackTime().durationDisp;
    } else {
      return '00:00';
    }
  });

  hifiString = computed(() => {
    let bps = this.bitsPerSample();
    let sFreq = this.sampleFreq();
    if (!this.isHifi) {
      return 'low';
    } else if (bps == 16 && sFreq == 44100) {
      return 'CD';
    } else if (bps > 16 && sFreq == 44100) {
      return 'HIFI';
    } else if (bps >= 24 && sFreq > 44100) {
      return 'Hi-Res';
    }
  });

  constructor() {
    const sseService = inject(SseService);
    const deviceService = this.deviceService;

    sseService.mediaRendererDeviceDriverStateChanged$.subscribe((data) =>
      this.updateRenderDeviceDriverState(data),
    );

    sseService.mediaRendererTrackInfoChanged$.subscribe((data) => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        if (
          this.trackInfoUpnp().currentTrack?.albumArtUrl !=
          data.currentTrack?.albumArtUrl
        ) {
          // update background images
          console.log('updating background images');
          this.backgroundImageService.setFooterBackgroundImage(
            data.currentTrack?.albumArtUrl,
          );
          this.backgroundImageService.setBackgroundImageMainScreen(
            data.currentTrack?.albumArtUrl,
          );
        }
        this.trackInfoUpnp.set(data);
      }
    });

    sseService.mediaRendererPositionChanged$.subscribe((data) => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.trackTimeUpnp.set(data);
      }
    });

    sseService.mediaRendererTransportStateChanged$.subscribe((data) =>
      this.updateTransportState(data),
    );
    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(
      (data) => this.renderDeviceChanged(data),
    );
  }

  private updateTransportState(state: TransportServiceStateDto) {
    if (state.udn == this.deviceService.selectedMediaRendererDevice().udn) {
      console.log('new transport state : ' + state.transportState);
      this.transportStateUpnp.set(state);
    }
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    console.log('renderDeviceChanged to : ' + device.friendlyName);
    // The synthetic "This Device" renderer has no UPnP device driver / transport state on the backend.
    if (device.udn === this.deviceService.LOCAL_BROWSER_UDN) {
      return;
    }
    this.readDeviceDriverState(device);
    this.readTrackInfoState(device);
    this.readTransportServiceState(device);
  }

  private readTrackInfoState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getCurrentSourceTrackInfo';
      this.httpService
        .post<TrackInfoDto>(this.baseUri, uri, device)
        .subscribe((data) => {
          if (
            data &&
            this.deviceService.isMediaRendererSelected(data.mediaRendererUdn)
          ) {
            this.trackInfoUpnp.set(data);
          }
        });
    }
  }

  private readTransportServiceState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getDeviceTransportServiceState';
      this.httpService
        .post<TransportServiceStateDto>(this.baseUri, uri, device)
        .subscribe((data) => {
          if (this.deviceService.isMediaRendererSelected(data?.udn)) {
            this.transportStateUpnp.set(data);
            console.log('readTransportServiceState shuffle : ' + data.shuffle);
            console.log('readTransportServiceState : ' + data);
          }
        });
    }
  }

  private readDeviceDriverState(device: MediaRendererDto) {
    if (device?.udn.length > 0) {
      const uri = '/getDeviceState';
      this.httpService
        .post<DeviceDriverState>(this.baseUri, uri, device)
        .subscribe((data) => {
          if (this.deviceService.isMediaRendererSelected(data?.rendererUDN)) {
            console.log(
              'updated device driver state for ' +
                data.rendererUDN +
                ' to ' +
                data.hasDeviceDriver,
            );
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
      standby: !this.deviceDriverState().standby,
    };
    this.httpService.post(this.baseUri, uri, request, 'power switch');
  }

  /**
   * Set's volume in percent
   */
  public setVolume(vol: number) {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.setVolume(vol);
      return;
    }
    if (this.deviceService.selectedMediaRendererDevice().udn) {
      const uri = '/setVolume';
      let request: MediaRendererSetVolume = {
        rendererUDN: this.deviceService.selectedMediaRendererDevice().udn,
        volume: vol,
      };
      this.httpService.post(this.baseUri, uri, request, 'volume control');
    } else {
      this.genericResultService.displayGenericMessage(
        'volume',
        'Cannot change volume. No media renderer selected.',
      );
    }
  }

  //
  // Renderer transport services for selected renderer
  // ================================================================================================================
  public pause() {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.pause();
      return;
    }
    const uri = '/pause';
    this.httpService.post(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaRendererDevice().udn,
      'pause',
    );
  }

  public stop() {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.stop();
      return;
    }
    const uri = '/stop';
    this.httpService.post(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaRendererDevice().udn,
      'stop',
    );
  }

  public play() {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.resume();
      return;
    }
    const uri = '/play';
    this.httpService.post(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaRendererDevice().udn,
      'play',
    );
  }

  public next() {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.next();
      return;
    }
    const uri = '/next';
    this.httpService.post(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaRendererDevice().udn,
      'next',
    );
  }

  // Maintenance methods
  public initServices(udn: string) {
    const uri = '/initServices';
    this.httpService.post(this.baseUri, uri, udn, 'init services');
  }

  /** Formats a number of seconds as mm:ss (or h:mm:ss for durations of an hour or more). */
  private static formatTime(totalSeconds: number): string {
    if (!totalSeconds || isNaN(totalSeconds) || totalSeconds < 0) {
      return '00:00';
    }
    const s = Math.floor(totalSeconds);
    const hrs = Math.floor(s / 3600);
    const mins = Math.floor((s % 3600) / 60);
    const secs = s % 60;
    const mm = String(mins).padStart(2, '0');
    const ss = String(secs).padStart(2, '0');
    return hrs > 0 ? String(hrs) + ':' + mm + ':' + ss : mm + ':' + ss;
  }
}
