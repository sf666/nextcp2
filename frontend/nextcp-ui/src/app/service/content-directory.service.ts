import { DeviceService } from './device.service';
import { ToastService } from './toast/toast.service';
import { PersistenceService } from './persistence/persistence.service';
import { Subject } from 'rxjs';
import { CdsBrowsePathService } from './../util/cds-browse-path.service';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { HttpService } from './http.service';
import { ContainerItemDto, BrowseRequestDto, MediaServerDto, ContainerDto, SearchRequestDto, SearchResultDto, MusicItemDto } from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ContentDirectoryService {

  baseUri = '/ContentDirectoryService';
  public currentContainerList: ContainerItemDto;
  private currentMediaServerDto: MediaServerDto;
  public orderAlbumsByGenre = false;

  // QuickSearch Support
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;

  // result container split by types
  public containerList_: ContainerDto[] = [];  // not playlist container
  public playlistList_: ContainerDto[] = [];   // playlist container

  // item treatment
  public musicTracks_: MusicItemDto[] = [];
  public otherItems_: MusicItemDto[] = [];

  // notfiy other about content change
  browseFinished$: Subject<ContainerItemDto> = new Subject();
  searchFinished$: Subject<ContainerItemDto> = new Subject();

  constructor(
    private httpService: HttpService,
    private dtoGeneratorService: DtoGeneratorService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private deviceService: DeviceService,
    private toastService: ToastService
    ) {

    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;
  }

  public getCurrentMediaServerDto(): MediaServerDto {
    return this.currentMediaServerDto;
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList.minimServerSupportTags;
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
 
  /**
  * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
  */
  public browseToMyPlaylist(playlistId : number) {
    this.browseChildren("$DBID$PLAYLIST$" + playlistId, "");
  }
  
  /**
   * 
   * @param objectID 
   * @param sortCriteria 
   * @param mediaServerUdn 
   */
  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn?: string): Subject<ContainerItemDto> {
    if (!mediaServerUdn) {
      if (!this.currentMediaServerDto?.udn) {
        this.toastService.error("select media server", "MediaLibrary");
      } else {
        mediaServerUdn = this.currentMediaServerDto.udn;
      }
    }
    return this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseChildrenByContiner(containerDto: ContainerDto): Subject<ContainerItemDto> {
    return this.browseChildrenByRequest(this.createBrowseRequest(containerDto.id, "", containerDto.mediaServerUDN));
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto): Subject<ContainerItemDto> {
    const uri = '/browseChildren';
    const sub = this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto)
    sub.subscribe(data => this.updateContainer(data));
    this.cdsBrowsePathService.persistPathToRoot();
    return sub;
  }

  public refreshCurrentContainer(): void {
    this.browseChildrenByRequest(this.createBrowseRequest(this.currentContainerID, "", this.currentMediaServerDto.udn));
  }

  /**
   * 
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
    this.containerList_ = this.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
    this.playlistList_ = this.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
    this.musicTracks_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0);
    this.otherItems_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0);
    this.browseFinished$.next(data);
  }

  private isMusicTrack(item: MusicItemDto) {
    if (item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0) {
      return true;
    }
    
    return false;
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
  // =====================================================================================
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
      this.updateSearchResultItemAndNavigate(data.musicItems);
    });
  }

  public searchAllPlaylist(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllPlaylist';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResultAndNavigate(data.playlistItems);
    });
  }

  public searchAllAlbum(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllAlbum';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResultAndNavigate(data.albumItems);
    });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    const uri = '/searchAllArtists';
    this.httpService.post<SearchResultDto>(this.baseUri, uri, quickSearchDto).subscribe(data => {
      this.updateSearchResultAndNavigate(data.artistItems);
    });
  }

  private updateSearchResultAndNavigate(searchResultContainer: ContainerDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.containerDto = searchResultContainer;
    ci.currentContainer = this.currentContainerList.currentContainer;
    ci.currentContainer.parentID = this.cdsBrowsePathService.peekCurrentPathID();
    this.clearSearch();
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItemAndNavigate(searchResultItems: MusicItemDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.musicItemDto = searchResultItems;
    ci.currentContainer = this.currentContainerList.currentContainer;
    ci.currentContainer.albumartUri = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==';
    ci.currentContainer.childCount = searchResultItems.length;
    ci.currentContainer.artist = '';
    ci.currentContainer.title = '';
    ci.currentContainer.parentID = this.cdsBrowsePathService.peekCurrentPathID();
    this.clearSearch();
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }
}

