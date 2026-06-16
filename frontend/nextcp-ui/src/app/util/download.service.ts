import { MusicItemDto } from './../service/dto.d';
import { Injectable, inject } from '@angular/core';
import { HttpService } from '../service/http.service';

@Injectable({
  providedIn: 'root',
})
export class DownloadService {
  private httpService = inject(HttpService);

  baseUri = '/DownloadService';

  downloadFileByMBID(
    song: MusicItemDto,
    callback?: {
      close?: () => void;
      closeThisPopup?: (...args: any[]) => void;
    },
  ): void {
    const a = document.createElement('a');
    a.download = 'file';
    a.href = '/DownloadService/downloadFileByMBID/' + song.songId.objectID;
    a.target = '_blank';
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
