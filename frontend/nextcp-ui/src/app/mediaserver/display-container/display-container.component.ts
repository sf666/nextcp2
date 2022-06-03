import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';
import { AvtransportService } from 'src/app/service/avtransport.service';
import { PlaylistService } from './../../service/playlist.service';
import { TrackQualityService } from './../../util/track-quality.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { MyMusicService } from './../../service/my-music.service';
import { MusicItemDto, ContainerDto } from './../../service/dto.d';
import { Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'mediaServer-display-container',
  templateUrl: './display-container.component.html',
  styleUrls: ['./display-container.component.scss']
})
export class DisplayContainerComponent implements OnInit, OnChanges {

  @Input() showTopHeader = true;

  // Songs & Albums & Items to display
  @Input() currentContainer: ContainerDto;
  
  @Input() musicTracks: MusicItemDto[] = [];
  @Input() otherItems_: MusicItemDto[] = [];

  @Input() albums: ContainerDto[];
  @Input() playlists: ContainerDto[];
  @Input() otherContainer: ContainerDto[];

  @Input() scrollToID: string;
  @Input() extendedApi: boolean = true;

  // Inform parent about actions
  @Output() containerSelected = new EventEmitter<ContainerDto>();

  private listView = true;
  private lastDiscLabel = '';

  quickSearchString: string;

  // some calculated constants
  private allTracksSameAlbum_: boolean;
  private oneTrackWithMusicBrainzId_: boolean;
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameDisc_: boolean;

  // like member
  currentAlbumLiked = false;
  private currentAlbumReleaseID = "";


  constructor(
    private myMusicService: MyMusicService,
    private timeDisplayService: TimeDisplayService,
    public playlistService: PlaylistService,
    public avtransportService: AvtransportService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private songOptionsServiceService: SongOptionsServiceService,
    public trackQualityService: TrackQualityService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.init();
  }

  ngOnInit(): void {
    this.init();
    if (this.scrollToID) {
      this.cdsBrowsePathService.scrollIntoViewID(this.scrollToID);
    }    
  }

  private init(): void {
    this.checkAllTracksSameAlbum();
    this.checkOneTrackWithMusicBrainzId();
    this.checkAllTracksSameDisc();
    this.checkLikeStatus();
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
    if (this.musicTracks.length > 0) {
      const firstTrackDisc = this.musicTracks[0].numberOfThisDisc;
      this.allTracksSameDisc_ = !this.musicTracks.find(item => item.numberOfThisDisc !== firstTrackDisc);
    }
    console.log("allTracksSameDisc_ : " + this.allTracksSameDisc_);
  }

  private checkAllTracksSameAlbum(): void {
    console.log("checkAllTracksSameAlbum ..." + this.allTracksSameAlbum_);
    const numtrack = this.musicTracks.length;
    const numMbid = this.musicTracks.filter(item => item.musicBrainzId?.ReleaseTrackId?.length > 0).length;

    console.log("number of tracs : " + numtrack);
    console.log("number of tracs with mbid: " + numMbid);

    if ((numMbid > 0) && (numtrack == numMbid)) {
      const firstTrackMbid = this.musicTracks[0].musicBrainzId?.ReleaseTrackId;
      const numSameMbid = this.musicTracks.filter(item => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid).length;
      this.allTracksSameAlbum_ = numSameMbid == numMbid;
      console.log("number of tracs with same mbid like first track : " + numSameMbid);
    } else {
      if (this.musicTracks.length > 0) {
        const firstTrackAlbum = this.musicTracks[0].album;
        const albumsWithOtherNames = this.musicTracks.filter(item => item.album !== firstTrackAlbum).length;
        this.allTracksSameAlbum_ = albumsWithOtherNames == 0;
        console.log("number of tracs with other album title : " + albumsWithOtherNames);
      }
    }
    console.log("checkAllTracksSameAlbum : " + this.allTracksSameAlbum_);
  }

  private checkOneTrackWithMusicBrainzId(): void {
    const mbTrackExists = this.musicTracks.filter(item => (this.hasSongId(item)))?.length > 0;
    this.oneTrackWithMusicBrainzId_ = mbTrackExists;
    console.log("checkOneTrackWithMusicBrainzId : " + this.oneTrackWithMusicBrainzId_);
  }

  private hasSongId(item: MusicItemDto): boolean {
    return (item.songId?.musicBrainzIdTrackId?.length > 0) || (item.songId?.umsAudiotrackId != null);
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
    if (this.musicTracks.length > 0) {
      return true;
    }
    return false;
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
    if (this.musicTracks.length) {
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
      this.musicTracks.forEach(
        el => completeTime = completeTime + (el.audioFormat?.durationInSeconds ? el.audioFormat.durationInSeconds : 0)
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return "";
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

  clearSearch(): void {
    this.quickSearchString = '';
  }

  //
  // Sorted container access to albums, playlists or other container 
  // ===============================================================================================

  private doFilterText(title: string, filter?: string) {
    if (!filter) {
      return true;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
  }

  get albumList(): ContainerDto[] {
    return this.filteredAlbumList(this.quickSearchString);
  }
  private filteredAlbumList(filter?: string): ContainerDto[] {
    if (filter) {
      return this.albums.filter(item => this.doFilterText(item.title, filter));
    } else {
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
    return this.otherContainerFilter(this.quickSearchString);
  }

  public otherContainerFilter(filter?: string): ContainerDto[] {
    if (filter) {
      return this.otherContainer.filter(item => this.doFilterText(item.title, filter));
    } else {
      return this.otherContainer;
    }
  }

  get items(): MusicItemDto[] {
    return this.getOtherItemsFilter();
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


  //
  // Actions (click events)
  // ===============================================================================================

  public browseTo(containerDto: ContainerDto): void {
    this.containerSelected.emit(containerDto);
  }

  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }
  
  play(musicItemDto: MusicItemDto): void {
    this.avtransportService.playResource(musicItemDto);
  }

  //
  // CSS support like responsive grid layout counting in respect to first image visible or not
  // ===============================================================================================
  getTitleResponsiveClass(): string {
    if (!this.allTracksSameAlbum()) {
      return "col-8 col-sm-7 col-md-5 col-lg-4";
    } else {
      return "col-11 col-sm-9 col-md-7 col-lg-7";
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
    this.songOptionsServiceService.openOptionsDialog(event, item);
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
    if (item.objectClass?.startsWith("object.item.imageItem")) {
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

  //
  // Access
  //
  getAllMusicTracks() {
    return this.musicTracks;
  }
}
