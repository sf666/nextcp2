import { ToastService } from './toast/toast.service';
import { PersistenceService } from './persistence/persistence.service';
import { Subject } from 'rxjs';
import { CdsBrowsePathService } from './../util/cds-browse-path.service';
import { Router } from '@angular/router';
import { DtoGeneratorService } from './../util/dto-generator.service';
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
  private currentMediaServerDto: MediaServerDto;
  public orderAlbumsByGenre = false;

  private lastOidIsResoredFromCache: boolean;

  // QuickSearch Support
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;

  // some calculated constants
  private allTracksSameAlbum_: boolean;
  private oneTrackWithMusicBrainzId_: boolean;
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameDisc_: boolean;

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
    private persistenceService: PersistenceService,
    private dtoGeneratorService: DtoGeneratorService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private toastService: ToastService,
    private deviceService: DeviceService) {
    // Initialize empty result object
    this.currentContainerList = this.dtoGeneratorService.generateEmptyContainerItemDto();
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;

    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  public getCurrentMediaServerDto(): MediaServerDto {
    return this.currentMediaServerDto;
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  // container without playlists
  public containerList(filter?: string): ContainerDto[] {
    if (filter) {
      return this.containerList_.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.containerList_;
    }
  }

  // playlist container
  public playlistList(filter?: string): ContainerDto[] {
    if (filter) {
      return this.playlistList_.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.playlistList_;
    }
  }

  // container with album tags
  public albumList(filter?: string): ContainerDto[] {
    if (filter) {
      return this.currentContainerList.albumDto.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.currentContainerList.albumDto;
    }
  }

  private doFilterText(title: string, filter?: string) {
    if (!filter) {
      return true;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
  }

  public getMusicTracks(filter?: string): MusicItemDto[] {
    if (filter) {
      return this.musicTracks_.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.musicTracks_;
    }
  }

  public getItems(filter?: string): MusicItemDto[] {
    if (filter) {
      return this.otherItems_.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.otherItems_;
    }
  }

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList.minimServerSupportTags;
  }

  //
  // --------------------------------------------------------------------------------------------
  //

  mediaServerChanged(data: MediaServerDto): void {
    // Update to root folder of media server
    let oid: string;
    if (this.persistenceService.isCurrentMediaServer(data.udn)) {
      oid = this.persistenceService.getLastMediaServerPath();
      this.cdsBrowsePathService.restorePathToRoot();
      this.lastOidIsResoredFromCache = true;
    } else {
      this.lastOidIsResoredFromCache = false;
      oid = '0';
      this.cdsBrowsePathService.clearPath();
      this.persistenceService.setNewMediaServerDevice(data.udn);
    }
    this.currentMediaServerDto = data;
    this.browseChildrenByRequest(this.createBrowseRequest(oid, "", data.udn));
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
    this.browseChildren(this.getParentTarget(), "", this.deviceService.selectedMediaServerDevice.udn, true);
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
      console.log("Parent ID : " + this.currentContainerList.currentContainer.parentID);
      targetUDN = this.currentContainerList.currentContainer.parentID;
    }
    return targetUDN;
  }

  public setIndividualParentID(parentID: string): void {
    this.customParentID = parentID;
  }

  /**
   * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
   */
  public browseToMyMusic() {
    this.browseChildren("$DBID$MYMUSIC$","");
  }

  /**
   * 
   * @param objectID 
   * @param sortCriteria 
   * @param mediaServerUdn 
   */
  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn?: string, isStepOut?: boolean): void {
    if (!mediaServerUdn) {
      if (!this.currentMediaServerDto?.udn) {
        this.toastService.error("select media server", "MediaLibrary");
      } else {
        mediaServerUdn = this.currentMediaServerDto.udn;
      }
    }
    this.updateBrowsePath(objectID, isStepOut);
    this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseToRoot(sortCriteria: string): void {
    this.cdsBrowsePathService.clearPath();
    this.updateBrowsePath("0");
    this.browseChildrenByRequest(this.createBrowseRequest("0", sortCriteria, this.currentMediaServerDto.udn));
  }

  public browseChildrenByContiner(containerDto: ContainerDto): Subject<ContainerItemDto> {
    this.updateBrowsePath(containerDto.id);
    return this.browseChildrenByRequest(this.createBrowseRequest(containerDto.id, "", containerDto.mediaServerUDN));
  }

  private updateBrowsePath(id: string, isStepOut?: boolean) {
    if (isStepOut) {
      this.cdsBrowsePathService.stepOut();
    } else {
      this.cdsBrowsePathService.stepIn(id);
    }
  }

  private browseChildrenByRequest(browseRequestDto: BrowseRequestDto): Subject<ContainerItemDto> {
    const uri = '/browseChildren';
    const sub = this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto)
    sub.subscribe(data => this.updateContainer(data));
    this.persistenceService.setCurrentObjectID(browseRequestDto.objectID);
    this.cdsBrowsePathService.persistPathToRoot();
    return sub;
  }

  /**
   * 
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
    if (this.lastOidIsResoredFromCache && !(data.containerDto.length > 0 || data.musicItemDto.length > 0)) {
      this.browseToRoot('');
      this.lastOidIsResoredFromCache = false;
    } else {
      this.containerList_ = this.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
      this.playlistList_ = this.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
      this.musicTracks_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) === 0);
      this.otherItems_ = this.currentContainerList.musicItemDto.filter(item => item.objectClass.lastIndexOf("object.item.audioItem", 0) !== 0);

      this.checkAllTracksSameAlbum();
      this.checkOneTrackWithMusicBrainzId();
      this.checkAllTracksSameMusicBrainzReleaseId();
      this.checkAllTracksSameDisc();
    }
    this.browseFinished$.next(data);
  }

  public allTracksSameAlbum(): boolean {
    return this.allTracksSameAlbum_;
  }

  private checkAllTracksSameAlbum(): void {
    console.log("checkAllTracksSameAlbum ..." + this.allTracksSameAlbum_);
    const numtrack = this.getMusicTracks().length;
    const numMbid = this.getMusicTracks().filter(item => item.musicBrainzId?.ReleaseTrackId?.length > 0).length;

    console.log("number of tracs : " + numtrack);
    console.log("number of tracs with mbid: " + numMbid);

    if ((numMbid > 0) && (numtrack == numMbid)) {
      const firstTrackMbid = this.getMusicTracks()[0].musicBrainzId?.ReleaseTrackId;
      const numSameMbid = this.getMusicTracks().filter(item => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid).length;
      this.allTracksSameAlbum_ = numSameMbid == numMbid;
      console.log("number of tracs with same mbid like first track : " + numSameMbid);
    } else {
      if (this.getMusicTracks().length > 0) {
        const firstTrackAlbum = this.getMusicTracks()[0].album;
        const albumsWithOtherNames = this.getMusicTracks().filter(item => item.album !== firstTrackAlbum).length;
        this.allTracksSameAlbum_ = albumsWithOtherNames == 0;
        console.log("number of tracs with other album title : " + albumsWithOtherNames);
      }
    }
    console.log("checkAllTracksSameAlbum : " + this.allTracksSameAlbum_);
  }

  public oneTrackWithMusicBrainzId(): boolean {
    return this.oneTrackWithMusicBrainzId_;
  }

  private checkOneTrackWithMusicBrainzId(): void {
    const mbTrackExists = this.musicTracks_.filter(item => (item.musicBrainzId?.TrackId?.length > 0))?.length > 0;
    this.oneTrackWithMusicBrainzId_ = mbTrackExists;
    console.log("checkOneTrackWithMusicBrainzId : " + this.oneTrackWithMusicBrainzId_);
  }

  public allTracksSameMusicBrainzReleaseId(): boolean {
    return this.allTracksSameMusicBrainzReleaseId_;
  }

  private checkAllTracksSameMusicBrainzReleaseId(): void {
    if (this.getMusicTracks().length > 0) {
      const firstTrackReleaseID = this.getMusicTracks()[0].musicBrainzId.ReleaseTrackId;
      this.allTracksSameMusicBrainzReleaseId_ = this.getMusicTracks().filter(item => item.musicBrainzId.ReleaseTrackId !== firstTrackReleaseID).length == 0;
    }
    this.allTracksSameMusicBrainzReleaseId_ = true;
    console.log("allTracksSameMusicBrainzReleaseId_ : " + this.allTracksSameMusicBrainzReleaseId_);
  }

  public allTracksSameDisc(): boolean {
    return this.allTracksSameDisc_;
  }

  private checkAllTracksSameDisc(): void {
    if (this.getMusicTracks().length > 0) {
      const firstTrackDisc = this.getMusicTracks()[0].numberOfThisDisc;
      this.allTracksSameDisc_ = !this.getMusicTracks().find(item => item.numberOfThisDisc !== firstTrackDisc);
    }
    console.log("allTracksSameDisc_ : " + this.allTracksSameDisc_);
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

  private updateSearchResultAndNavigate(searchResultContainer : ContainerDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.containerDto = searchResultContainer;
    ci.currentContainer = this.currentContainerList.currentContainer;
    ci.currentContainer.parentID = this.cdsBrowsePathService.peekCurrentPathID();
    this.clearSearch();
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItemAndNavigate(searchResultItems : MusicItemDto[]) {
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

