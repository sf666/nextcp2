import { DtoGeneratorService } from './../util/dto-generator.service';
import { BackgroundImageService } from './../util/background-image.service';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { DeviceDriverState, MediaRendererSwitchPower, MediaRendererSetVolume, MediaRendererDto, TrackInfoDto, TrackTimeDto, InputSourceDto, TransportServiceStateDto } from './dto.d';
import { SseService } from './sse/sse.service';
import { Injectable, computed, effect, signal, inject } from '@angular/core';
import { GenericResultService } from './generic-result.service';
import { LocalPlayerService } from './local-player.service';
import { TimeDisplayService } from './../util/time-display.service';
import { AppVisibilityService } from './app-visibility/app-visibility-service.service';
import { toObservable } from '@angular/core/rxjs-interop';

/**
 * Last playback position reported by the renderer, plus the wall clock at which it arrived. The
 * displayed position is derived from it (see RendererService.interpolate) instead of being shown
 * as-is, so it advances every second even though a plain AVTransport renderer is only polled every
 * five seconds.
 */
interface PositionAnchor {
  udn: string;
  seconds: number;
  duration: number;
  streaming: boolean;
  trackCount: number;
  atMs: number;
}

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
  private timeDisplayService = inject(TimeDisplayService);
  private appVisibilityService = inject(AppVisibilityService);

  private baseUri = '/DeviceRendererService';

  // Raw state from the selected UPnP renderer (set from SSE / device reads).
  private trackInfoUpnp = signal<TrackInfoDto>(this.dtoGeneratorService.emptyTrackInfo());
  // Position of the selected UPnP renderer: the last reported sample plus a 1 Hz clock it is
  // extrapolated against. A plain AVTransport renderer is polled only every 5s by the backend
  // (MediaRendererDevice.tick), which made the footer time and progress bar jump in 5s steps;
  // OpenHome renderers push ~1/s via GENA and simply re-anchor on every event.
  private positionAnchor = signal<PositionAnchor>({
    udn: '',
    seconds: 0,
    duration: 0,
    streaming: false,
    trackCount: 0,
    atMs: 0,
  });
  private nowMs = signal<number>(Date.now());
  // Stop extrapolating when no sample has arrived for this long: the SSE stream may have died, or the
  // renderer may have stopped without ever sending a transport state change. Better a frozen display
  // than one that keeps counting into nowhere.
  private static readonly MAX_EXTRAPOLATION_MS = 30000;
  private transportStateUpnp = signal<TransportServiceStateDto>(
    this.dtoGeneratorService.generateEmptyTransportServiceStateDto(),
  );
  inputSourceList = signal<InputSourceDto>(
    this.dtoGeneratorService.emptyInputSourceDto(),
  );
  deviceDriverState = signal<DeviceDriverState>(
    this.dtoGeneratorService.emptyDeviceDriverState(),
  );

  /**
   * Position the given anchor implies at the given wall clock. Pure and idempotent, so a late or
   * throttled clock tick self-corrects instead of accumulating drift.
   */
  private static interpolate(anchor: PositionAnchor, nowMs: number): number {
    const elapsedMs = Math.max(0, nowMs - anchor.atMs);
    const elapsed =
      elapsedMs > RendererService.MAX_EXTRAPOLATION_MS ? 0 : Math.floor(elapsedMs / 1000);
    const seconds = anchor.seconds + elapsed;
    // Never run past the end of the track; a stream (duration 0) just keeps counting up.
    return anchor.duration > 0 ? Math.min(seconds, anchor.duration) : seconds;
  }

  private trackTimeUpnp = computed<TrackTimeDto>(() => {
    const anchor = this.positionAnchor();
    const seconds = RendererService.interpolate(anchor, this.nowMs());
    return {
      mediaRendererUdn: anchor.udn,
      duration: anchor.duration,
      // Mirrors the backend, which replaces the length of a continuous stream with this literal.
      durationDisp: anchor.streaming
        ? seconds > 0
          ? 'streaming'
          : '00:00'
        : this.timeDisplayService.convertLongToDateStringShort(anchor.duration),
      seconds,
      secondsDisp: this.timeDisplayService.convertLongToDateStringShort(seconds),
      trackCount: anchor.trackCount,
      percent:
        anchor.duration > 0 ? Math.min(100, Math.floor((seconds * 100) / anchor.duration)) : 0,
      streaming: anchor.streaming,
    };
  });

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
    time.secondsDisp = this.timeDisplayService.convertLongToDateStringShort(cur);
    time.duration = Math.floor(dur);
    time.durationDisp = dur > 0 ? this.timeDisplayService.convertLongToDateStringShort(dur) : '00:00';
    time.percent = dur > 0 ? Math.min(100, Math.floor((cur / dur) * 100)) : 0;
    // A live web radio has no position to seek to; without this the footer would offer a seek slider
    // for it, because "streaming" defaults to false on the empty DTO.
    time.streaming = this.localPlayer.live();
    return time;
  });
  private localTransportState = computed<TransportServiceStateDto>(() => {
    const state = this.dtoGeneratorService.generateEmptyTransportServiceStateDto();
    state.transportState = this.localPlayer.playing() ? 'PLAYING' : 'PAUSED';
    state.canPause = true;
    state.canSeek = !this.localPlayer.live();
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
      return '/assets/images/folder-bg.webp';
    }
  });
  /**
   * Artwork for the one place that shows the cover big: the now playing view. Browse grids and the
   * footer thumbnail keep imgSrc, so a listing of a thousand tiles still loads small images.
   */
  imgSrcLarge = computed(() => {
    return this.trackInfo().currentTrack?.albumArtUrlLarge || this.imgSrc();
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
          // update footer background to the now-playing artwork; the full-screen
          // "living canvas" wash (#main-screen) is driven by the browsed item
          // instead, so the chrome stays coherent with the header you see.
          console.log('updating background images');
          this.backgroundImageService.setFooterBackgroundImage(
            data.currentTrack?.albumArtUrl,
          );
        }
        // A new track starts over at 0. Without this the interpolated position would keep counting
        // from the previous track until the next position sample arrives (up to 5s later).
        if (this.trackKey(data) !== this.trackKey(this.trackInfoUpnp())) {
          const metaDuration = data.currentTrack?.audioFormat?.durationInSeconds ?? 0;
          const anchor = this.positionAnchor();
          this.positionAnchor.set({
            ...anchor,
            seconds: 0,
            duration: metaDuration > 0 ? metaDuration : anchor.duration,
            atMs: Date.now(),
          });
          this.nowMs.set(Date.now());
        }
        this.trackInfoUpnp.set(data);
      }
    });

    sseService.mediaRendererPositionChanged$.subscribe((data) => {
      if (deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.applyPositionEvent(data);
      }
    });

    sseService.mediaRendererTransportStateChanged$.subscribe((data) =>
      this.updateTransportState(data),
    );
    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(
      (data) => this.renderDeviceChanged(data),
    );

    // Drive the interpolation, but only while it is actually needed: a real renderer is selected, it
    // is playing, and the app is visible. The app runs zoneless, so this signal write is what
    // repaints the footer.
    effect((onCleanup) => {
      const needsTicking =
        !this.deviceService.isLocalBrowserSelected() &&
        this.isPlaying() &&
        this.appVisibilityService.isVisible();
      if (!needsTicking) {
        return;
      }
      // Set once up front so restarting the clock does not show a stale frame for up to a second.
      this.nowMs.set(Date.now());
      const timerId = setInterval(() => this.nowMs.set(Date.now()), 1000);
      onCleanup(() => clearInterval(timerId));
    });
  }

  /** Identity of a now-playing track, used to detect a track change. */
  private trackKey(info: TrackInfoDto): string {
    const track = info?.currentTrack;
    return (track?.objectID ?? '') + '|' + (info?.uri ?? track?.title ?? '');
  }

  /** Takes a position sample from the renderer as the new anchor for the interpolation. */
  private applyPositionEvent(dto: TrackTimeDto): void {
    const displayed = this.trackTimeUpnp().seconds;
    let seconds = Math.max(0, dto.seconds ?? 0);
    // Absorb small backward steps: polling and GENA delivery jitter can report a sample marginally
    // behind what is already on screen, which would look like the time ticking backwards. A larger
    // difference is a real seek or track change and must be applied.
    if (seconds < displayed && displayed - seconds <= 2) {
      seconds = displayed;
    }
    this.positionAnchor.set({
      udn: dto.mediaRendererUdn,
      seconds,
      duration: dto.duration ?? 0,
      streaming: dto.streaming,
      trackCount: dto.trackCount,
      atMs: Date.now(),
    });
    this.nowMs.set(Date.now());
  }

  /**
   * Freezes the currently displayed position and rebases the clock on it. Needed whenever playback
   * pauses or resumes: a plain AVTransport renderer sends no position samples at all while paused, so
   * without rebasing, the wall clock kept running and a two minute pause would surface as a two
   * minute jump forward on resume.
   */
  private reanchorToDisplayed(): void {
    const anchor = this.positionAnchor();
    const seconds = RendererService.interpolate(anchor, this.nowMs());
    const now = Date.now();
    this.positionAnchor.set({ ...anchor, seconds, atMs: now });
    this.nowMs.set(now);
  }

  /**
   * Moves the displayed position to a seek target right away, instead of letting it snap back to the
   * last reported sample until the renderer reports the new one.
   */
  public anchorPosition(seconds: number): void {
    const anchor = this.positionAnchor();
    const target = Math.max(0, anchor.duration > 0 ? Math.min(seconds, anchor.duration) : seconds);
    const now = Date.now();
    this.positionAnchor.set({ ...anchor, seconds: target, atMs: now });
    this.nowMs.set(now);
  }

  private updateTransportState(state: TransportServiceStateDto) {
    if (state.udn == this.deviceService.selectedMediaRendererDevice().udn) {
      console.log('new transport state : ' + state.transportState);
      if (state.transportState !== this.transportStateUpnp().transportState) {
        this.reanchorToDisplayed();
      }
      this.transportStateUpnp.set(state);
    }
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    console.log('renderDeviceChanged to : ' + device.friendlyName);
    // Drop the previous renderer's position; there is no REST endpoint to read the new one, so the
    // display stays frozen until its first position event arrives.
    this.positionAnchor.set({
      udn: device.udn,
      seconds: 0,
      duration: 0,
      streaming: false,
      trackCount: 0,
      atMs: 0,
    });
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

}
