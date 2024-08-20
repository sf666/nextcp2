import { Subject } from 'rxjs';
import { HttpService } from './../http.service';
import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MediaPlayerService {
  baseUri = 'MediaRendererService';

  constructor(private httpService: HttpService) { }

  public mediaPlayerExists(): Subject<boolean> {
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
}
