import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { SearchContainerService } from './../../../service/search/search-container.service';
import { DtoGeneratorService } from './../../../util/dto-generator.service';
import { DeviceService } from './../../../service/device.service';
import { Router } from '@angular/router';
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
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private searchContainerService: SearchContainerService,
    private router: Router) {
  }

  //
  // single item selected
  //

  musicItemSelected(musicItem: MusicItemDto) {
    console.debug("search item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.contentDirectoryService.clearSearch();
    this.router.navigateByUrl('searchResultSingleItem');
  }

  albumItemSelected(albumItem: ContainerDto) {
    console.debug("album selected : " + albumItem);
    this.searchContainerService.containerItem = albumItem;
    this.contentDirectoryService.clearSearch();
    this.contentDirectoryService.browseChildrenByContiner(albumItem);
    this.router.navigateByUrl('music-library');
  }

  playlistItemSelected(playlistItem: ContainerDto) {
    console.debug("album selected : " + playlistItem);
    this.searchContainerService.containerItem = playlistItem;
    this.contentDirectoryService.browseChildrenByContiner(playlistItem);
    this.router.navigateByUrl('music-library');
  }

  artistItemSelected(artistItem: ContainerDto) {
    console.debug("album selected : " + artistItem);
    this.searchContainerService.containerItem = artistItem;
    this.contentDirectoryService.browseChildrenByContiner(artistItem);
    this.router.navigateByUrl('music-library');
  }

  //
  // show all clicked
  // 


  // TODO: Should include a "please wait ..." dialog for long 'showAll...' queries ?

  showAllItem() {
    this.contentDirectoryService.searchAllItems(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, ""));
  }

  showAllAlbum() {
    this.contentDirectoryService.searchAllAlbum(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, ""));
  }
  showAllItemArtist() {
    this.contentDirectoryService.searchAllArtists(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, ""));
  }
  showAllPlaylist() {
    this.contentDirectoryService.searchAllPlaylist(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, ""));
  }
}
