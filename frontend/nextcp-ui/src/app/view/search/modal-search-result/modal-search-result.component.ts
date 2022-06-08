import { GlobalSearchService } from './../../../service/search/global-search.service';
import { AvtransportService } from './../../../service/avtransport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { Router } from '@angular/router';
import { Component } from '@angular/core';

@Component({
  selector: 'app-modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss'],
  providers: [ContentDirectoryService]
})

export class ModalSearchResultComponent {

  constructor(
    private searchItemService: SearchItemService,
    public globalSearchService: GlobalSearchService,
    private avtransportService: AvtransportService,
    private router: Router) {
  }

  //
  // single item selected
  //

  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug("search item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.globalSearchService.clearSearch();
    this.avtransportService.playResource(musicItem);
  }

  albumItemSelected(albumItem: ContainerDto): void {
    console.debug("album selected : " + albumItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.contentDirectoryService.browseChildrenByContiner(albumItem);
    void this.router.navigateByUrl('searchResult');
  }

  playlistItemSelected(playlistItem: ContainerDto): void {
    console.debug("album selected : " + playlistItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.contentDirectoryService.browseChildrenByContiner(playlistItem);
    void this.router.navigateByUrl('searchResult');
  }

  artistItemSelected(artistItem: ContainerDto): void {
    console.debug("album selected : " + artistItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.contentDirectoryService.browseChildrenByContiner(artistItem);
    void this.router.navigateByUrl('searchResult');
  }
}
