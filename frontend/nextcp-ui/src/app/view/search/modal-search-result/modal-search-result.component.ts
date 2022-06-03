import { AvtransportService } from './../../../service/avtransport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { DtoGeneratorService } from './../../../util/dto-generator.service';
import { DeviceService } from './../../../service/device.service';
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
    public contentDirectoryService: ContentDirectoryService,
    private searchItemService: SearchItemService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private avtransportService: AvtransportService,
    private router: Router) {
  }

  //
  // single item selected
  //

  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug("search item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.contentDirectoryService.clearSearch();
    this.avtransportService.playResource(musicItem);
  }

  albumItemSelected(albumItem: ContainerDto): void {
    console.debug("album selected : " + albumItem);
    this.contentDirectoryService.clearSearch();
    this.contentDirectoryService.browseChildrenByContiner(albumItem);
    void this.router.navigateByUrl('music-library');
  }

  playlistItemSelected(playlistItem: ContainerDto): void {
    console.debug("album selected : " + playlistItem);
    this.contentDirectoryService.clearSearch();
    this.contentDirectoryService.browseChildrenByContiner(playlistItem);
    void this.router.navigateByUrl('music-library');
  }

  artistItemSelected(artistItem: ContainerDto): void {
    console.debug("album selected : " + artistItem);
    this.contentDirectoryService.clearSearch();
    this.contentDirectoryService.browseChildrenByContiner(artistItem);
    void this.router.navigateByUrl('music-library');
  }

  //
  // show all clicked
  // 


  // TODO: Should include a "please wait ..." dialog for long 'showAll...' queries ?

  get currentContainerID(): string {
    return this.contentDirectoryService.currentContainerList.currentContainer.id;
  }

  showAllItem(): void {
    this.contentDirectoryService.searchAllItems(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "-upnp:rating, +dc:title", this.currentContainerID, 0, 100));
    this.contentDirectoryService.hideQuickSearchPanel();
  }

  showAllAlbum(): void {
    this.contentDirectoryService.searchAllAlbum(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "-ums:likedAlbum, +dc:title", this.currentContainerID, 0, 100));
    this.contentDirectoryService.hideQuickSearchPanel();
  }

  showAllItemArtist(): void {
    this.contentDirectoryService.searchAllArtists(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "", this.currentContainerID, 0, 100));
    this.contentDirectoryService.hideQuickSearchPanel();
  }

  showAllPlaylist(): void {
    this.contentDirectoryService.searchAllPlaylist(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.contentDirectoryService.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "", this.currentContainerID, 0, 100));
    this.contentDirectoryService.hideQuickSearchPanel();
  }  
}
