import { Router } from '@angular/router';
import { ConfigurationService } from './../configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ContentDirectoryService } from './../content-directory.service';
import { SearchResultDto, ContainerDto, MusicItemDto } from './../dto.d';
import { Injectable } from '@angular/core';
import { debounce } from 'src/app/global';
import { Subject } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class GlobalSearchService {

  //
  // Event publishing
  //

  // User clicked on an quick search music item
  musicItemClicked$: Subject<MusicItemDto> = new Subject();

  // User clicked on an quick search container like album, person or playlist
  containerClicked$: Subject<ContainerDto> = new Subject();



  // QuickSearch Support (Global search) 
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  private quickSearchPanelVisible_: boolean;

  public lastSearch;

  private currentSearchText: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private doSearchFunc: any;

  private doSearchThrotteled = (): void => {
    this.contentDirectoryService.quickSearch(this.currentSearchText, "", this.deviceService.selectedMediaServerDevice.udn).subscribe(data => this.searchResultReceived(data));
  };


  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private deviceService: DeviceService,
    private dtoGeneratorService: DtoGeneratorService,
    private router: Router,
    private configurationService: ConfigurationService
  ) {
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible_ = false;
    this.doSearchFunc = debounce(this.getSearchDelay(), this.doSearchThrotteled);
  }

  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }

  get quickSearchPanelVisible() {
    return this.quickSearchPanelVisible_;
  }

  set quickSearchPanelVisible(value : boolean) {
    this.quickSearchPanelVisible_ = value;
    if (value == false) {
      this.clearSearch();      
    }
  }

  public showQuickSearchPanel(): void {
    this.quickSearchPanelVisible_ = true;
  }

  public hideQuickSearchPanel(): void {
    this.quickSearchPanelVisible_ = false;
  }


  private searchResultReceived(data: SearchResultDto): void {
    this.quickSearchResultList = data;
  }

  private doSearch(): void {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    this.doSearchFunc();
  }


  //
  // Search
  // =========================================================================

  get quickSearchString(): string {
    return this.quickSearchQueryString;
  }

  set quickSearchString(value: string) {
    this.quickSearchQueryString = value;
    if (value == '') {
      this.quickSearchPanelVisible_ = false;
    } else {
      if (value && value.length > 2) {
        this.quickSearchPanelVisible_ = true;
        this.currentSearchText = value;
        this.doSearch();
      }
    }
  }

  clearSearch(): void {
    this.quickSearchString = "";
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible_ = false;
  }

  public backToLastSearch() {
    if (this.lastSearch) {
      this.lastSearch();
    }
  }

  public setSelectedContainer(container : ContainerDto): void {
    this.contentDirectoryService.browseChildrenByContainer(container);
  }

  //
  // item or container selected
  //
  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug("music item selected : " + musicItem);
    this.quickSearchPanelVisible = false;
    this.clearSearch();
    this.musicItemClicked$.next(musicItem);
  }

  containerSelected(container: ContainerDto): void {
    console.debug("container selected : " + container.id);
    this.quickSearchPanelVisible = false;
    this.clearSearch();
    this.containerClicked$.next(container);
  }

  //
  // show all clicked
  // 

  get currentContainerID(): string {
    return this.contentDirectoryService.currentContainerList.currentContainer.id;
  }

  showAllItem(): void {
    this.contentDirectoryService.searchAllItems(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "-upnp:rating, +dc:title", this.currentContainerID, 0, 100));
    this.hideQuickSearchPanel();
    this.lastSearch = this.showAllItem;
    void this.router.navigateByUrl('searchResult');
  }

  showAllAlbum(): void {
    this.contentDirectoryService.searchAllAlbum(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "-ums:likedAlbum, +dc:title", this.currentContainerID, 0, 100));
    this.hideQuickSearchPanel();
    this.lastSearch = this.showAllAlbum;
    void this.router.navigateByUrl('searchResult');
  }

  showAllItemArtist(): void {
    this.contentDirectoryService.searchAllArtists(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "", this.currentContainerID, 0, 100));
    this.hideQuickSearchPanel();
    this.lastSearch = this.showAllItemArtist;
    void this.router.navigateByUrl('searchResult');
  }

  showAllPlaylist(): void {
    this.contentDirectoryService.searchAllPlaylist(
      this.dtoGeneratorService.generateQuickSearchDto(
        this.quickSearchQueryString, this.deviceService.selectedMediaServerDevice.udn, "", this.currentContainerID, 0, 100));
    this.hideQuickSearchPanel();
    void this.router.navigateByUrl('searchResult');
  }
}

