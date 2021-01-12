import { ToastrService } from 'ngx-toastr';
import { HttpService } from './http.service';
import { SseService } from './sse/sse.service';
import { MusicItemDto, PlayRequestDto, MediaRendererDto, UpnpAvTransportState, RadioStation, PlayRadioDto } from './dto.d';
import { Injectable } from '@angular/core';
import { DeviceService } from './device.service';

@Injectable({
  providedIn: 'root'
})
export class AvtransportService {

  upnpAvTransportState: UpnpAvTransportState;
  selectedMediaRenderer: MediaRendererDto;

  baseUri = '/AvTransportService';

  constructor(
    private sse: SseService,
    private httpService: HttpService,
    private toastr: ToastrService,
    private deviceService: DeviceService) {

    // Register for application event (user selected new media player within the UI)
    this.deviceService.mediaRendererChanged$.subscribe(data => this.renderDeviceChanged(data));

    // Register to domain event (change of TransportState)
    this.sse.mediaRendererAvTransportStateChanged$.subscribe(data => {
      if (this.deviceService.isMediaRendererSelected(data.mediaRenderer.udn)) {
        this.updateTransportState(data);
      }
    });
  }

  // 
  // event processing 
  // ================================================================================

  private updateTransportState(upnpAvTransportState: UpnpAvTransportState) {
    if (this.deviceService.isMediaRendererSelected(upnpAvTransportState.mediaRenderer.udn)) {
      this.upnpAvTransportState = upnpAvTransportState;
    }
  }

  private renderDeviceChanged(device: MediaRendererDto) {
    this.selectedMediaRenderer = device;
    this.getInitialAvTransportState();
  }

  public getInitialAvTransportState() {
    const uri = '/MediaRendererAvTransportState';
    this.httpService.post<UpnpAvTransportState>(this.baseUri, uri, this.selectedMediaRenderer).subscribe();
  }

  //
  // Status Infos
  // ================================================================================

  public isPlaying(): boolean {
    let playing: boolean = this.upnpAvTransportState?.TransportState === 'PLAYING';
    return playing;
  }

  //
  // AvTransportService actions ()
  // ================================================================================

  public play(): void {
    const uri = '/play';
    this.httpService.post(this.baseUri, uri, this.selectedMediaRenderer.udn).subscribe();
  }

  public pause(): void {
    const uri = '/pause';
    this.httpService.post(this.baseUri, uri, this.selectedMediaRenderer.udn).subscribe();
  }

  public playResource(musicItemDto: MusicItemDto) {
    const uri = '/playResource';
    this._playResource(uri, musicItemDto);
  }

  public playResourceNext(musicItemDto: MusicItemDto) {
    const uri = '/playResourceNext';
    this._playResource(uri, musicItemDto);
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

  playRadio(radio: RadioStation) {
    const uri = '/playOnlineResource';
    const playReq: PlayRadioDto = {
      mediaRendererDto: this.selectedMediaRenderer,
      radioStation: radio
    }

    this.httpService.post(this.baseUri, uri, playReq).subscribe();
  }

}
