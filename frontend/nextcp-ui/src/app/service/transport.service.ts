import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { ToastService } from './toast/toast.service';
import { HttpService } from './http.service';
import { SseService } from './sse/sse.service';
import { MusicItemDto, PlayRequestDto, MediaRendererDto, UpnpAvTransportState, RadioStation, PlayRadioDto, SeekSecondsDto } from './dto';
import { Injectable, signal } from '@angular/core';
import { DeviceService } from './device.service';

@Injectable({
  providedIn: 'root'
})
export class TransportService {

  upnpAvTransportState = signal<UpnpAvTransportState>(this.dtoGeneratorService.emptyUpnpAvTransportState());
  public lastPlayedMusicItem: MusicItemDto;
  baseUri = '/TransportService';

  constructor(
    private sse: SseService,
    private httpService: HttpService,
    private toastr: ToastService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService) {

    // Register to domain event (change of TransportState)
    this.sse.mediaRendererAvTransportStateChanged$.subscribe(data => {
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
    if (this.deviceService.isMediaRendererSelected(upnpAvTransportState.mediaRenderer.udn)) {
      this.upnpAvTransportState.set(upnpAvTransportState);
    }
  }

  //
  // TransportService actions ()
  // ================================================================================

  public play(): void {
    const uri = '/play';
    this.httpService.post(this.baseUri, uri, this.selectedMediaRenderer.udn).subscribe();
  }

  public pause(): void {
    const uri = '/pause';
    this.httpService.post(this.baseUri, uri, this.selectedMediaRenderer.udn).subscribe();
  }

  public seek(secondsAbsolute: number): void {
    const uri = '/seekSecondsAbsolute';
    if (this.deviceService.selectedMediaRendererDevice().udn) {
      let seek: SeekSecondsDto = { rendererUDN: this.deviceService.selectedMediaRendererDevice().udn, seconds: secondsAbsolute };
      this.httpService.post(this.baseUri, uri, seek).subscribe();
    }
  }

  public playResource(musicItemDto: MusicItemDto): void {
    const uri = '/playResource';
    this._playResource(uri, musicItemDto);
  }

  public playResourceNext(musicItemDto: MusicItemDto): void {
    const uri = '/playResourceNext';
    this._playResource(uri, musicItemDto);
  }

  public playUrl(url : string) : void {
    const uri = '/playResource';
    if (this.selectedMediaRenderer?.udn?.length > 0) {
      const playReq: PlayRequestDto = {
        mediaRendererDto: this.selectedMediaRenderer,
        streamMetadata: '',
        streamUrl: url
      }

      this.httpService.post(this.baseUri, uri, playReq).subscribe();
    } else {
      this.toastr.error("select media renderer before playing songs", "media renderer");
    }
  }

  private _playResource(uri: string, musicItemDto: MusicItemDto) {
    if (this.selectedMediaRenderer?.udn?.length > 0) {
      const playReq: PlayRequestDto = {
        mediaRendererDto: this.selectedMediaRenderer,
        streamMetadata: musicItemDto.currentTrackMetadata,
        streamUrl: musicItemDto.streamingURL
      }

      this.httpService.post(this.baseUri, uri, playReq).subscribe();
    } else {
      this.toastr.error("select media renderer before playing songs", "media renderer");
    }
  }

  playRadio(radio: RadioStation): void {
    const uri = '/playOnlineResource';
    const playReq: PlayRadioDto = {
      mediaRendererDto: this.selectedMediaRenderer,
      radioStation: radio
    }

    this.httpService.post(this.baseUri, uri, playReq).subscribe();
  }
}
