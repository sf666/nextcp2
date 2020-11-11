import { CdsBrowsePathService } from './../util/cds-browse-path.service';
import { Router } from '@angular/router';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { SearchItemService } from './search/search-item.service';
import { DeviceService } from './device.service';
import { HttpService } from './http.service';
import { ContainerItemDto, BrowseRequestDto, MediaServerDto, ContainerDto, QuickSearchRequestDto, QuickSearchResultDto } from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ContentDirectoryService {

  baseUri = '/ContentDirectoryService';
  public currentContainerList: ContainerItemDto;

  // QuickSearch Support
  public quickSearchResultList: QuickSearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;


  constructor(
    private httpService: HttpService,
    private searchItemService: SearchItemService,
    private dtoGeneratorService: DtoGeneratorService,
    private cdsBrowsePathService: CdsBrowsePathService,

    private router: Router,
    private deviceService: DeviceService) {

    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptyQuickSearchResultDto();
    this.quickSearchPanelVisible = false;

    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    // Update to root folder of media server
    this.browseChildrenByRequest(this.createBrowseRequest("0", "", data.udn));
  }

  public clearSearch() {
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptyQuickSearchResultDto();
    this.quickSearchPanelVisible = false;
  }

  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn: string): void {
    this.updateBrowsePath(objectID);
    this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseChildrenByContiner(containerDto: ContainerDto): void {
    this.updateBrowsePath(containerDto.id);
    this.browseChildrenByRequest(this.createBrowseRequest(containerDto.id, "", containerDto.mediaServerUDN));
  }

  private updateBrowsePath(id: string) {
    if (this.cdsBrowsePathService.peekCurrentPathID().length < id.length) {
      this.cdsBrowsePathService.stepIn(id);
    } else {
      this.cdsBrowsePathService.stepOut();
    }
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto): void {
    const uri = '/browseChildren';
    this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto).subscribe(data => this.updateContainer(data));
  }

  updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
  }

  private createBrowseRequest(objectID: string, sortCriteria: string, mediaServerUdn: string): BrowseRequestDto {
    let br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria
    }

    return br;
  }

  //
  // Search Section
  //
  public quickSearch(searchQuery: string, sortCriteria: string, mediaServerUdn: string): void {
    this.quickSearchByDto(this.dtoGeneratorService.generateQuickSearchDto(searchQuery, mediaServerUdn, sortCriteria));
  }

  public quickSearchByDto(quickSearchDto: QuickSearchRequestDto): void {
    const uri = '/quickSearch';
    this.httpService.post<QuickSearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => this.quickSearchResultList = data);
  }

  public searchAllItems(quickSearchDto: QuickSearchRequestDto): void {
    const uri = '/searchAllItems';
    this.httpService.post<QuickSearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllPlaylist(quickSearchDto: QuickSearchRequestDto): void {
    const uri = '/searchAllPlaylist';
    this.httpService.post<QuickSearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllAlbum(quickSearchDto: QuickSearchRequestDto): void {
    const uri = '/searchAllAlbum';
    this.httpService.post<QuickSearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllArtists(quickSearchDto: QuickSearchRequestDto): void {
    const uri = '/searchAllArtists';
    this.httpService.post<QuickSearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      this.router.navigateByUrl('searchResultContainer');
    });
  }

}

