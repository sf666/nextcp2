import { Router } from '@angular/router';
import { ConfigurationService } from './../configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ContentDirectoryService } from './../content-directory.service';
import { SearchResultDto } from './../dto.d';
import { Injectable } from '@angular/core';
import { debounce } from 'src/app/global';


@Injectable({
  providedIn: 'root'
})
export class GlobalSearchService {

  // QuickSearch Support (Global search) 
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;

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
    this.quickSearchPanelVisible = false;
    this.doSearchFunc = debounce(this.getSearchDelay(), this.doSearchThrotteled);
  }


  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }

  public showQuickSearchPanel(): void {
    this.quickSearchPanelVisible = true;
  }

  public hideQuickSearchPanel(): void {
    this.quickSearchPanelVisible = false;
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
      this.quickSearchPanelVisible = false;
    } else {
      if (value && value.length > 2) {
        this.quickSearchPanelVisible = true;
        this.currentSearchText = value;
        this.doSearch();
      }
    }
  }

  clearSearch(): void {
    this.quickSearchString = "";
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;
  }

  public backToLastSearch() {
    if (this.lastSearch) {
      this.lastSearch();
    }
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
    this.lastSearch = this.showAllPlaylist;
    void this.router.navigateByUrl('searchResult');
  }
}
