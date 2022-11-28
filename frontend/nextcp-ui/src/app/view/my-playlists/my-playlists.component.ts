import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { MyPlaylistService } from './my-playlist.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'my-playlists',
  templateUrl: './my-playlists.component.html',
  styleUrls: ['./my-playlists.component.scss'],
  providers: [ContentDirectoryService]
})
export class MyPlaylistsComponent implements OnInit {

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private myPlaylistService: MyPlaylistService,
    public contentDirectoryService: ContentDirectoryService
  ) {
    this.deviceService.mediaServerChanged$.subscribe(server => this.browseToMyPlaylist(this.myPlaylistService.activePlaylistId, server.udn));
  }

  ngOnInit(): void {
    if (this.deviceService.selectedMediaServerDevice.udn) {
      this.browseToMyPlaylist(this.myPlaylistService.activePlaylistId, this.deviceService.selectedMediaServerDevice.udn);
    }
    this.myPlaylistService.activePlaylistId$.subscribe(id => this.browseToMyPlaylist(id, this.deviceService.selectedMediaServerDevice.udn));
  }

  /**
  * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
  */
  public browseToMyPlaylist(playlistId: number, mediaServerUdn: string) {
    this.contentDirectoryService.browseChildren("$DBID$PLAYLIST$" + playlistId, "", mediaServerUdn);
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) {

  }

  itemDeleted(event: MusicItemDto) {
    this.contentDirectoryService.refreshCurrentContainer();
  }

  //
  // Util methods
  //

  hasExtendedApi(): boolean {
    return this.deviceService.selectedMediaServerDeviceHasExtendedApi;
  }

  //
  // bindings
  //

  getContentHandler(): ScrollLoadHandler {
    return { cdsBrowsePathService: null, contentDirectoryService: this.contentDirectoryService, persistenceService: null }
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
