import { ConfigurationService } from './configuration.service';
import { DeviceService } from './device.service';
import { ToastService } from './toast/toast.service';
import { Observable, Subject } from 'rxjs';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { HttpService } from './http.service';
import {
  ContainerItemDto,
  BrowseRequestDto,
  ContainerDto,
  SearchRequestDto,
  SearchResultDto,
  MusicItemDto,
} from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ContentDirectoryService {  
  baseUri = '/ContentDirectoryService';

  private lastBrowseRequest: BrowseRequestDto;

  public currentContainerList: ContainerItemDto;
  public orderAlbumsByGenre = false;

  // result container split by types
  public albumList_: ContainerDto[] = []; // not playlist container
  public containerList_: ContainerDto[] = []; // not playlist container
  public playlistList_: ContainerDto[] = []; // playlist container

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

  // search
  lastSearchObject: SearchRequestDto;
  lastSearchType = '';

  constructor(
    public configService: ConfigurationService,
    private httpService: HttpService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private toastService: ToastService,
  ) {
    // Initialize empty result object
    this.currentContainerList =
      this.dtoGeneratorService.generateEmptyContainerItemDto();
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
  public browseChildren(
    objectID: string,
    sortCriteria: string,
    mediaServerUdn?: string,
  ): Subject<ContainerItemDto> {
    if (!mediaServerUdn) {
      if (!this.deviceService.selectedMediaServerDevice.udn) {
        this.toastService.error('select media server', 'MediaLibrary');
      } else {
        mediaServerUdn = this.deviceService.selectedMediaServerDevice.udn;
      }
    }
    let browseRequestDto = this.createBrowseRequest(
      objectID,
      sortCriteria,
      mediaServerUdn,
    );
    this.setActivePage(browseRequestDto);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public browseChildrenByContainer(
    containerDto: ContainerDto,
    sortCriteria?: string,
  ): Subject<ContainerItemDto> {
    return this.browseChildrenByOID(
      containerDto.id,
      containerDto.mediaServerUDN,
      sortCriteria,
    );
  }

  public browseChildrenByOID(
    oid: string,
    udn: string,
    sortCriteria?: string,
  ): Subject<ContainerItemDto> {
    let browseRequestDto = this.createBrowseRequest(oid, sortCriteria, udn);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public searchCurrentContainer(searchStr: string): Subject<ContainerItemDto> {
    // At this time, we filter the content by posting a browse request and afterwards a manual filter (backend)
    return this.browseChildrenByRequest(
      this.createBrowseRequest(
        this.currentContainerList.currentContainer.id,
        '',
        this.currentContainerList.currentContainer.mediaServerUDN,
        searchStr,
      ),
    );
  }

  private browseChildrenByRequest(
    browseRequestDto: BrowseRequestDto,
    additive?: boolean,
  ): Subject<ContainerItemDto> {
    if (!additive) {
      this.page = 0;
    }

    this.setActivePage(browseRequestDto);

    const uri = '/browseChildren';
    this.lastBrowseRequest = browseRequestDto;
    const sub = this.httpService.post<ContainerItemDto>(
      this.baseUri,
      uri,
      browseRequestDto,
    );    
    if (additive) {
      console.log("browseChildrenByRequest - additive");
      sub.subscribe((data) => this.addContainer(data));
    } else {
      console.log("browseChildrenByRequest - single");
      sub.subscribe((data) => this.updateContainer(data));
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
    let browseRequestDto = this.createBrowseRequest(
      this.currentContainerID,
      '',
      this.deviceService.selectedMediaServerDevice.udn,
      '',
    );
    this.page = 0;
    this.setActivePage(browseRequestDto);
    this.browseChildrenByRequest(browseRequestDto);
  }

  /**
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    console.log("CDS - updating container with : " + data);
    if (data) {
      this.currentContainerList = data;
      this.updatePageTurnId(data);
      this.albumList_ = data.albumDto;
      this.containerList_ = data.containerDto?.filter(
        (item) => item.objectClass !== 'object.container.playlistContainer',
      );
      this.playlistList_ = data.containerDto?.filter(
        (item) => item.objectClass === 'object.container.playlistContainer',
      );
      this.musicTracks_ = data.musicItemDto?.filter(
        (item) =>
          item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0,
      );
      this.otherItems_ = data.musicItemDto?.filter(
        (item) =>
          item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0,
      );
      this.browseFinished$.next(data);

      const count =
        data.albumDto?.length +
        data.containerDto?.length +
        data.musicItemDto?.length;
      if (count >= this.MAX_REQUEST_ITEMS) {
        this.browseToNextPage();
      } else {
        console.log('CDS - updateContainer : loaded last page.');
      }
    }
  }

  public addContainer(data: ContainerItemDto): void {
    if (data) {
      this.currentContainerList = data;
      const count =
        data.albumDto.length +
        data.containerDto.length +
        data.musicItemDto.length;
      this.updatePageTurnId(data);
      this.albumList_ = this.albumList_.concat(data.albumDto);
      this.containerList_ = this.containerList_.concat(
        data.containerDto.filter(
          (item) => item.objectClass !== 'object.container.playlistContainer',
        ),
      );
      this.playlistList_ = this.playlistList_.concat(
        data.containerDto.filter(
          (item) => item.objectClass === 'object.container.playlistContainer',
        ),
      );
      this.musicTracks_ = this.musicTracks_.concat(
        data.musicItemDto.filter(
          (item) =>
            item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0,
        ),
      );
      this.otherItems_ = this.otherItems_.concat(
        data.musicItemDto.filter(
          (item) =>
            item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0,
        ),
      );

      this.browseFinished$.next(data);

      if (count >= this.MAX_REQUEST_ITEMS) {
        this.browseToNextPage();
      } else {
        console.log('addContainer: loaded last page.');
      }
    }
  }

  public getPageTurnId(): string {
    return this.turn_page_id;
  }

  private updatePageTurnId(data: ContainerItemDto): string {
    let idxObj: number;
    let dataArrayLen: number;
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
      this.turn_page_id =
        data.containerDto[data.containerDto.length + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.musicItemDto?.length;
    if (idxObj <= 0) {
      this.turn_page_id =
        data.musicItemDto[data.musicItemDto.length + idxObj - 1].objectID;
      return;
    }

    this.turn_page_id = null;
  }

  private createBrowseRequest(
    objectID: string,
    sortCriteria: string,
    mediaServerUdn: string,
    searchInOID?: string,
  ): BrowseRequestDto {
    const br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria,
      start: 0,
      filter: '*',
      count: 999,
      searchInOID: '',
    };
    return br;
  }

  get currentContainerID(): string {
    return this.currentContainerList.currentContainer.id;
  }
  //
  // Search Section
  // =====================================================================================
  //
  public quickSearch(
    searchQuery: string,
    sortCriteria: string,
    mediaServerUdn: string,
  ): Subject<SearchResultDto> {
    return this.quickSearchByDto(
      this.dtoGeneratorService.generateQuickSearchDto(
        searchQuery,
        mediaServerUdn,
        sortCriteria,
        this.currentContainerID,
      ),
    );
  }

  public quickSearchByDto(
    quickSearchDto: SearchRequestDto,
  ): Subject<SearchResultDto> {
    const uri = '/quickSearch';
    return this.httpService.post<SearchResultDto>(
      this.baseUri,
      uri,
      quickSearchDto,
    );
  }

  public rescanContent(mediaServerUdn: string): void {
    const uri = '/rescanContent';
    this.httpService.post(this.baseUri, uri, mediaServerUdn).subscribe();
  }

  public searchAllItems(quickSearchDto: SearchRequestDto): void {
    console.log("CDS - searchAllItems");
    const uri = '/searchAllItems';
    this.lastSearchObject = quickSearchDto;
    this.lastSearchType = 'songs';
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultItem(data.musicItems);
      });
  }

  public searchAllPlaylist(
    quickSearchDto: SearchRequestDto,
  ): Observable<SearchResultDto> {
    console.log("CDS - searchAllPlaylist");
    const uri = '/searchAllPlaylist';
    this.lastSearchObject = quickSearchDto;
    this.lastSearchType = 'playlists';
    let result = this.httpService.post<SearchResultDto>(
      this.baseUri,
      uri,
      quickSearchDto,
    );
    result.subscribe((data) => {
      this.updateSearchResultContainer(data.playlistItems);
    });
    return result;
  }

  public searchAllAlbum(quickSearchDto: SearchRequestDto): void {
    console.log("CDS - searchAllAlbum");
    const uri = '/searchAllAlbum';
    this.lastSearchObject = quickSearchDto;
    this.lastSearchType = 'album';
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultContainer(data.albumItems);
      });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    console.log("CDS - searchAllArtists");
    const uri = '/searchAllArtists';
    this.lastSearchObject = quickSearchDto;
    this.lastSearchType = 'artists';
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultContainer(data.artistItems);
      });
  }

  private updateSearchResultContainer(searchResultContainer: ContainerDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.containerDto = searchResultContainer;
    ci.currentContainer.parentID = this.lastBrowseRequest.objectID;
    ci.currentContainer.title =
      '[' +
      this.lastSearchType +
      " search] for '" +
      this.lastSearchObject.searchRequest +
      "'";
    ci.currentContainer.albumartUri =
      'data:image/gif;base64,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';
      ci.parentFolderTitle = 'back to music library';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItem(searchResultItems: MusicItemDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.musicItemDto = searchResultItems;
    ci.currentContainer.albumartUri =
      'data:image/gif;base64,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';
    ci.currentContainer.parentID = this.lastBrowseRequest.objectID;
    ci.currentContainer.title =
      '[' +
      this.lastSearchType +
      " search] for '" +
      this.lastSearchObject.searchRequest +
      "'";
    ci.currentContainer.childCount = searchResultItems.length;
    ci.parentFolderTitle = 'back to music library';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  public deleteMusicTrack(item: MusicItemDto) {
    this.musicTracks_ = this.musicTracks_.filter(
      (listitem) => listitem.songId !== item.songId,
    );
  }
}
