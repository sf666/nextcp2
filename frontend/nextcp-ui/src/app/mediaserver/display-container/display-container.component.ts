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
import {
  MusicItemDto,
  ContainerDto,
  ContainerItemDto,
} from './../../service/dto.d';
import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { debounce } from 'src/app/global';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { MatOption } from '@angular/material/core';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { MatInput, MatInputModule } from '@angular/material/input';
import {
  MatFormField,
  MatLabel,
  MatPrefix,
  MatSuffix,
} from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DomChangedDirective } from '../../directive/watch-dom-tree.directive';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { ContainerTileComponent } from './container-tile/container-tile.component';
import { DisplayContainerHeaderComponent } from './display-container-header/display-container-header.component';
import { ItemTileComponent } from './item-tile/item-tile.component';

@Component({
  selector: 'mediaServer-display-container',
  templateUrl: './display-container.component.html',
  styleUrls: ['./display-container.component.scss'],
  providers: [{ provide: 'uniqueId', useValue: 'default_display_container' }],
  standalone: true,
  imports: [
    DomChangedDirective,
    MatButtonModule,
    MatIconModule,
    MatFormField,
    MatLabel,
    MatPrefix,
    MatInputModule,
    MatSuffix,
    MatSelectModule,
    ReactiveFormsModule,
    MatOption,
    QualityBadgeComponent,
    StarRatingComponent,
    ContainerTileComponent,
    ItemTileComponent,
    DisplayContainerHeaderComponent,
  ],
})
export class DisplayContainerComponent {

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
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameDisc_: boolean;

  private currentUrl = '';

  // like member
  currentAlbumLiked = false;
  private currentAlbumReleaseID = '';

  // Filter function for current displayed elements
  //
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private filteredBrowseToFunc: any;

  constructor(
    private myMusicService: MyMusicService,
    private backgroundImageService: BackgroundImageService,
    private sseService: SseService,
    private timeDisplayService: TimeDisplayService,
    public playlistService: PlaylistService,
    public transportService: TransportService,
    private dtoGeneratorService: DtoGeneratorService,
    private songOptionsServiceService: SongOptionsServiceService,
    private configurationService: ConfigurationService,
    public trackQualityService: TrackQualityService
  ) {

  }

  domChange(event: any): void {
    console.log('DOM changed event ... ');
    // TODO implement ScrollTo OID
  }

  quickSearchChanged(newQuickSerchText: string) {
    this.quickSearchString = newQuickSerchText;
  }

  /**
   * @param elementID ATTENTION: elementID needs to have tabindex set to '-1': <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID: string): boolean {
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      targetElement.focus();
      console.log('scrolled to element ID : ' + elementID);
      return true;
    }
    return false;
  }

  getSearchDelay(): number {
    const delay =
      this.configurationService.serverConfig?.applicationConfig
        ?.globalSearchDelay != null
        ? this.configurationService.serverConfig.applicationConfig
            ?.globalSearchDelay
        : 600;
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


  private hasSongId(item: MusicItemDto): boolean {
    return item.songId?.musicBrainzIdTrackId?.length > 0;
  }

  private checkTilesView() {
    const streaming_exists =
      this.musicTracks?.filter((item) => item.audioFormat.isStreaming)?.length >
      0;
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
        this.currentAlbumReleaseID =
          this.musicTracks[0].musicBrainzId.ReleaseTrackId;
        this.myMusicService
          .isAlbumLiked(this.currentAlbumReleaseID)
          .subscribe((res) => (this.currentAlbumLiked = res));
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

  getFavoriteCssBtnClass(): string {
    if (this.isLiked()) {
      return "liked";
    } else {
      return "disliked";
    }
  }

  getFavoriteCssIconClass(): string {
    if (this.isLiked()) {
      return "filled";
    } else {
      return "";
    }
  }

  isLiked(): boolean {
    return this.currentAlbumLiked;
  }

  isContainerAlbum(): boolean {
    return this.allTracksSameMusicBrainzReleaseId_;
  }

  likePossible(): boolean {
    return this.allTracksSameMusicBrainzReleaseId_;
  }

  dislikeAlbum(): void {
    this.myMusicService
      .deleteAlbumLike(this.currentAlbumReleaseID)
      .subscribe((d) => this.checkLikeStatus());
  }

  likeAlbum(): void {
    this.myMusicService
      .likeAlbum(this.currentAlbumReleaseID)
      .subscribe((d) => this.checkLikeStatus());
  }

  toggleLikeAlbum(): void {
    if (this.isLiked()) {
      this.dislikeAlbum();
    } else {
      this.likeAlbum();
    }
  }

  //
  // Statistics
  // ===============================================================================================

  public get musicItemsCount(): number {
    if (this.musicTracks?.length) {
      return this.musicTracks.length;
    } else {
      return 0;
    }
  }

  get containerType(): string {
    if (
      this.currentContainer.objectClass === 'object.container.playlistContainer'
    ) {
      return 'Playlist';
    } else if (
      this.currentContainer.objectClass === 'object.container.album.musicAlbum'
    ) {
      return 'Album';
    } else {
      return 'Folder';
    }
  }

  get totalPlaytime(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.musicTracks.length > 0) {
      this.musicTracks?.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0))
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return '';
  }

  get totalPlaytimeShort(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.musicTracks.length > 0) {
      this.musicTracks?.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0))
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateStringShort(completeTime);
    }
    return '';
  }

  public get currentContainer(): ContainerDto {
    if (
      this.contentHandler?.contentDirectoryService?.currentContainerList
        ?.currentContainer
    ) {
      return this.contentHandler.contentDirectoryService.currentContainerList
        .currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  //
  // Button actions
  // ===============================================================================================
  toggleListView(): void {
    this.listView = !this.listView;
    console.log('list view is now : ' + this.listView);
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
        tracks = this.musicTracks.filter((item) =>
          this.doFilterText(item.title, this.quickSearchString)
        );
      } else {
        tracks = this.musicTracks;
      }
      if (this?.selectedGenres?.length > 0) {
        tracks = tracks.filter((item) => this.doFilterGenre(item));
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
    this.selectedGenres?.forEach((genre) => {
      if (this.doFilterText(container.genre, genre)) {
        add = true;
      }
    });
    return add;
  }

  private doFilterGenre(item: MusicItemDto): boolean {
    let add = false;
    this.selectedGenres?.forEach((genre) => {
      if (this.doFilterText(item.genre, genre)) {
        add = true;
      }
    });
    return add;
  }

  private doFilterText(title: string, filter?: string): boolean {
    if (!filter) {
      return true;
    }
    if (!title && 'NONE' == filter) {
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
        cont = this.albums.filter((item) =>
          this.doFilterText(item.title, this.quickSearchString)
        );
      } else {
        cont = this.albums;
      }
      if (this?.selectedGenres?.length > 0) {
        cont = cont.filter((item) => this.doFilterGenreByContainer(item));
      }
      return cont;
    } else {
      console.log('size : ' + this.albums.length);
      return this.albums;
    }
  }

  get playlistList(): ContainerDto[] {
    return this.playlistFilter(this.quickSearchString);
  }

  // playlist container
  public playlistFilter(filter?: string): ContainerDto[] {
    if (filter) {
      return this.playlists.filter((item) =>
        this.doFilterText(item.title, filter)
      );
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
      return this.container.filter((item) =>
        this.doFilterText(item.title, filter)
      );
    } else {
      return this.container;
    }
  }

  get items(): MusicItemDto[] {
    return this.getOtherItemsFilter(this.quickSearchString);
  }

  private getOtherItemsFilter(filter?: string): MusicItemDto[] {
    if (filter) {
      return this.otherItems_.filter((item) =>
        this.doFilterText(item.title, filter)
      );
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

  public browseToOid(
    oid: string,
    udn: string,
    stepIn: boolean,
    sortCriteria?: string
  ): Promise<boolean> {
    this.clearSearch();
    if (!this.contentHandler) {
      console.error('contentHandler not initialized.');
      return;
    }

    const promise = new Promise<boolean>((resolve, reject) => {
      if (this.contentHandler.persistenceService) {
        this.contentHandler.persistenceService.setCurrentObjectID(oid);
      }
      if (this.contentHandler.contentDirectoryService) {
        this.contentHandler.contentDirectoryService
          .browseChildrenByOID(oid, udn, '')
          .subscribe((data) => {
            this.browseFinished(data);
            if (data?.currentContainer?.id) {
              resolve(true);
            } else {
              resolve(false);
            }
          });
      } else {
        console.error(
          'display-container.component: contentDirectoryService not set.'
        );
        reject('display-container.component: contentDirectoryService not set.');
      }
    });

    return promise;
  }

  //
  // Actions (click events)
  // ===============================================================================================

  public browseTo(containerDto: ContainerDto): void {
    this.browseToOid(containerDto.id, containerDto.mediaServerUDN, true, '');
    this.containerSelected.emit(containerDto);
  }

  private browseFinished(data: ContainerItemDto) {
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
      rect.bottom <=
        (window.innerHeight ||
          document.documentElement.clientHeight) /* or $(window).height() */ &&
      rect.right <=
        (window.innerWidth ||
          document.documentElement.clientWidth) /* or $(window).width() */
    );
  }


  addPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylist(container);
  }

  addItemToPlaylist(item: MusicItemDto): void {
    this.playlistService.addToPlaylist(item);
  }

  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  playItem(musicItemDto: MusicItemDto): void {
    this.transportService.playResource(musicItemDto);
  }


  getOtherItemHeadline(item: MusicItemDto): string {
    // object.item.audioItem
    if (item.objectClass?.startsWith('object.item.audioItem')) {
      if (item?.audioFormat?.isStreaming) {
        return 'RADIO';
      }
      return 'IMAGE';
    } else if (item.objectClass?.startsWith('object.item.imageItem')) {
      return 'IMAGE';
    } else if (item.objectClass?.startsWith('object.item.videoItem')) {
      return 'VIDEO';
    } else if (item.objectClass?.startsWith('object.item.playlistItem')) {
      return 'PLAYLIST';
    } else if (item.objectClass?.startsWith('object.item.textItem')) {
      return 'TEXT';
    } else if (item.objectClass?.startsWith('object.item.bookmarkItem')) {
      return 'BOOKMARK';
    }
    return '';
  }

  getAlbumTitle() {
    if (this.allMusicTracks.length > 0) {
      return 'albumTitle small';
    } else {
      return 'albumTitle';
    }
  }

  getAlbumTile() {
    if (this.allMusicTracks.length > 0) {
      return 'albumTile small';
    } else {
      return 'albumTile';
    }
  }
}
