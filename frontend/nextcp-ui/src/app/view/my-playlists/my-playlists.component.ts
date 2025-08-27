import { Router } from '@angular/router';
import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { MyPlaylistService } from './my-playlist.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit, computed } from '@angular/core';
import { DisplayContainerComponent } from '../../mediaserver/display-container/display-container.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'my-playlists',
  templateUrl: './my-playlists.component.html',
  styleUrls: ['./my-playlists.component.scss'],
  providers: [ContentDirectoryService],
  standalone: true,
  imports: [DisplayContainerComponent, NavBarComponent],
})
/**
 * Sidebar "my playlist" items.
 */
export class MyPlaylistsComponent implements OnInit {
  private rootPlaylistId = '';

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private router: Router,
    private myPlaylistService: MyPlaylistService,
    public contentDirectoryService: ContentDirectoryService,
  ) {
    toObservable(this.deviceService.selectedMediaServerDevice).subscribe((server) =>
      this.browseToMyPlaylist(
        this.myPlaylistService.activePlaylistId,
        server.udn,
      ),
    );
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
    if (this.deviceService.selectedMediaServerDevice().udn) {
      this.browseToMyPlaylist(
        this.myPlaylistService.activePlaylistId,
        this.deviceService.selectedMediaServerDevice().udn,
      );
    }
    this.myPlaylistService.activePlaylistId$.subscribe((id) =>
      this.browseToMyPlaylist(
        id,
        this.deviceService.selectedMediaServerDevice().udn,
      ),
    );
  }

  /**
   * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
   */
  public browseToMyPlaylist(playlistId: string, mediaServerUdn: string) {
    if (mediaServerUdn.length > 0) {
      this.rootPlaylistId = playlistId;
      this.contentDirectoryService.browseChildren(
        playlistId + '',
        '',
        mediaServerUdn,
      );
    } else {
      console.log("initial media server -> not selected yet.");
    }
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) { }

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
      contentDirectoryService: this.contentDirectoryService,
      persistenceService: null,
    };
  }

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList().currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_();
  }

  otherItems_(): MusicItemDto[] {
    return this.contentDirectoryService.otherItems_();
  }

  albums(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList().albumDto;
  }

  playlists(): ContainerDto[] {
    return this.contentDirectoryService.playlistList_();
  }

  otherContainer(): ContainerDto[] {
    return this.contentDirectoryService.containerList_();
  }

  scrollToID(): string {
    return '';
  }

  public homeButtonPressed(event: any) {
    this.browseToMyPlaylist(
      this.myPlaylistService.activePlaylistId,
      this.deviceService.selectedMediaServerDevice().udn,
    );
  }

  public backButtonPressed(event: any) {
    this.contentDirectoryService.browseToParent("");
  }

  backButtonVisible(): boolean {
    return this.rootPlaylistId != this.contentDirectoryService.currentContainerList().currentContainer.id;
  }

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList().parentFolderTitle;
  }
}
