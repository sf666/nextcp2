import { AvtransportService } from './../../../service/avtransport.service';
import { Router } from '@angular/router';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
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

  browseToContainer(container: ContainerDto) {
    this.contentDirectoryService.browseChildrenByContiner(container);
    this.router.navigateByUrl('music-library');
  }

  play(musicItemDto: MusicItemDto) {
    this.avtransportService.playResource(musicItemDto);
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateString(item.audioFormat.durationInSeconds);
    } else {
      return "";
    }
  }

  showSongPopup(event : any , item: MusicItemDto) {
    this.songOptionsServiceService.openOptionsDialog(event,item);
  }  
}
