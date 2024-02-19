import { ConfigurationService } from './configuration.service';
import { DeviceService } from './device.service';
import { ToastService } from './toast/toast.service';
import { Observable, Subject } from 'rxjs';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { HttpService } from './http.service';
import { ContainerItemDto, BrowseRequestDto, ContainerDto, SearchRequestDto, SearchResultDto, MusicItemDto } from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ContentDirectoryService {


  baseUri = '/ContentDirectoryService';
  public currentContainerList: ContainerItemDto;
  public orderAlbumsByGenre = false;

  // result container split by types
  public albumList_: ContainerDto[] = [];  // not playlist container
  public containerList_: ContainerDto[] = [];  // not playlist container
  public playlistList_: ContainerDto[] = [];   // playlist container

  // item treatment
  public musicTracks_: MusicItemDto[] = [];
  public otherItems_: MusicItemDto[] = [];

  // notfiy other about content change
  browseFinished$: Subject<ContainerItemDto> = new Subject();
  searchFinished$: Subject<ContainerItemDto> = new Subject();

  // to which page was browsed 

  private page = 0;
  private TURN_PAGE_AFTER = 60;
  private MAX_REQUEST_ITEMS = 100;
  private PAGED_BROWSE_REQUEST: BrowseRequestDto;
  private turn_page_id: string;

  constructor(
    public configService: ConfigurationService,
    private httpService: HttpService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private toastService: ToastService
  ) {
    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
    if (configService.applicationConfig.nextPageAfter) {
      this.TURN_PAGE_AFTER = configService.applicationConfig.nextPageAfter;
    }
    if (configService.applicationConfig.itemsPerPage) {
      this.MAX_REQUEST_ITEMS = configService.applicationConfig.itemsPerPage;
    }
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList.minimServerSupportTags;
  }

  /**
   * 
   * @param objectID 
   * @param sortCriteria 
   * @param mediaServerUdn 
   */
  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn?: string): Subject<ContainerItemDto> {
    if (!mediaServerUdn) {
      if (!this.deviceService.selectedMediaServerDevice.udn) {
        this.toastService.error("select media server", "MediaLibrary");
      } else {
        mediaServerUdn = this.deviceService.selectedMediaServerDevice.udn;
      }
    }
    let browseRequestDto = this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn);
    this.setActivePage(browseRequestDto);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public browseChildrenByContainer(containerDto: ContainerDto, sortCriteria?: string): Subject<ContainerItemDto> {
    return this.browseChildrenByOID(containerDto.id, containerDto.mediaServerUDN, sortCriteria);
  }

  public browseChildrenByOID(oid: string, udn: string, sortCriteria?: string): Subject<ContainerItemDto> {
    let browseRequestDto = this.createBrowseRequest(oid, sortCriteria, udn);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public searchCurrentContainer(searchStr: string): Subject<ContainerItemDto> {
    // At this time, we filter the content by posting a browse request and afterwards a manual filter (backend)
    return this.browseChildrenByRequest(this.createBrowseRequest(this.currentContainerList.currentContainer.id, "", this.currentContainerList.currentContainer.mediaServerUDN, searchStr));
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto, additive?: boolean): Subject<ContainerItemDto> {
    if (!additive) {
      this.page = 0;
    }

    this.setActivePage(browseRequestDto);

    const uri = '/browseChildren';
    const sub = this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto);
    if (additive) {
      sub.subscribe(data => this.addContainer(data));
    } else {
      sub.subscribe(data => this.updateContainer(data));
    }
    return sub;
  }

  private setActivePage(browseRequestDto: BrowseRequestDto) {
    this.PAGED_BROWSE_REQUEST = browseRequestDto;
    browseRequestDto.start = this.MAX_REQUEST_ITEMS * this.page;
    browseRequestDto.count = this.MAX_REQUEST_ITEMS;
  }

  public browseToNextPage(): Subject<ContainerItemDto> {
    this.page++;
    return this.browseChildrenByRequest(this.PAGED_BROWSE_REQUEST, true);
  }

  public refreshCurrentContainer(): void {
    let browseRequestDto = this.createBrowseRequest(this.currentContainerID, "", this.deviceService.selectedMediaServerDevice.udn, "");
    this.page = 0;
    this.setActivePage(browseRequestDto);
    this.browseChildrenByRequest(browseRequestDto);
  }

  /**
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    if (data) {
      this.currentContainerList = data;
      this.updatePageTurnId(data);
      this.albumList_ = data.albumDto;
      this.containerList_ = data.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
      this.playlistList_ = data.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
      this.musicTracks_ = data.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0);
      this.otherItems_ = data.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0);
      this.browseFinished$.next(data);

      const count = data.albumDto.length + data.containerDto.length + data.musicItemDto.length;
      if (count >= this.MAX_REQUEST_ITEMS) {
        this.browseToNextPage();
      } else {
        console.log("updateContainer : loaded last page.");
      }
    }
  }

  public addContainer(data: ContainerItemDto): void {
    if (data) {
      this.currentContainerList = data;
      const count = data.albumDto.length + data.containerDto.length + data.musicItemDto.length;
      this.updatePageTurnId(data);
      this.albumList_ = this.albumList_.concat(data.albumDto);
      this.containerList_ = this.containerList_.concat(data.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer"));
      this.playlistList_ = this.playlistList_.concat(data.containerDto.filter(item => item.objectClass === "object.container.playlistContainer"));
      this.musicTracks_ = this.musicTracks_.concat(data.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0));
      this.otherItems_ = this.otherItems_.concat(data.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0));

      this.browseFinished$.next(data);

      if (count >= this.MAX_REQUEST_ITEMS) {
        this.browseToNextPage();
      } else {
        console.log("addContainer: loaded last page.");
      }
    }
  }

  public getPageTurnId(): string {
    return this.turn_page_id;
  }

  private updatePageTurnId(data: ContainerItemDto): string {
    let idxObj: number;
    let dataArrayLen : number;
    if (data.albumDto?.length) {
      dataArrayLen = data.albumDto.length;
    } else {
      dataArrayLen = 0;
    }

    idxObj = this.TURN_PAGE_AFTER - dataArrayLen;
    if (idxObj <= 0) {
      this.turn_page_id = data.albumDto[dataArrayLen + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.containerDto?.length;
    if (idxObj <= 0) {
      this.turn_page_id = data.containerDto[data.containerDto.length + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.musicItemDto?.length;
    if (idxObj <= 0) {
      this.turn_page_id = data.musicItemDto[data.musicItemDto.length + idxObj - 1].objectID;
      return;
    }

    this.turn_page_id = null;
  }

  private createBrowseRequest(objectID: string, sortCriteria: string, mediaServerUdn: string, searchInOID?: string): BrowseRequestDto {
    const br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria,
      start: 0,
      filter: "*",
      count: 999,
      searchInOID: ""
    }
    return br;
  }

  get currentContainerID(): string {
    return this.currentContainerList.currentContainer.id;
  }
  //
  // Search Section
  // =====================================================================================
  //
  public quickSearch(searchQuery: string, sortCriteria: string, mediaServerUdn: string): Subject<SearchResultDto> {
    return this.quickSearchByDto(this.dtoGeneratorService.generateQuickSearchDto(searchQuery, mediaServerUdn, sortCriteria, this.currentContainerID));
  }

  public quickSearchByDto(quickSearchDto: SearchRequestDto): Subject<SearchResultDto> {
    const uri = '/quickSearch';
    return this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto);
  }

  public rescanContent(mediaServerUdn: string): void {
    const uri = '/rescanContent';
    this.httpService.post(this.baseUri, uri, mediaServerUdn).subscribe();
  }

  public searchAllItems(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllItems';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResultItem(data.musicItems);
    });
  }

  public searchAllPlaylist(quickSearchDto: SearchRequestDto): Observable<SearchResultDto> {
    const uri = '/searchAllPlaylist';
    let result = this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto);
    result.subscribe(data => {
      this.updateSearchResult(data.playlistItems);
    });
    return result;
  }

  public searchAllAlbum(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllAlbum';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResult(data.albumItems);
    });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllArtists';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResult(data.artistItems);
    });
  }

  private updateSearchResult(searchResultContainer: ContainerDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.containerDto = searchResultContainer;
    ci.currentContainer = this.currentContainerList.currentContainer;
    ci.currentContainer.parentID = '';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItem(searchResultItems: MusicItemDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.musicItemDto = searchResultItems;
    ci.currentContainer = this.currentContainerList.currentContainer;
    ci.currentContainer.albumartUri = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
    ci.currentContainer.childCount = searchResultItems.length;
    ci.currentContainer.artist = '';
    ci.currentContainer.title = '';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  public deleteMusicTrack(item: MusicItemDto) {
    this.musicTracks_ = this.musicTracks_.filter(listitem => listitem.songId !== item.songId);
  }
}

