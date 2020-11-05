import { PlaylistService } from './../../../service/playlist.service';
import { AvtransportService } from './../../../service/avtransport.service';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component } from '@angular/core';

@Component({
  selector: 'browseResultItem',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent {

  constructor(
    public contentDirectoryService: ContentDirectoryService, 
    public avtransportService: AvtransportService,
    public playlistService: PlaylistService) { }

  playAllTracks() : void {
      this.playlistService.addContainerToPlaylistAndPlay(this.contentDirectoryService.currentContainerList.currentContainer);
  }

  addAllTracks(): void {
    this.playlistService.addContainerToPlaylist(this.contentDirectoryService.currentContainerList.currentContainer);
  }

  allTracksSameAlbum(): boolean {
    if (this.contentDirectoryService.currentContainerList?.musicItemDto?.length > 0) {
      let firstTrackAlbum = this.contentDirectoryService.currentContainerList.musicItemDto[0].album;
      return this.contentDirectoryService.currentContainerList.musicItemDto.filter(item => item.album !== firstTrackAlbum).length == 0;
    }
    return true;
  }

}
