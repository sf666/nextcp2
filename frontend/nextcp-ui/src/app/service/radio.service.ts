import { HttpService } from './http.service';
import { MusicItemDto, MediaRendererDto, PlayOpenHomeRadioDto } from './dto.d';
import { Injectable } from '@angular/core';
import { DeviceService } from './device.service';
import { toObservable } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root'
})

export class RadioService {

  private baseUri = '/RadioService';

  // OpenHome RadioService Stations
  radioItems: MusicItemDto[];

  constructor(
    private httpService: HttpService,
    public deviceService: DeviceService) {

    console.log("init RadioService ...");

    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(data => this.deviceChanged(data));

    if (this.deviceService.selectedMediaRendererDevice().udn !== '') {
      this.updateRadioStations(this.deviceService.selectedMediaRendererDevice());
    }
  }

  playOpenHomeStation(station: MusicItemDto): void {
    const req: PlayOpenHomeRadioDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice(),
      radioStation: station
    }
    const uri = "/playRadioStation";
    this.httpService.post(this.baseUri, uri, req).subscribe();
  }


  private deviceChanged(rendererDto: MediaRendererDto): void {
    this.updateRadioStations(rendererDto);
  }

  private updateRadioStations(rendererDto: MediaRendererDto) {
    if (rendererDto.udn !== '') {
      console.log("updating radio stations ...");
      const uri = '/deviceRadioStations';
      this.httpService.post<MusicItemDto[]>(this.baseUri, uri, rendererDto).subscribe(data => this.radioItems = data);
    }
  }
}
