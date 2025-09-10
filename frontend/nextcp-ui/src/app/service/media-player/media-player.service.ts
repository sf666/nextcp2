import { DeviceService } from './../device.service';
import { Subject } from 'rxjs';
import { HttpService } from './../http.service';
import { HttpHeaders } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MediaPlayerService {
  baseUri = 'MediaRendererService';

  public mediaServerExists = signal<boolean>(false);

  constructor(
    private deviceService: DeviceService,
    private httpService: HttpService
  ) {
    this.mediaPlayerExistsRequest().subscribe(status => {
      console.log("mediaPlayerExists : " + status);
      this.mediaServerExists.set(status);
    });
  }

  public mediaPlayerExistsRequest(): Subject<boolean> {
    const uri = '/mediaPlayerExists';
    return this.httpService.get<boolean>(this.baseUri, uri);
  }

  public isPlayScreening(): Subject<boolean> {
    const uri = '/isPlayScreening';
    return this.httpService.get<boolean>(this.baseUri, uri);
  }

  public stopPlayScreening(): void {
    const uri = '/stopPlayScreening';
    this.httpService.get<boolean>(this.baseUri, uri);
  }

  public startPlayScreening(): void {
    const uri = '/startPlayScreening';
    this.httpService.get<boolean>(this.baseUri, uri);
  }

  public createFolder(): void {
    const uri = '/createFolder';
    this.httpService.post<boolean>(this.baseUri, uri, this.deviceService.selectedMediaServerDevice().udn);
  }

  public upload(): void {
    const uri = '/upload';
    console.log("upload : " + this.deviceService.selectedMediaServerDevice().udn);
    this.httpService.post<boolean>(this.baseUri, uri, this.deviceService.selectedMediaServerDevice().udn);
  }
}
