import { DeviceService } from './device.service';
import { ToastService } from './toast/toast.service';
import { Subject } from 'rxjs';
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
  private TURN_PAGE_AFTER = 1;
  private MAX_REQUEST_ITEMS = 30;
  private PAGED_BROWSE_REQUEST : BrowseRequestDto;
  private turn_page_id : string;

  constructor(
    private httpService: HttpService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private toastService: ToastService
    ) {

    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList.minimServerSupportTags;
  }

  /**
  * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
  */
  public browseToMyPlaylist(playlistId : number, mediaServerUdn: string) {
    this.browseChildren("$DBID$PLAYLIST$" + playlistId, "", mediaServerUdn);
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
    return this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseChildrenByContainer(containerDto: ContainerDto): Subject<ContainerItemDto> {
    return this.browseChildrenByRequest(this.createBrowseRequest(containerDto.id, "", containerDto.mediaServerUDN));
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto, additive? : boolean): Subject<ContainerItemDto> {
    if (this.PAGED_BROWSE_REQUEST && browseRequestDto.objectID != this.PAGED_BROWSE_REQUEST.objectID) {
      this.page = 0;
    }

    this.PAGED_BROWSE_REQUEST = browseRequestDto;
    browseRequestDto.start = this.MAX_REQUEST_ITEMS * this.page;
    browseRequestDto.count = this.MAX_REQUEST_ITEMS;

    const uri = '/browseChildren';
    const sub = this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto)
    if (additive) {
      sub.subscribe(data => this.addContainer(data));
    } else {
      sub.subscribe(data => this.updateContainer(data));
    }
    return sub;
  }

  public browseToNextPage(): Subject<ContainerItemDto> {
    this.page++;
    return this.browseChildrenByRequest(this.PAGED_BROWSE_REQUEST, true);
  }

  public refreshCurrentContainer(): void {
    this.browseChildrenByRequest(this.createBrowseRequest(this.currentContainerID, "", this.deviceService.selectedMediaServerDevice.udn));
  }  

  /**
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
    this.updatePageTurnId(data);
    this.containerList_ = this.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
    this.playlistList_ = this.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
    this.musicTracks_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0);
    this.otherItems_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0);
    this.browseFinished$.next(data);
  }

  public addContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
    this.updatePageTurnId(data);
    this.containerList_ = this.containerList_.concat(this.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer"));
    this.playlistList_ = this.playlistList_.concat(this.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer"));
    this.musicTracks_ = this.musicTracks_.concat(this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0));
    this.otherItems_ = this.otherItems_.concat(this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0));
    this.browseFinished$.next(data);
  }

  public getPageTurnId() : string {
    return this.turn_page_id;
  }

  private updatePageTurnId(data: ContainerItemDto) : string{
    let idxObj : number;
    idxObj = this.TURN_PAGE_AFTER - data.albumDto.length;
    if (idxObj <= 0) {
      this.turn_page_id = data.albumDto[data.albumDto.length + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.containerDto.length;
    if (idxObj <= 0) {
      this.turn_page_id = data.containerDto[data.containerDto.length + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.musicItemDto.length;
    if (idxObj <= 0) {
      this.turn_page_id = data.musicItemDto[data.musicItemDto.length + idxObj - 1].objectID;
      return;
    }

    this.turn_page_id = null;
  }

  private createBrowseRequest(objectID: string, sortCriteria: string, mediaServerUdn: string): BrowseRequestDto {
    const br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria,
      start: 0,
      count: 999
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

  public searchAllPlaylist(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllPlaylist';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResult(data.playlistItems);
    });
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
}

