import { DeviceService } from 'src/app/service/device.service';
import { SseService } from './../../service/sse/sse.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ConfigurationService } from './../../service/configuration.service';
import { ScrollLoadHandler } from './defs.d';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';
import { TransportService } from 'src/app/service/transport.service';
import { PlaylistService } from './../../service/playlist.service';
import { TrackQualityService } from './../../util/track-quality.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { MyMusicService } from './../../service/my-music.service';
import { MusicItemDto, ContainerDto, ContainerItemDto } from './../../service/dto.d';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { debounce } from 'src/app/global';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { MatInput } from '@angular/material/input';
import { MatFormField, MatLabel, MatPrefix, MatSuffix } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { DomChangedDirective } from '../../directive/watch-dom-tree.directive';

@Component({
    selector: 'mediaServer-display-container',
    templateUrl: './display-container.component.html',
    styleUrls: ['./display-container.component.scss'],
    providers: [{ provide: 'uniqueId', useValue: 'default_display_container' }],
    standalone: true,
    imports: [DomChangedDirective, MatButton, MatIcon, MatFormField, MatLabel, MatPrefix, MatInput, FormsModule, MatSuffix, MatSelect, ReactiveFormsModule, MatOption, QualityBadgeComponent, StarRatingComponent]
})
export class DisplayContainerComponent implements OnInit {

  genresForm = new FormControl('');

  @Input() showTopHeader = true;
  @Input() extendedApi: boolean = true;
  @Input() contentHandler: ScrollLoadHandler;

  // Inform parent about actions
  @Output() containerSelected = new EventEmitter<ContainerDto>();
  @Output() browseFinish = new EventEmitter<ContainerItemDto>();
  @Output() itemDeleted = new EventEmitter<MusicItemDto>();

  private listView = true;
  private lastDiscLabel = '';

  private lastScrollToId = '';
  private intersecObserver: IntersectionObserver;

  quickSearchString: string;
  genresList: Set<String>;
  genresListSorted: Array<String>;
  selectedGenres: Array<string> = [];

  // some calculated constants
  private allTracksSameAlbum_: boolean;
  private oneTrackWithMusicBrainzId_: boolean;
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameDisc_: boolean;

  private currentUrl = '';

  // like member
  currentAlbumLiked = false;
  private currentAlbumReleaseID = "";

  // Filter function for current displayed elements
  // 
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private filteredBrowseToFunc: any;

  constructor(
    private myMusicService: MyMusicService,
    private sseService: SseService,
    private deviceService: DeviceService,
    private timeDisplayService: TimeDisplayService,
    public playlistService: PlaylistService,
    public transportService: TransportService,
    private dtoGeneratorService: DtoGeneratorService,
    private songOptionsServiceService: SongOptionsServiceService,
    private configurationService: ConfigurationService,
    public trackQualityService: TrackQualityService) {
    console.log("constructor : DisplayContainerComponent");
    sseService.mediaRendererTrackInfoChanged$.subscribe(data => {
      if (data?.mediaRendererUdn && data?.currentTrack && deviceService.isMediaRendererSelected(data.mediaRendererUdn)) {
        this.currentUrl = data.currentTrack.streamingURL;
      }
    });
  }

  private partialPageLoaded() {
    this.fillGenres();
  }

  domChange(event: any): void {
    console.log("DOM changed event ... ");
    if (this.contentHandler.cdsBrowsePathService) {
      if (this.contentHandler.cdsBrowsePathService.scrollToID !== this.lastScrollToId) {
        if (this.scrollIntoViewID(this.contentHandler.cdsBrowsePathService.scrollToID)) {
          this.lastScrollToId = this.contentHandler.cdsBrowsePathService.scrollToID;
        }
      }
    }
  }

  /**
   * @param elementID ATTENTION: elementID needs to have tabindex set to '-1': <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID: string): boolean {
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      targetElement.focus();
      console.log("scrolled to element ID : " + elementID);
      return true;
    }
    return false;
  }   

  ngOnInit(): void {
    this.doUiChecks();
    if (this.contentHandler?.contentDirectoryService) {
      this.contentHandler.contentDirectoryService.browseFinished$.subscribe(data => this.partialPageLoaded());
    }
  }

  private doUiChecks(): void {
    this.checkAllTracksSameAlbum();
    this.checkOneTrackWithMusicBrainzId();
    this.checkAllTracksSameDisc();
    this.checkLikeStatus();
    this.checkTilesView();
  }

  private fillGenres(): void {
    this.genresList = new Set();
    this.musicTracks?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split("/");
        aGenre?.forEach((gen) => {
          this.genresList.add(gen.trim());
        })
      }
    });
    this.albumList?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split("/");
        aGenre?.forEach((gen) => {
          this.genresList.add(gen.trim());
        })
      }
    });

    this.genresListSorted = [...this.genresList].sort();
  }

  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }

  //
  // Accessor
  //

  get musicTracks(): MusicItemDto[] {
    return this.contentHandler.contentDirectoryService.musicTracks_;
  }

  get otherItems_() {
    return this.contentHandler.contentDirectoryService.otherItems_;
  }

  get albums(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.albumList_;
  }

  get playlists(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.playlistList_;
  }

  get container(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.containerList_;
  }


  //
  // Initial checks
  // ===============================================================================================

  public allTracksSameDisc(): boolean {
    return this.allTracksSameDisc_;
  }

  public allTracksSameAlbum(): boolean {
    return this.allTracksSameAlbum_;
  }

  private checkAllTracksSameDisc(): void {
    if (this.musicTracks?.length > 0) {
      const firstTrackDisc = this.musicTracks[0]?.numberOfThisDisc;
      this.allTracksSameDisc_ = !this.musicTracks?.find(item => item.numberOfThisDisc !== firstTrackDisc);
    }
    console.log("allTracksSameDisc_ : " + this.allTracksSameDisc_);
  }

  private checkAllTracksSameAlbum(): void {
    const numtrack = this.musicTracks?.length;
    const numMbid = this.musicTracks?.filter(item => item.musicBrainzId?.ReleaseTrackId?.length > 0).length;

    console.log("number of tracs : " + numtrack);
    console.log("number of tracs with mbid: " + numMbid);

    this.allTracksSameMusicBrainzReleaseId_ = false;

    if ((numMbid > 0) && (numtrack == numMbid)) {
      const firstTrackMbid = this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId;
      const numSameMbid = this.musicTracks.filter(item => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid).length;
      this.allTracksSameAlbum_ = numSameMbid == numMbid;
      this.allTracksSameMusicBrainzReleaseId_ = this.allTracksSameAlbum_;
      console.log("number of tracs with same mbid like first track : " + numSameMbid);
    } else {
      if (this.musicTracks?.length > 0) {
        const firstTrackAlbum = this.musicTracks[0].album;
        const albumsWithOtherNames = this.musicTracks.filter(item => item.album !== firstTrackAlbum).length;
        this.allTracksSameAlbum_ = albumsWithOtherNames == 0;
        console.log("number of tracs with other album title : " + albumsWithOtherNames);
      }
    }
    console.log("checkAllTracksSameAlbum : " + this.allTracksSameAlbum_);
    console.log("checkAllTracksSameMusicbrainzReleaseId : " + this.allTracksSameMusicBrainzReleaseId_);
  }

  private checkOneTrackWithMusicBrainzId(): void {
    const mbTrackExists = this.musicTracks?.filter(item => (this.hasSongId(item)))?.length > 0;
    this.oneTrackWithMusicBrainzId_ = mbTrackExists;
    console.log("checkOneTrackWithMusicBrainzId : " + this.oneTrackWithMusicBrainzId_);
  }

  private hasSongId(item: MusicItemDto): boolean {
    return (item.songId?.musicBrainzIdTrackId?.length > 0);
  }

  private checkTilesView() {
    const streaming_exists = this.musicTracks?.filter(item => (item.audioFormat.isStreaming))?.length > 0;
    if (streaming_exists) {
      // we have streaming services. Display as list is not very nice.
      this.listView = false;
    } else {
      this.listView = true;
    }
  } 

  private checkLikeStatus() {
    if (this.allTracksSameMusicBrainzReleaseId_) {
      if (this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId) {
        this.currentAlbumReleaseID = this.musicTracks[0].musicBrainzId.ReleaseTrackId;
        this.myMusicService.isAlbumLiked(this.currentAlbumReleaseID).subscribe(
          res => this.currentAlbumLiked = res
        )
      }
    } else {
      this.currentAlbumLiked = false;
      this.currentAlbumReleaseID = undefined;
    }
  }

  hasSongs(): boolean {
    return this.musicTracks?.length > 0 ? true : false;
  }

  hasGenres(): boolean {
    return this.genresList?.size > 0 ? true : false;
  }

  //
  // Like section
  // ==============================================================================

  isLiked(): boolean {
    return this.currentAlbumLiked;
  }

  likePossible(): boolean {
    if (!this.allTracksSameMusicBrainzReleaseId_) {
      return false;
    }
    if (this.currentAlbumReleaseID) {
      if (!this.currentAlbumLiked) {
        return true;
      }
    }
    return false;
  }

  dislikeAlbum(): void {
    this.myMusicService.deleteAlbumLike(this.currentAlbumReleaseID).subscribe(d => this.checkLikeStatus());
  }

  likeAlbum(): void {
    this.myMusicService.likeAlbum(this.currentAlbumReleaseID).subscribe(d => this.checkLikeStatus());
  }

  //
  // Statistics
  // ===============================================================================================

  public get itemsCount(): number {
    if (this.musicTracks?.length) {
      return this.musicTracks.length;
    } else {
      return 0;
    }
  }

  get containerType(): string {
    if (this.currentContainer.objectClass === "object.container.playlistContainer") {
      return "Playlist";
    } else if (this.currentContainer.objectClass === "object.container.album.musicAlbum") {
      return "Album";
    } else {
      return "Folder";
    }
  }

  get totalPlaytime(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.musicTracks.length > 0) {
      this.musicTracks?.forEach(
        el => completeTime = completeTime + (el.audioFormat?.durationInSeconds ? el.audioFormat.durationInSeconds : 0)
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return "";
  }

  public get currentContainer(): ContainerDto {
    if (this.contentHandler?.contentDirectoryService?.currentContainerList?.currentContainer) {
      return this.contentHandler.contentDirectoryService.currentContainerList.currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  //
  // Button actions
  // ===============================================================================================
  toggleListView(): void {
    this.listView = !this.listView;
    console.log("list view is now : " + this.listView);
  }

  playPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  shufflePlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, true);
  }

  //
  // Search support
  // ===============================================================================================

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.clearSearch();
    }
  }

  public clearSearch(): void {
    this.quickSearchString = '';
  }

  //
  // Sorted container access to albums, playlists or other container 
  // ===============================================================================================

  get allMusicTracks() {
    return this.filteredMusicTracks(true);
  }
  private filteredMusicTracks(filter?: boolean): MusicItemDto[] {
    if (filter) {
      let tracks: Array<MusicItemDto>;
      if (this.quickSearchString) {
        tracks = this.musicTracks.filter(item => this.doFilterText(item.title, this.quickSearchString));
      } else {
        tracks = this.musicTracks;
      }
      if (this?.selectedGenres?.length > 0) {
        tracks = tracks.filter(item => this.doFilterGenre(item));
      }
      return tracks;
    } else {
      return this.musicTracks;
    }
  }

  /**
   * Filter an album by genre
   * @param container album container
   * @returns 
   */
  private doFilterGenreByContainer(container: ContainerDto): boolean {
    let add = false;
    this.selectedGenres?.forEach(genre => {
      if (this.doFilterText(container.genre, genre)) {
        add = true;
      }
    })
    return add;
  }

  private doFilterGenre(item: MusicItemDto): boolean {
    let add = false;
    this.selectedGenres?.forEach(genre => {
      if (this.doFilterText(item.genre, genre)) {
        add = true;
      }
    })
    return add;
  }


  private doFilterText(title: string, filter?: string): boolean {
    if (!filter) {
      return true;
    }
    if (!title && "NONE" == filter) {
      return true;
    } else if (!title) {
      return false;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
  }

  get albumList(): ContainerDto[] {
    return this.filteredAlbumList(true);
  }

  private filteredAlbumList(filter: boolean): ContainerDto[] {
    if (filter) {
      let cont: Array<ContainerDto>;
      if (this.quickSearchString) {
        cont = this.albums.filter(item => this.doFilterText(item.title, this.quickSearchString));
      } else {
        cont = this.albums;
      }
      if (this?.selectedGenres?.length > 0) {
        cont = cont.filter(item => this.doFilterGenreByContainer(item));
      }
      return cont;
    } else {
      console.log("size : " + this.albums.length);
      return this.albums;
    }
  }

  get playlistList(): ContainerDto[] {
    return this.playlistFilter(this.quickSearchString);
  }

  // playlist container
  public playlistFilter(filter?: string): ContainerDto[] {
    if (filter) {
      return this.playlists.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.playlists;
    }
  }

  // other container
  get containerList(): ContainerDto[] {
    return this.containerFilter(this.quickSearchString);
  }

  public containerFilter(filter?: string): ContainerDto[] {
    if (filter) {
      return this.container.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.container;
    }
  }

  get items(): MusicItemDto[] {
    return this.getOtherItemsFilter(this.quickSearchString);
  }

  private getOtherItemsFilter(filter?: string): MusicItemDto[] {
    if (filter) {
      return this.otherItems_.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.otherItems_;
    }
  }

  isListView(): boolean {
    return this.listView;
  }

  // Disc Label support 


  getDiscLabel(item: MusicItemDto): string {
    if (item.numberOfThisDisc !== this.lastDiscLabel) {
      this.lastDiscLabel = item.numberOfThisDisc;
      return `Disk ${item.numberOfThisDisc}`;
    }
    return '';
  }

  newDiscLabel(item: MusicItemDto): boolean {
    if (item.numberOfThisDisc !== this.lastDiscLabel) {
      return true;
    }
    return false;
  }

  public browseToOid(oid: string, udn: string, stepIn: boolean, sortCriteria?: string): Promise<boolean> {
    this.clearSearch();
    if (!this.contentHandler) {
      console.error("contentHandler not initialized.");
      return;
    }

    const promise = new Promise<boolean>((resolve, reject) => {
      if (this.contentHandler.cdsBrowsePathService) {
        if (stepIn) {
          this.contentHandler.cdsBrowsePathService.stepIn(oid);
        } else {
          this.contentHandler.cdsBrowsePathService.stepOut();
        }
      }
      if (this.contentHandler.persistenceService) {
        this.contentHandler.persistenceService.setCurrentObjectID(oid);
      }
      if (this.contentHandler.contentDirectoryService) {
        this.contentHandler.contentDirectoryService.browseChildrenByOID(oid, udn, "").subscribe(data => {
          this.browseFinished(data);
          if (data?.currentContainer?.id) {
            resolve(true);
          } else {
            resolve(false);
          }
        });
      } else {
        console.error("display-container.component: contentDirectoryService not set.");
        reject("display-container.component: contentDirectoryService not set.");
      }
    });

    return promise;
  }

  //
  // Actions (click events)
  // ===============================================================================================

  public browseTo(containerDto: ContainerDto): void {
    this.browseToOid(containerDto.id, containerDto.mediaServerUDN, true, "");
    this.containerSelected.emit(containerDto);
  }

  private browseFinished(data: ContainerItemDto) {
    this.doUiChecks();
    this.browseFinish.emit(data);
  }

  public loadNextBrowsePage() {
    this.contentHandler.contentDirectoryService.browseToNextPage().subscribe();
  }

  private isElementInViewport(el: HTMLElement): boolean {
    if (!el) {
      return false;
    }
    var rect = el.getBoundingClientRect();

    return (
      rect.top >= 0 &&
      rect.left >= 0 &&
      rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /* or $(window).height() */
      rect.right <= (window.innerWidth || document.documentElement.clientWidth) /* or $(window).width() */
    );
  }

  private updateIntersec() {
    let element: HTMLElement;
    let id = this.contentHandler.contentDirectoryService.getPageTurnId();
    element = document.getElementById(id);

    if (this.isElementInViewport(element)) {
      this.loadNextBrowsePage();
    } else {
      var options = {
        threshold: 0.75 //root: document.documentElement,
      };
      if (element) {
        if (this.intersecObserver) {
          this.intersecObserver.disconnect();
        }
        this.intersecObserver = new IntersectionObserver((entries) => {
          entries?.forEach(entry => {
            if (entry.isIntersecting || entry.intersectionRatio > 0) {
              this.loadNextBrowsePage();
            }
          });
        }, options);

        this.intersecObserver.observe(element);
      }
    }
  }

  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  play(musicItemDto: MusicItemDto): void {
    this.transportService.playResource(musicItemDto);
  }

  public selectedRowClass(musicItemDto: MusicItemDto): string {
    if (this.currentUrl && (musicItemDto?.streamingURL == this.currentUrl)) {
      return "selectRow";
    }
    return "";
  }

  //
  // CSS support like responsive grid layout counting in respect to first image visible or not
  // ===============================================================================================
  getTitleResponsiveClass(): string {
    if (!this.allTracksSameAlbum()) {
      return "col-8 col-sm-7 col-md-5 col-lg-5 col-xl-3";
    } else {
      return "col-11 col-sm-9 col-md-7 col-lg-6 col-xl-4";
    }
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateString(item.audioFormat.durationInSeconds);
    } else {
      return "";
    }
  }

  showSongPopup(event: MouseEvent, item: MusicItemDto): void {
    this.songOptionsServiceService.openOptionsDialog(event, item, this.currentContainer).subscribe(result => {
      // handle user action from dialog ...
      if (result) {
        if (result.type == 'add') {
          console.log("added song to playlist : ");
        } else if (result.type == 'delete') {
          console.log("deleted song from playlist : ");
          setTimeout(() => {
            this.itemDeleted.emit(result.data);
          }, 200);
        } else if (result.type == 'download') {
          console.log("download song : ");
        } else if (result.type == 'next') {
          console.log("play next song : ");
        }
      }
    });
  }

  getOtherContainerHeadline(item: ContainerDto): string {
    if (item.objectClass.startsWith("object.container.person")) {
      return "ARTIST";
    } else if (item.objectClass?.startsWith("object.container.playlistContainer")) {
      return "PLAYLIST";
    } else if (item.objectClass?.startsWith("object.container.album")) {
      return "ALBUM";
    } else if (item.objectClass?.startsWith("object.container.genre")) {
      return "GENRE";
    } else if (item.objectClass?.startsWith("object.container.channelGroup")) {
      return "CHANNELS";
    } else if (item.objectClass?.startsWith("object.container.epgContainer")) {
      return "EPG";
    } else if (item.objectClass?.startsWith("object.container.storageSystem")) {
      return "DEVICE";
    } else if (item.objectClass?.startsWith("object.container.storageVolume")) {
      return "DISC";
    } else if (item.objectClass?.startsWith("object.container.storageFolder")) {
      return "";
    } else if (item.objectClass?.startsWith("object.container.bookmarkFolder")) {
      return "BOOKMARKS";
    }
    return "";
  }

  getOtherItemHeadline(item: MusicItemDto): string {
    // object.item.audioItem
    if (item.objectClass?.startsWith("object.item.audioItem")) {
      if (item?.audioFormat?.isStreaming) {
        return "RADIO";
      }
      return "IMAGE";
    } else if (item.objectClass?.startsWith("object.item.imageItem")) {
      return "IMAGE";
    } else if (item.objectClass?.startsWith("object.item.videoItem")) {
      return "VIDEO";
    } else if (item.objectClass?.startsWith("object.item.playlistItem")) {
      return "PLAYLIST";
    } else if (item.objectClass?.startsWith("object.item.textItem")) {
      return "TEXT";
    } else if (item.objectClass?.startsWith("object.item.bookmarkItem")) {
      return "BOOKMARK";
    }
    return "";
  }

  getAlbumTitle() {
    if (this.allMusicTracks.length > 0) {
      return "albumTitle small";
    } else {
      return "albumTitle";
    }
  }

  getAlbumTile() {
    if (this.allMusicTracks.length > 0) {
      return "albumTile small";
    } else {
      return "albumTile";
    }
  }
}
