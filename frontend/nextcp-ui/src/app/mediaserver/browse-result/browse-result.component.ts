import { ScrollViewService } from './../../util/scroll-view.service';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { Component } from '@angular/core';
import { DeviceService } from 'src/app/service/device.service';
import { TrackQualityService } from './../../util/track-quality.service';
import { SongOptionsServiceService } from './../popup/song-options/song-options-service.service';
import { TimeDisplayService } from './../../util/time-display.service';
import { MusicItemDto, ContainerDto } from './../../service/dto.d';
import { PlaylistService } from './../../service/playlist.service';
import { AvtransportService } from './../../service/avtransport.service';
import { ContentDirectoryService } from './../../service/content-directory.service';

import { MinimTagComponent } from './../popup/minim-tag/minim-tag.component';
import { BackgroundImageService } from './../../util/background-image.service';
import { ContainerItemDto } from './../../service/dto.d';
import { AfterViewChecked, ElementRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'media-server-browse-result',
  templateUrl: './browse-result.component.html',
  styleUrls: ['./browse-result.component.scss']
})
export class BrowseResultComponent implements AfterViewChecked {

  private listView: boolean;

  quickSearchString: string;

  constructor(
    private backgroundImageService: BackgroundImageService,
    private dialog: MatDialog,
    private deviceService: DeviceService,
    private cdsBrowsePathService: CdsBrowsePathService,
    public contentDirectoryService: ContentDirectoryService,
    private scrollViewService: ScrollViewService,
    private songOptionsServiceService: SongOptionsServiceService,
    public avtransportService: AvtransportService,
    private timeDisplayService: TimeDisplayService,
    public trackQualityService: TrackQualityService,
    public playlistService: PlaylistService) {
    this.listView = this.allTracksSameAlbum();
  }

  // 
  // Container
  //
  ngAfterViewChecked(): void {
    this.backgroundImageService.setBackgroundImageMainScreen(this.currentContainer.albumartUri);
  }

  // 
  // media items lists
  //
  get containerList(): ContainerDto[] {
    return this.contentDirectoryService.containerList(this.quickSearchString);
  }

  get playlistList(): ContainerDto[] {
    return this.contentDirectoryService.playlistList(this.quickSearchString);
  }

  get albumList(): ContainerDto[] {
    return this.contentDirectoryService.albumList(this.quickSearchString);
  }

  containerListWithoutMinimServerTags(): ContainerDto[] {
    return this.contentDirectoryService.containerListWithoutMinimServerTags(this.quickSearchString);
  }

  get minimTagsList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.minimServerSupportTags;
  }

  get musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.getMusicTracks(this.quickSearchString);
  }

  //
  // filter methods
  // 
  public filterSpecialObjectClass(item: ContainerDto, objCls: string): boolean {
    let result = item.objectClass.lastIndexOf(objCls, 0) === 0;
    if (this.filterContent()) {
      result = result && item.title.toLowerCase().includes(this.quickSearchString);
    }
    return result;
  }

  public filterGeneralContainerClass(item: ContainerDto): boolean {
    if (this.filterContent()) {
      return item.title.toLowerCase().includes(this.quickSearchString);
    }
    return true;
  }

  public filterMusicItems(item: MusicItemDto, objCls: string): boolean {
    let result = item.objectClass.lastIndexOf(objCls, 0) === 0;
    if (this.filterContent()) {
      result = result && item.title.toLowerCase().includes(this.quickSearchString);
    }
    return result;
  }

  public get itemsCount(): number {
    if (this.musicTracks.length) {
      return this.musicTracks.length;
    } else {
      return 0;
    }
  }

  private filterContent(): boolean {
    return this.quickSearchString.length > 0;
  }

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.clearSearch();
    }
  }

  clearSearch(): void {
    this.quickSearchString = '';
  }

  get containerType(): string {
    if (this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.playlistContainer") {
      return "Playlist";
    } else if (this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.album.musicAlbum") {
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

  public get hasChilds(): boolean {
    return this.containerList?.length > 0 || this.playlistList?.length > 0 || this.itemsCount > 0;
  }


  public get currentContainerLabel(): string {
    return this.contentDirectoryService.currentContainerList.currentContainer.title;
  }

  public get currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }

  public get currentObjectList(): ContainerItemDto {
    return this.contentDirectoryService.currentContainerList;
  }

  public browseTo(containerDto: ContainerDto): void {    
    this.contentDirectoryService.browseChildrenByContiner(containerDto).subscribe( () => this.quickSearchString = '');
  }

  shufflePlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, true);
  }

  playPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  hasSongs(): boolean {
    if (this.contentDirectoryService.currentContainerList?.musicItemDto?.length > 0) {
      return true;
    }
    return false;
  }

  get switchViewIcon(): string {
    return "format-list-text"; // view-list
  }

  openMinimTagDialog(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(MinimTagComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }

  //
  // Album 
  //
  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  // Album end

  isRendererSelected(): boolean {
    return this.deviceService.selectedMediaServerDevice.udn.length > 0;
  }

  domChange(_event: Event): void {
    if (this.cdsBrowsePathService.scrollToID.length > 0) {
      this.scrollViewService.scrollIntoViewID(this.cdsBrowsePathService.scrollToID);
    }
  }


  get topDivId(): string {
    return "top-div";
  }

  playAllTracks(): void {
    this.playlistService.addContainerToPlaylistAndPlay(this.contentDirectoryService.currentContainerList.currentContainer, false);
  }

  addAllTracks(): void {
    this.playlistService.addContainerToPlaylist(this.contentDirectoryService.currentContainerList.currentContainer);
  }

  isListView(): boolean {
    return this.listView;
  }

  toggleListView(): void {
    this.listView = !this.listView;
  }

  allTracksSameAlbum(): boolean {
    if (this.musicTracks.length > 0) {
      const firstTrackAlbum = this.musicTracks[0].album;
      return this.musicTracks.filter(item => item.album !== firstTrackAlbum).length == 0;
    }
    return true;
  }

  play(musicItemDto: MusicItemDto): void {
    this.avtransportService.playResource(musicItemDto);
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateString(item.audioFormat.durationInSeconds);
    } else {
      return "";
    }
  }

  showSongPopup(event: Event, item: MusicItemDto): void {
    this.songOptionsServiceService.openOptionsDialog(event, item);
  }
}
