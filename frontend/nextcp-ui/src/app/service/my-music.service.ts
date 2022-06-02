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
    private genericResultService: GenericResultService,
    private contentDirectoryService: ContentDirectoryService) {
  }

  public likeAlbum(mbReleaseID: string): Subject<any> {
    const uri = '/likeAlbum/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      return this.httpService.post(this.baseUri, uri, mbReleaseID);
    }
  }

  public deleteAlbumLike(mbReleaseID: string): Subject<any> {
    const uri = '/deleteAlbumLike/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      return this.httpService.post(this.baseUri, uri, mbReleaseID);
    }
  }

  public isAlbumLiked(mbReleaseID: string): Subject<boolean> {
    const uri = '/isAlbumLiked/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      return this.httpService.post<boolean>(this.baseUri, uri, mbReleaseID);
    }
  }

  public backupLikedAlbums(): void {
    if (this.contentDirectoryService.getCurrentMediaServerDto()?.udn) {
      const uri = '/backupLikedAlbums/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
      this.httpService.getWithGenericResult(this.baseUri, uri, "backup liked albums");
    } else {
      this.genericResultService.displayErrorMessage("please select a media server", "backup liked albums");
    }
  }

  public restoreLikedAlbums(): void {
    if (this.contentDirectoryService.getCurrentMediaServerDto()?.udn) {
      const uri = '/restoreLikedAlbums/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
      this.httpService.getWithGenericResult(this.baseUri, uri, "restore liked albums");
    } else {
      this.genericResultService.displayErrorMessage("please select a media server", "restore liked albums");
    }
  }
}
