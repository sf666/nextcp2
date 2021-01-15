import { MusicItemDto } from './../service/dto.d';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../service/http.service';

@Injectable({
  providedIn: 'root'
})
export class DownloadService {

  baseUri = '/DownloadService';

  constructor(private httpService: HttpService) { }

  downloadFileByMBID(song: MusicItemDto) {
    let uri = "/DownloadService/downloadFileByMBID/" + song.musicBrainzId.TrackId;
    window.open(uri);
  }

}
