import { SearchItemService } from './../../service/search/search-item.service';
import { Router} from '@angular/router';
import { MusicItemDto, ContainerItemDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss']
})

export class ModalSearchResultComponent {

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private searchItemService: SearchItemService,
    private router: Router) {
  }

  musicItemSelected(musicItem: MusicItemDto) {
    console.debug("search item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.contentDirectoryService.quickSearchQueryString = "";
    this.contentDirectoryService.quickSearchPanelVisible = false;
    this.router.navigateByUrl('searchResultSingleItem');
  }

  albumItemSelected(musicItem: ContainerItemDto) {

  }

  playlistItemSelected(musicItem: ContainerItemDto) {

  }

  artistItemSelected(musicItem: ContainerItemDto) {

  }

}
