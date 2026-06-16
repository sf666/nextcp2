import { MusicItemIdDto, UpdateAlbumArtUriRequest } from './dto.d';
import { Injectable, inject } from '@angular/core';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CdsUpdateService {
  private httpService = inject(HttpService);
  private deviceSerice = inject(DeviceService);

  baseUri = '/ContentDirectoryService';

  public setNewAlbumArtUri(
    ids: MusicItemIdDto,
    oldAlbumArtURI: string,
    albumArtURI: string,
  ): Subject<void> {
    const uri = '/updateAlbumArtUri';

    const updateRequest: UpdateAlbumArtUriRequest = {
      previousAlbumArtUri: oldAlbumArtURI,
      newAlbumArtUri: albumArtURI,
      musicItemIdDto: ids,
      mediaServerDevice: this.deviceSerice.selectedMediaServerDevice().udn,
    };

    return this.httpService.post<void>(this.baseUri, uri, updateRequest);
  }
}
