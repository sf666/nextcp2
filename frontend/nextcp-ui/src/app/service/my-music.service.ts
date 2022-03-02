import { ContainerItemDto } from './dto.d';
import { ContentDirectoryService } from './content-directory.service';
import { Subject } from 'rxjs';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class MyMusicService {

  baseUri = '/MyMusicService';

  public currentAlbumLiked = false;
  public currentAlbumReleaseID = "";

  constructor(
    private httpService: HttpService,
    private contentDirectoryService: ContentDirectoryService) {
    contentDirectoryService.browseFinished$.subscribe(data => this.updateContainerStatus())
  }

  private updateContainerStatus(): void {
    if (this.contentDirectoryService.allTracksSameMusicBrainzReleaseId()) {
      if (this.contentDirectoryService.getMusicTracks()[0]?.musicBrainzId?.ReleaseTrackId) {
        this.currentAlbumReleaseID = this.contentDirectoryService.getMusicTracks()[0].musicBrainzId.ReleaseTrackId;
        this.isAlbumLiked(this.currentAlbumReleaseID).subscribe(
          res => this.currentAlbumLiked = res
        )
      }
    } else {
      this.currentAlbumLiked = false;
      this.currentAlbumReleaseID = undefined;
    }
  }

  public likeAlbum(mbReleaseID: string): void {
    const uri = '/likeAlbum/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      this.httpService.post(this.baseUri, uri, mbReleaseID).subscribe(d => this.updateContainerStatus());
    }
  }

  public deleteAlbumLike(mbReleaseID: string): void {
    const uri = '/deleteAlbumLike/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      this.httpService.post(this.baseUri, uri, mbReleaseID).subscribe(d => this.updateContainerStatus());
    }
  }

  public isAlbumLiked(mbReleaseID: string): Subject<boolean> {
    const uri = '/isAlbumLiked/' + this.contentDirectoryService.getCurrentMediaServerDto().udn;
    if (mbReleaseID !== '') {
      return this.httpService.post<boolean>(this.baseUri, uri, mbReleaseID);
    }
  }
}
