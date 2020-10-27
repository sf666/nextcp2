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
}
