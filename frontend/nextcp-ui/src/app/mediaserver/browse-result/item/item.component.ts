import { TimeDisplayService } from './../../../util/time-display.service';
import { ScrollViewService } from './../../../util/scroll-view.service';
import { MusicItemDto } from './../../../service/dto.d';
import { PlaylistService } from './../../../service/playlist.service';
import { AvtransportService } from './../../../service/avtransport.service';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component, AfterViewChecked, AfterViewInit } from '@angular/core';

@Component({
  selector: 'browseResultItem',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.scss']
})
export class ItemComponent {

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private scrollViewService: ScrollViewService,
    public avtransportService: AvtransportService,
    private timeDisplayService: TimeDisplayService,
    public playlistService: PlaylistService) { }


  get topDivId(): string {
    return "top-div";
  }

  playAllTracks(): void {
    this.playlistService.addContainerToPlaylistAndPlay(this.contentDirectoryService.currentContainerList.currentContainer, false);
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

  play(musicItemDto: MusicItemDto) {
    this.avtransportService.playResource(musicItemDto);
  }

  getDuration(item: MusicItemDto): string {
    return this.timeDisplayService.convertLongToDateString(item.audioFormat.durationInSeconds);
  }
}
