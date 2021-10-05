import { Subject } from 'rxjs';
import { CdsBrowsePathService } from './../util/cds-browse-path.service';
import { Router } from '@angular/router';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { SearchItemService } from './search/search-item.service';
import { DeviceService } from './device.service';
import { HttpService } from './http.service';
import { ContainerItemDto, BrowseRequestDto, MediaServerDto, ContainerDto, SearchRequestDto, SearchResultDto, MusicItemDto } from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ContentDirectoryService {

  baseUri = '/ContentDirectoryService';
  public currentContainerList: ContainerItemDto;
  private customParentID: string;

  // QuickSearch Support
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;

  currentContainerListChanged$: Subject<ContainerItemDto> = new Subject();

  constructor(
    private httpService: HttpService,
    private searchItemService: SearchItemService,
    private dtoGeneratorService: DtoGeneratorService,
    private cdsBrowsePathService: CdsBrowsePathService,

    private router: Router,
    private deviceService: DeviceService) {
    
    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;

    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  // general container excluding special container
  public containerList(filter?: string): ContainerDto[] {
    return this.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer" && this.doFilterText(item.title, filter));
  }

  // special container: playlists
  public playlistList(filter?: string): ContainerDto[] {
    return this.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer" && this.doFilterText(item.title, filter));
  }

  // container with album tags
  public albumList(filter?: string): ContainerDto[] {
    return this.currentContainerList.albumDto.filter(item => this.doFilterText(item.title, filter));
  }

  private doFilterText(title: string, filter?: string) {
    if (!filter) {
      return true;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
  }

  public getMusicTracks(filter?: string): MusicItemDto[] {
    if (this.currentContainerList.musicItemDto?.length) {
      return this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0 && this.doFilterText(item.title, filter));
    }
    return [];
  }

  public containerListWithoutMinimServerTags(filter?: string): ContainerDto[] {
    return this.currentContainerList.containerDto.filter(item => !item.title.startsWith(">> ") && this.doFilterText(item.title, filter));
  }

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList.minimServerSupportTags;
  }

  //
  // --------------------------------------------------------------------------------------------
  //

  mediaServerChanged(data: MediaServerDto): void {
    // Update to root folder of media server
    let oid : string;
    if (localStorage.getItem('lastMediaServerDevice') === data.udn) {
      oid = localStorage.getItem('lastMediaServerPath');
    } else {
      oid = '0';
    }
    this.browseChildrenByRequest(this.createBrowseRequest(oid, "", data.udn));
    localStorage.setItem('lastMediaServerDevice', data.udn);
  }

  public showQuickSearchPanel(): void {
    this.quickSearchPanelVisible = true;
  }


  public hideQuickSearchPanel(): void {
    this.quickSearchPanelVisible = false;
  }

  public clearSearch(): void {
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;
  }

  public gotoParent(): void {
    this.browseChildren(this.getParentTarget(), "", this.deviceService.selectedMediaServerDevice.udn);
  }

  public popCurrentPathAsParent(): void {
    this.setIndividualParentID(this.currentContainerList.currentContainer.id);
  }

  private getParentTarget(): string {
    let targetUDN: string;

    if (this.customParentID) {
      targetUDN = this.customParentID;
      this.customParentID = null;
    } else {
      targetUDN = this.currentContainerList.currentContainer.parentID;
    }
    return targetUDN;
  }

  public setIndividualParentID(parentID: string): void {
    this.customParentID = parentID;
  }

  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn: string): void {
    this.updateBrowsePath(objectID);
    this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseToRoot(sortCriteria: string, mediaServerUdn: string): void {
    this.cdsBrowsePathService.clearPath();
    this.updateBrowsePath("0");
    this.browseChildrenByRequest(this.createBrowseRequest("0", sortCriteria, mediaServerUdn));
  }

  public browseChildrenByContiner(containerDto: ContainerDto): Subject<ContainerItemDto> {
    this.updateBrowsePath(containerDto.id);
    return this.browseChildrenByRequest(this.createBrowseRequest(containerDto.id, "", containerDto.mediaServerUDN));
  }

  private updateBrowsePath(id: string) {
    if (this.cdsBrowsePathService.peekCurrentPathID()?.length < id.length) {
      this.cdsBrowsePathService.stepIn(id);
    } else {
      this.cdsBrowsePathService.stepOut();
    }
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto): Subject<ContainerItemDto> {
    const uri = '/browseChildren';
    const sub = this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto)
    sub.subscribe(data => this.updateContainer(data));
    localStorage.setItem('lastMediaServerPath', browseRequestDto.objectID); 
    return sub;
  }

  updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
    this.currentContainerListChanged$.next(data);
  }

  private createBrowseRequest(objectID: string, sortCriteria: string, mediaServerUdn: string): BrowseRequestDto {
    const br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria
    }

    return br;
  }
  get currentContainerID(): string {
    return this.currentContainerList.currentContainer.id;
  }
  //
  // Search Section
  //
  public quickSearch(searchQuery: string, sortCriteria: string, mediaServerUdn: string): void {
    this.quickSearchByDto(this.dtoGeneratorService.generateQuickSearchDto(searchQuery, mediaServerUdn, sortCriteria, this.currentContainerID));
  }

  public quickSearchByDto(quickSearchDto: SearchRequestDto): void {

    const uri = '/quickSearch';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.quickSearchResultList = data;
    });
  }

  public rescanContent(mediaServerUdn: string): void {
    const uri = '/rescanContent';
    this.httpService.post(this.baseUri, uri, mediaServerUdn).subscribe();
  }

  public searchAllItems(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllItems';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      void this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllPlaylist(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllPlaylist';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      void this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllAlbum(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllAlbum';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      void this.router.navigateByUrl('searchResultContainer');
    });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllArtists';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.searchItemService.musicItemList = data;
      this.clearSearch();
      void this.router.navigateByUrl('searchResultContainer');
    });
  }

}

