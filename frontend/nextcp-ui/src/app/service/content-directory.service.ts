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
import { computed, Injectable, signal } from '@angular/core';

@Injectable()
export class ContentDirectoryService {
  baseUri = '/ContentDirectoryService';

  // for parent navigation back to last CDS objectId
  private lastBrowseRequest: BrowseRequestDto;

  //
  // signals
  // ==========================================================

  currentContainerList = signal<ContainerItemDto>(this.dtoGeneratorService.generateEmptyContainerItemDto());
  
  isCurrentContainerRoot = computed(() => {
    return this.currentContainerList().currentContainer.id === '0' || 
      this.currentContainerList().currentContainer.parentID === '-1' || 
      this.currentContainerList().currentContainer.id.length == 0;
  });

  isCurrentContainerRootOrHasParentRoot = computed(() => {
    return this.isCurrentContainerRoot() || this.currentContainerList().currentContainer.parentID === '0';
  });

  // result container split by types
  albumList_ = signal<ContainerDto[]>([]);
  containerList_ = signal<ContainerDto[]>([]);
  playlistList_ = signal<ContainerDto[]>([]);

  // item treatment
  musicTracks_ = signal<MusicItemDto[]>([]);
  otherItems_ = signal<MusicItemDto[]>([]);

  // notify other about content change
  browseFinished$: Subject<ContainerItemDto> = new Subject();
  searchFinished$: Subject<ContainerItemDto> = new Subject();

  // to which page was browsed

  private page = 0;
  private TURN_PAGE_AFTER = 60;
  private MAX_REQUEST_ITEMS = 100;
  private PAGED_BROWSE_REQUEST: BrowseRequestDto;
  private turn_page_id: string;

  // search
  lastSearchObject = signal<SearchRequestDto>(this.dtoGeneratorService.generateEmptySearchRequestDto());
  lastSearchType = signal<string>('');

  private id = "id_" + Math.random().toString(16).slice(2);

  constructor(
    public configService: ConfigurationService,
    private httpService: HttpService,
    private dtoGeneratorService: DtoGeneratorService,
    private deviceService: DeviceService,
    private toastService: ToastService,
  ) {
    console.log("[ContentDirectoryService-" + this.id +"] : constructor call");
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

  public browseToParent(
    sortCriteria: string,
    mediaServerUdn?: string,
  ): Subject<ContainerItemDto> {    
    if (!this.isCurrentContainerRoot()) {
      return this.browseChildren(this.currentContainerList().currentContainer.parentID, sortCriteria, mediaServerUdn);
    }
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

    if (browseRequestDto.mediaServerUDN?.length < 1) {
      console.log(this.id + "UDN not set. Stop browsing.");
      return;
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
      console.log(this.id + " : browseChildrenByRequest - additive");
      sub.subscribe((data) => this.addContainer(data));
    } else {
      console.log(this.id + " : browseChildrenByRequest - single");
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
//    console.log("CDS " + this.id + " : updating container with " + data.musicItemDto.length + " items.");
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
        console.log('CDS ' + this.id + ' : updateContainer loaded last page.');
      }
    } else {
      console.log('CDS ' + this.id + " : no search result was provided.");
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
        console.log(this.id + ' : addContainer loaded last page.');
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
    console.log(this.id + " : do quick search");
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
    console.log("CDS " + this.id + " : searchAllItems");
    const uri = '/searchAllItems';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('songs');
    console.log(this.id + "performing search for all matching items ...");
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe({
        next: (data) => {
          console.log("received " + data.musicItems.length + " items. Updating search result ...");
          this.updateSearchResultItem(data.musicItems);
        },
        error: (error: any) => {
          console.error(this.id + 'searchAllItems error : ', error);
        }
      });
  }

  public searchAllPlaylist(
    quickSearchDto: SearchRequestDto,
  ): Observable<SearchResultDto> {
    console.log("CDS " + this.id + " : searchAllPlaylist");
    const uri = '/searchAllPlaylist';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('playlists');
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
    console.log("CDS " + this.id + " : searchAllAlbum");
    const uri = '/searchAllAlbum';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('album');
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultContainer(data.albumItems);
      });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    console.log("CDS " + this.id + " : searchAllArtists");
    const uri = '/searchAllArtists';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('artists');
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
    ci.currentContainer.title = this.lastSearchType() + " matching '" +
      this.lastSearchObject().searchRequest + "'";
    ci.currentContainer.id = 'search_result';
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
    ci.currentContainer.id = 'search_result';
    ci.currentContainer.title =
      this.lastSearchType() +
      " matching '" +
      this.lastSearchObject().searchRequest +
      "'";
    ci.currentContainer.childCount = searchResultItems.length;
    ci.parentFolderTitle = 'back to music library';
    console.log(this.id + " : updating current container with search result ...");
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  public deleteMusicTrack(item: MusicItemDto) {
    this.musicTracks_.update(v => v.filter((listitem) => listitem.songId !== item.songId));
  }
}
