import { Router } from '@angular/router';
import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { MyPlaylistService } from './my-playlist.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';
import { DisplayContainerComponent } from '../../mediaserver/display-container/display-container.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
  selector: 'my-playlists',
  templateUrl: './my-playlists.component.html',
  styleUrls: ['./my-playlists.component.scss'],
  providers: [ContentDirectoryService],
  standalone: true,
  imports: [DisplayContainerComponent, NavBarComponent],
})
export class MyPlaylistsComponent implements OnInit {
  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private router: Router,
    private myPlaylistService: MyPlaylistService,
    public contentDirectoryService: ContentDirectoryService,
  ) {
    this.deviceService.mediaServerChanged$.subscribe((server) =>
      this.browseToMyPlaylist(
        this.myPlaylistService.activePlaylistId,
        server.udn,
      ),
    );
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
    if (this.deviceService.selectedMediaServerDevice.udn) {
      this.browseToMyPlaylist(
        this.myPlaylistService.activePlaylistId,
        this.deviceService.selectedMediaServerDevice.udn,
      );
    }
    this.myPlaylistService.activePlaylistId$.subscribe((id) =>
      this.browseToMyPlaylist(
        id,
        this.deviceService.selectedMediaServerDevice.udn,
      ),
    );
  }

  /**
   * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
   */
  public browseToMyPlaylist(playlistId: string, mediaServerUdn: string) {
    this.contentDirectoryService.browseChildren(
      playlistId + '',
      '',
      mediaServerUdn,
    );
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) {}

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
    return {
      cdsBrowsePathService: null,
      contentDirectoryService: this.contentDirectoryService,
      persistenceService: null,
    };
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
    return '';
  }

  public backButtonPressed(event: any) {
    // TODO : move to media library
    const currentParent =
      this.contentDirectoryService?.currentContainerList?.currentContainer
        ?.parentID;
    if (currentParent) {
    }
    this.router.navigateByUrl('/music-library/' + currentParent);
  }

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }

  public backButtonDisabled(): boolean {
    if (
      this.contentDirectoryService?.currentContainerList?.currentContainer?.id
    ) {
      return (
        this.contentDirectoryService.currentContainerList.currentContainer
          .id === '0' ||
        this.contentDirectoryService.currentContainerList.currentContainer
          .id === ''
      );
    }
    return false;
  }
}
