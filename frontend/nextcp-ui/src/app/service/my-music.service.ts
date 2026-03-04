import { ToastService } from './toast/toast.service';
import { DeviceService } from 'src/app/service/device.service';
import { Subject } from 'rxjs';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';
import { MusicAlbumIds } from './dto';

@Injectable({
  providedIn: 'root'
})

export class MyMusicService {

  private baseUri = '/MyMusicService';

  constructor(
    private httpService: HttpService,
    private deviceService: DeviceService,
    private toastService: ToastService) {
  }

  public likeAlbum(albumIds: MusicAlbumIds): Subject<any> {
    if (!this.deviceService.selectedMediaServerDevice().udn) {
      this.toastService.error("select media server", "like album");
      return;
    }
    const uri = '/likeAlbum/' + this.deviceService.selectedMediaServerDevice().udn;
    let hasId: boolean = false;
    if (albumIds.musicBrainzAlbumId !== '') {
      console.log("like musicbrainz album : " + albumIds.musicBrainzAlbumId);
      hasId = true;
    }
    if (albumIds.discogsReleaseId != undefined) {
      console.log("like discogs release : " + albumIds.discogsReleaseId);
      hasId = true;
    }
    if (hasId) {      
      return this.httpService.post(this.baseUri, uri, albumIds);
    } else {
      console.log("no id's given for like album");
    }
  }

  public deleteAlbumLike(albumIds: MusicAlbumIds): Subject<any> {
    const uri = '/deleteAlbumLike/' + this.deviceService.selectedMediaServerDevice().udn;
    if (albumIds.musicBrainzAlbumId !== '' || albumIds.discogsReleaseId != undefined) {
      return this.httpService.post(this.baseUri, uri, albumIds);
    } else {
      console.log("no id's given for delete album like");
    }
  }

  public isAlbumLiked(albumIds: MusicAlbumIds): Subject<boolean> {
    const uri = '/isAlbumLiked/' + this.deviceService.selectedMediaServerDevice().udn;
    if (albumIds.musicBrainzAlbumId !== '' || albumIds.discogsReleaseId != undefined) {
      return this.httpService.post<boolean>(this.baseUri, uri, albumIds);
    } else {
      console.log("no id's given for is album liked");
    }
  }

  public backupLikedAlbums(): void {
    if (!this.deviceService.selectedMediaServerDevice().udn) {
      this.toastService.error("select media server first", "backup like album");
      return;
    }
    const uri = '/backupLikedAlbums/' + this.deviceService.selectedMediaServerDevice().udn;
    this.httpService.get(this.baseUri, uri, "backup liked albums");
  }

  public restoreLikedAlbums(): void {
    if (!this.deviceService.selectedMediaServerDevice().udn) {
      this.toastService.error("select media server first", "restore liked albums");
      return;
    }
    const uri = '/restoreLikedAlbums/' + this.deviceService.selectedMediaServerDevice().udn;
    this.httpService.get(this.baseUri, uri, "restore liked albums");
  }

  public backupRatings(): void {
    if (!this.deviceService.selectedMediaServerDevice().udn) {
      this.toastService.error("select media server first", "backup ratings");
      return;
    }
    const uri = '/backupRatings/' + this.deviceService.selectedMediaServerDevice().udn;
    this.httpService.get(this.baseUri, uri, "backup ratings");
  }

  public restoreRatings(): void {
    if (!this.deviceService.selectedMediaServerDevice().udn) {
      this.toastService.error("select media server first", "restore ratings");
      return;
    }
    const uri = '/restoreRatings/' + this.deviceService.selectedMediaServerDevice().udn;
    this.httpService.get(this.baseUri, uri, "restore ratings");
  }

}
