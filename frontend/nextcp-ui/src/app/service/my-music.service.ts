import { ToastService } from './toast/toast.service';
import { DeviceService } from 'src/app/service/device.service';
import { GenericResultService } from './generic-result.service';
import { ContentDirectoryService } from './content-directory.service';
import { Subject } from 'rxjs';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class MyMusicService {

  baseUri = '/MyMusicService';

  constructor(
    private httpService: HttpService,
    private deviceService: DeviceService,
    private toastService: ToastService,
    private genericResultService: GenericResultService) {
  }

  public likeAlbum(mbReleaseID: string): Subject<any> {
    if (!this.deviceService.selectedMediaServerDevice?.udn) {
      this.toastService.error("select media server", "like album");
      return;
    }
    const uri = '/likeAlbum/' + this.deviceService.selectedMediaServerDevice.udn;
    if (mbReleaseID !== '') {
      return this.httpService.post(this.baseUri, uri, mbReleaseID);
    }
  }

  public deleteAlbumLike(mbReleaseID: string): Subject<any> {
    const uri = '/deleteAlbumLike/' + this.deviceService.selectedMediaServerDevice.udn;
    if (mbReleaseID !== '') {
      return this.httpService.post(this.baseUri, uri, mbReleaseID);
    }
  }

  public isAlbumLiked(mbReleaseID: string): Subject<boolean> {
    const uri = '/isAlbumLiked/' + this.deviceService.selectedMediaServerDevice.udn;
    if (mbReleaseID !== '') {
      return this.httpService.post<boolean>(this.baseUri, uri, mbReleaseID);
    }
  }

  public backupLikedAlbums(): void {
    if (!this.deviceService.selectedMediaServerDevice?.udn) {
      this.toastService.error("select media server first", "backup like album");
      return;
    }
    const uri = '/backupLikedAlbums/' + this.deviceService.selectedMediaServerDevice.udn;
    this.httpService.getWithGenericResult(this.baseUri, uri, "backup liked albums");
  }

  public restoreLikedAlbums(): void {
    if (!this.deviceService.selectedMediaServerDevice?.udn) {
      this.toastService.error("select media server first", "restore liked albums");
      return;
    }
    const uri = '/restoreLikedAlbums/' + this.deviceService.selectedMediaServerDevice.udn;
    this.httpService.getWithGenericResult(this.baseUri, uri, "restore liked albums");
  }
}
