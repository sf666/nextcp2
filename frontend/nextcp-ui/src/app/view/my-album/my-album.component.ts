import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContainerDto, MusicItemDto, MediaServerDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'myAlbums',
  templateUrl: './my-album.component.html',
  styleUrls: ['./my-album.component.scss'],
  providers: [ContentDirectoryService]
})
export class MyAlbumComponent implements OnInit {

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");
    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    this.loadMyAlbums();
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
    this.loadMyAlbums();
  }

  private loadMyAlbums() {
    const oid = "$DBID$MYMUSIC$";
    if (this.deviceService.selectedMediaServerDevice.udn) {
      this.contentDirectoryService.browseChildren(oid, "", this.deviceService.selectedMediaServerDevice.udn).subscribe();
    }
  }

  hasExtendedApi(): boolean {
    return this.deviceService.selectedMediaServerDeviceHasExtendedApi;
  }

  //
  // Event
  //

  getParentTitle(): string {
    if (this.isMyMusicRoot()) {
      return "";
    }
    return "back to my music";
  }

  public backButtonPressed(event: any) {
    this.loadMyAlbums();
  }


  containerSelected(event: ContainerDto) {
    
  }

  //
  // Util methods
  //
  isMyMusicRoot(): boolean {
    return this.contentDirectoryService.currentContainerID.lastIndexOf("$DBID$MYMUSIC$", 0) === 0;
  }

  //
  // bindings
  //
  getContentHandler(): ScrollLoadHandler {
    return { cdsBrowsePathService: null, contentDirectoryService: this.contentDirectoryService, persistenceService: null }
  }

  backButtonDisabled() {
    return this.isMyMusicRoot();
  }

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_;
  }

  otherItems_(): MusicItemDto[] {
    return this.contentDirectoryService.otherItems_;
  }

  albums(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.albumDto;
  }

  playlists(): ContainerDto[] {
    return this.contentDirectoryService.playlistList_;
  }

  otherContainer(): ContainerDto[] {
    return this.contentDirectoryService.containerList_;
  }

  scrollToID(): string {
    return "";
  }
}
