import { CdsBrowsePathService } from './../../../util/cds-browse-path.service';
import { delay } from './../../../global';
import { PlaylistService } from './../../../service/playlist.service';
import { ContainerDto } from './../../../service/dto.d';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component } from '@angular/core';

@Component({
  selector: 'browseResultAlbum',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.scss']
})
export class AlbumComponent {

  constructor(
    public playlistService: PlaylistService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private contentDirectoryService: ContentDirectoryService) { }


  public get albumList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.albumDto;
  }

  public browseTo(containerDto: ContainerDto) {
    this.contentDirectoryService.browseChildren(containerDto.id, containerDto.parentID, containerDto.mediaServerUDN);
  }

  playAlbum(container) {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

}
