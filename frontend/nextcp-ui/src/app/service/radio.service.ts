import { HttpService } from './http.service';
import { MusicItemDto, MediaRendererDto, PlayOpenHomeRadioDto, PlayRequestDto } from './dto.d';
import { Injectable, signal, inject } from '@angular/core';
import { DeviceService } from './device.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})
export class RadioService {
  private httpService = inject(HttpService);
  deviceService = inject(DeviceService);

  private baseUri = '/RadioService';

  // OpenHome RadioService Stations
  radioItems = signal<MusicItemDto[]>([]);

  constructor() {
    console.log('init RadioService ...');

    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(
      (data) => this.deviceChanged(data),
    );

    if (this.deviceService.selectedMediaRendererDevice().udn !== '') {
      this.updateRadioStations(
        this.deviceService.selectedMediaRendererDevice(),
      );
    }
  }

  playOpenHomeStation(station: MusicItemDto): void {
    const req: PlayOpenHomeRadioDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice(),
      radioStation: station,
    };
    const uri = '/playRadioStation';
    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  // Plays an arbitrary stream URL (e.g. an audioBroadcast item from a media server) via the
  // renderer's OpenHome Radio source: the backend switches the source to "Radio" and loads the
  // stream with its metadata, so the renderer treats it as radio and consumes ICY metadata.
  playStream(item: MusicItemDto): void {
    const req: PlayRequestDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice(),
      streamUrl: item.streamingURL,
      streamMetadata: item.currentTrackMetadata,
    };
    const uri = '/playStream';
    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  private deviceChanged(rendererDto: MediaRendererDto): void {
    this.updateRadioStations(rendererDto);
  }

  private updateRadioStations(rendererDto: MediaRendererDto) {
    // The synthetic "This Device" renderer has no UPnP OpenHome Radio source on the backend.
    if (rendererDto.udn === '' || rendererDto.udn === this.deviceService.LOCAL_BROWSER_UDN) {
      this.radioItems.set([]);
      return;
    }
    console.log('updating radio stations ...');
    const uri = '/deviceRadioStations';
    this.httpService
      .post<MusicItemDto[]>(this.baseUri, uri, rendererDto)
      .subscribe((data) => {
        console.log('radio stations size : ' + data.length);
        this.radioItems.set(data);
      });
  }
}
