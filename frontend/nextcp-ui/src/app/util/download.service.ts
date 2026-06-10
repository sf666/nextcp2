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

  downloadFileByMBID(song: MusicItemDto, callback?: { close?: () => void; closeThisPopup?: (...args: any[]) => void }): void {

    const a = document.createElement('a');
    a.download = "file";
    a.href = "/DownloadService/downloadFileByMBID/" + song.songId.objectID;
    a.target = "_blank";
    a.style.display = 'none';
    
    a.onclick = function () {
      if (callback) {
        callback.close?.();
        callback.closeThisPopup?.();
      }
    };

    a.click();
  }
}
