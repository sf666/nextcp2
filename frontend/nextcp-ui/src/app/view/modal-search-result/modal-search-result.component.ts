import { SearchItemService } from './../../service/search/search-item.service';
import { SearchResultItemSingleComponent } from './../search/search-result-item-single/search-result-item-single.component';
import { Router, RouterModule } from '@angular/router';
import { MusicItemDto, ContainerItemDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, ViewChild } from '@angular/core';

@Component({
  selector: 'app-modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss']
})

export class ModalSearchResultComponent {

  public searchVisible: boolean;

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private searchItemService: SearchItemService,
    private router: Router) {
    this.searchVisible = false;
  }

  musicItemSelected(musicItem: MusicItemDto) {
    console.debug("search item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.router.navigateByUrl('searchResultSingleItem');
  }

  albumItemSelected(musicItem: ContainerItemDto) {

  }

  playlistItemSelected(musicItem: ContainerItemDto) {

  }

  artistItemSelected(musicItem: ContainerItemDto) {

  }

}
