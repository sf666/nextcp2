import { GlobalSearchService } from './../../../service/search/global-search.service';
import { TransportService } from '../../../service/transport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { RouteReuseStrategy, Router } from '@angular/router';
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
    private transportService: TransportService,
    private router: Router) {
  }

  //
  // single item selected
  //

  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug("music item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.globalSearchService.clearSearch();
    this.transportService.playResource(musicItem);
  }

  albumItemSelected(albumItem: ContainerDto): void {
    console.debug("album selected : " + albumItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.selectedRootContainer = albumItem;
    void this.router.navigateByUrl('searchResult', {onSameUrlNavigation: 'reload'});
  }

  playlistItemSelected(playlistItem: ContainerDto): void {
    console.debug("playlist selected : " + playlistItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.selectedRootContainer = playlistItem;
    void this.router.navigateByUrl('searchResult', {onSameUrlNavigation: 'reload'});
  }

  artistItemSelected(artistItem: ContainerDto): void {
    console.debug("artist selected : " + artistItem);
    this.globalSearchService.clearSearch();
    this.globalSearchService.selectedRootContainer = artistItem;
    void this.router.navigateByUrl('searchResult', {onSameUrlNavigation: 'reload'});
  }
}
