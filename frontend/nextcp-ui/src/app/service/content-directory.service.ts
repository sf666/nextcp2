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
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ContentDirectoryService {
  baseUri = '/ContentDirectoryService';

  // for parent navigation back to last CDS objectId
  private lastBrowseRequest: BrowseRequestDto;

  //
  // signals
  // ==========================================================

  currentContainerList = signal<ContainerItemDto>(this.dtoGeneratorService.generateEmptyContainerItemDto());

  // result container split by types
  albumList_ = signal<ContainerDto[]>([]);
  containerList_ = signal<ContainerDto[]>([]);
  playlistList_ = signal<ContainerDto[]>([]);

  // item treatment
  musicTracks_ = signal<MusicItemDto[]>([]);
  otherItems_ = signal<MusicItemDto[]>([]);

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
    return this.currentContainerList().minimServerSupportTags;
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
      if (!this.deviceService.selectedMediaServerDevice().udn) {
        this.toastService.error('select media server', 'MediaLibrary');
      } else {
        mediaServerUdn = this.deviceService.selectedMediaServerDevice().udn;
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
        this.currentContainerList().currentContainer.id,
        '',
        this.currentContainerList().currentContainer.mediaServerUDN,
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
      this.deviceService.selectedMediaServerDevice().udn,
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
      this.currentContainerList.set(data);
      this.updatePageTurnId(data);
      this.albumList_.set(data.albumDto);
      this.containerList_.set(data.containerDto?.filter(
        (item) => item.objectClass !== 'object.container.playlistContainer',
      ));
      this.playlistList_.set(data.containerDto?.filter(
        (item) => item.objectClass === 'object.container.playlistContainer',
      ));
      this.musicTracks_.set(data.musicItemDto?.filter(
        (item) =>
          item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0,
      ));
      this.otherItems_.set(data.musicItemDto?.filter(
        (item) =>
          item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0,
      ));
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

  /**
   * 
   * @param data Adding new data to existing array.
   */
  public addContainer(data: ContainerItemDto): void {
    if (data) {
      this.currentContainerList.set(data);
      const count =
        data.albumDto.length +
        data.containerDto.length +
        data.musicItemDto.length;
      this.updatePageTurnId(data);

      this.albumList_.update(v => { return [...v].concat(data.albumDto) });

      this.containerList_.update(v => {
        return v.concat(data.containerDto.filter((item) => item.objectClass !== 'object.container.playlistContainer'))
      });

      this.playlistList_.update(v => {
        return v.concat(data.containerDto.filter((item) => item.objectClass === 'object.container.playlistContainer'))
      });

      this.musicTracks_.update(v => {
        return v.concat(data.musicItemDto.filter((item) => item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0))
      });

      this.otherItems_.update(v => {
        return v.concat(data.musicItemDto.filter((item) => item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0))
      });

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
    return this.currentContainerList().currentContainer.id;
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
    ci.currentContainer.parentID = this.lastBrowseRequest?.objectID !== undefined ? this.lastBrowseRequest.objectID : "0";
    ci.currentContainer.title =
      '[' +
      this.lastSearchType +
      " search] for '" +
      this.lastSearchObject.searchRequest +
      "'";
    ci.currentContainer.albumartUri = '/assets/images/search-icon.png';
    ci.parentFolderTitle = 'back to music library';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItem(searchResultItems: MusicItemDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.musicItemDto = searchResultItems;
    ci.currentContainer.albumartUri = '/assets/images/search-icon.png';
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
    this.musicTracks_.update(v => v.filter((listitem) => listitem.songId !== item.songId));
  }
}
