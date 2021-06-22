import { AvtransportService } from './../../../service/avtransport.service';
import { Router } from '@angular/router';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, ContainerItemDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { Component, OnInit } from '@angular/core';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';

@Component({
  selector: 'app-search-result-item-multi',
  templateUrl: './search-result-item-multi.component.html',
  styleUrls: ['./search-result-item-multi.component.scss']
})
export class SearchResultItemMultiComponent {

  constructor(
    private avtransportService: AvtransportService,
    public searchItemService: SearchItemService,
    private songOptionsServiceService: SongOptionsServiceService,
    private timeDisplayService: TimeDisplayService,
    private contentDirectoryService: ContentDirectoryService,
    private router: Router) {

  }

  browseToContainer(container: ContainerDto): void {
    this.contentDirectoryService.browseChildrenByContiner(container);
    void this.router.navigateByUrl('music-library');
  }

  play(musicItemDto: MusicItemDto): void {
    this.avtransportService.playResource(musicItemDto);
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateString(item.audioFormat.durationInSeconds);
    } else {
      return "";
    }
  }

  showSongPopup(event: any, item: MusicItemDto) : void{
    this.songOptionsServiceService.openOptionsDialog(event, item);
  }

  get hasSongItems(): boolean {
    return this.searchItemService.musicItemList?.musicItems?.length > 0;
  }

  get hasAlbumItems(): boolean {
    return this.searchItemService.musicItemList?.albumItems?.length > 0;
  }

  get albumItems(): ContainerDto[] {
    return this.searchItemService.musicItemList.albumItems;
  }

  get hasPlaylistItems(): boolean {
    return this.searchItemService.musicItemList?.playlistItems?.length > 0;
  }

  get playlistItems(): ContainerDto[] {
    return this.searchItemService.musicItemList.playlistItems;
  }

  get hasArtistItems(): boolean {
    return this.searchItemService.musicItemList?.artistItems?.length > 0;
  }

  get artistItems(): ContainerDto[] {
    return this.searchItemService.musicItemList.artistItems;
  }
}
