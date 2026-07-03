import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { ToastService } from './toast/toast.service';
import { HttpService } from './http.service';
import { SseService } from './sse/sse.service';
import {
  MusicItemDto,
  PlayRequestDto,
  MediaRendererDto,
  UpnpAvTransportState,
  RadioStation,
  PlayRadioDto,
  SeekSecondsDto,
} from './dto';
import { Injectable, signal, inject } from '@angular/core';
import { DeviceService } from './device.service';
import { RadioService } from './radio.service';
import { LocalPlayerService } from './local-player.service';

@Injectable({
  providedIn: 'root',
})
export class TransportService {
  private sse = inject(SseService);
  private httpService = inject(HttpService);
  private toastr = inject(ToastService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private deviceService = inject(DeviceService);
  private radioService = inject(RadioService);
  private localPlayer = inject(LocalPlayerService);

  upnpAvTransportState = signal<UpnpAvTransportState>(
    this.dtoGeneratorService.emptyUpnpAvTransportState(),
  );
  public lastPlayedMusicItem!: MusicItemDto;
  baseUri = '/TransportService';

  constructor() {
    // Register to domain event (change of TransportState)
    this.sse.mediaRendererAvTransportStateChanged$.subscribe((data) => {
      if (this.deviceService.isMediaRendererSelected(data.mediaRenderer.udn)) {
        this.updateTransportState(data);
      }
    });
  }

  get selectedMediaRenderer(): MediaRendererDto {
    return this.deviceService.selectedMediaRendererDevice();
  }

  //
  // event processing
  // ================================================================================

  private updateTransportState(upnpAvTransportState: UpnpAvTransportState) {
    if (
      this.deviceService.isMediaRendererSelected(
        upnpAvTransportState.mediaRenderer.udn,
      )
    ) {
      this.upnpAvTransportState.set(upnpAvTransportState);
    }
  }

  //
  // TransportService actions ()
  // ================================================================================

  public play(): void {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.resume();
      return;
    }
    const uri = '/play';
    this.httpService
      .post(this.baseUri, uri, this.selectedMediaRenderer.udn)
      .subscribe();
  }

  public pause(): void {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.pause();
      return;
    }
    const uri = '/pause';
    this.httpService
      .post(this.baseUri, uri, this.selectedMediaRenderer.udn)
      .subscribe();
  }

  public seek(secondsAbsolute: number): void {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.seek(secondsAbsolute);
      return;
    }
    const uri = '/seekSecondsAbsolute';
    if (this.deviceService.selectedMediaRendererDevice().udn) {
      let seek: SeekSecondsDto = {
        rendererUDN: this.deviceService.selectedMediaRendererDevice().udn,
        seconds: secondsAbsolute,
      };
      this.httpService.post(this.baseUri, uri, seek).subscribe();
    }
  }

  public playResource(musicItemDto: MusicItemDto): void {
    // When the synthetic "This Browser" renderer is selected, play the stream in the browser itself
    // (HTML5 audio) instead of driving a UPnP renderer. This also covers broadcast/radio items - the
    // browser just fetches the stream URL.
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.play(musicItemDto);
      return;
    }
    // A continuous radio/broadcast stream must use the renderer's Radio source (OpenHome), where
    // ICY metadata is consumed - not the Playlist/AVTransport path. Detected via the audioBroadcast
    // UPnP class (e.g. AudioAddict) or the size-derived isStreaming flag (e.g. generic .m3u radio).
    if (this.isBroadcastItem(musicItemDto)) {
      this.radioService.playStream(musicItemDto);
      return;
    }
    const uri = '/playResource';
    this._playResource(uri, musicItemDto);
  }

  private isBroadcastItem(item: MusicItemDto): boolean {
    return (
      item.objectClass?.startsWith('object.item.audioItem.audioBroadcast') === true ||
      item.audioFormat?.isStreaming === true
    );
  }

  public playResourceNext(musicItemDto: MusicItemDto): void {
    const uri = '/playResourceNext';
    this._playResource(uri, musicItemDto);
  }

  private _playResource(uri: string, musicItemDto: MusicItemDto) {
    if (this.selectedMediaRenderer?.udn?.length > 0) {
      const playReq: PlayRequestDto = {
        mediaRendererDto: this.selectedMediaRenderer,
        streamMetadata: musicItemDto.currentTrackMetadata,
        streamUrl: musicItemDto.streamingURL,
      };

      this.httpService.post(this.baseUri, uri, playReq).subscribe();
    } else {
      this.toastr.error(
        'select media renderer before playing songs',
        'media renderer',
      );
    }
  }

  playRadio(radio: RadioStation): void {
    const uri = '/playOnlineResource';
    const playReq: PlayRadioDto = {
      mediaRendererDto: this.selectedMediaRenderer,
      radioStation: radio,
    };

    this.httpService.post(this.baseUri, uri, playReq).subscribe();
  }
}
