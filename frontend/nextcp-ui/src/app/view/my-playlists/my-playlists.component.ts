import { Router } from '@angular/router';
import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { MyPlaylistService } from './my-playlist.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import {
  BrowseCrumb,
  ContentDirectoryService,
} from './../../service/content-directory.service';
import { Component, OnInit, ChangeDetectionStrategy, inject, computed } from '@angular/core';
import { toObservable, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { distinctUntilChanged } from 'rxjs';
import { DisplayContainerComponent } from '../../mediaserver/display-container/display-container.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';

@Component({
  selector: 'my-playlists',
  templateUrl: './my-playlists.component.html',
  styleUrls: ['./my-playlists.component.scss'],
  providers: [ContentDirectoryService],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DisplayContainerComponent, NavBarComponent],
})
/**
 * Sidebar "my playlist" items.
 */
export class MyPlaylistsComponent implements OnInit {
  layoutService = inject(LayoutService);
  private deviceService = inject(DeviceService);
  private router = inject(Router);
  private myPlaylistService = inject(MyPlaylistService);
  contentDirectoryService = inject(ContentDirectoryService);

  private rootPlaylistId = '';

  constructor() {
    // The only place that browses: once on creation and once per change of server or playlist.
    // Before, the constructor subscription and the eager ngOnInit call both browsed the same id.
    // Not an effect(): the browse reads the very signals it writes, which would loop.
    toObservable(
      computed(() => ({
        playlistId: this.myPlaylistService.activePlaylistId,
        udn: this.deviceService.selectedMediaServerDevice().udn,
      })),
    )
      .pipe(
        distinctUntilChanged(
          (a, b) => a.playlistId === b.playlistId && a.udn === b.udn,
        ),
        takeUntilDestroyed(),
      )
      .subscribe(({ playlistId, udn }) =>
        this.browseToMyPlaylist(playlistId, udn),
      );
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
  }

  /**
   * Browses to special MyMusic Folder. TODO: URL should be retrieved from media server (i.e. UMS)
   */
  public browseToMyPlaylist(playlistId: string, mediaServerUdn: string) {
    if (mediaServerUdn.length > 0) {
      this.rootPlaylistId = playlistId;
      // The selected playlist is this view's top level, so the breadcrumb must
      // not present it as a child of something we never showed.
      this.contentDirectoryService.setBrowseRoot(playlistId + '');
      this.contentDirectoryService.browseChildren(
        playlistId + '',
        '',
        mediaServerUdn,
      );
    } else {
      console.log('initial media server -> not selected yet.');
    }
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
      contentDirectoryService: this.contentDirectoryService,
      persistenceService: undefined,
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

  /** Jump to an ancestor picked from the breadcrumb. */
  public crumbPressed(crumb: BrowseCrumb) {
    this.contentDirectoryService.browseChildren(
      crumb.id,
      '',
      this.deviceService.selectedMediaServerDevice().udn,
    );
  }

}
