import { MediaPlayerService } from 'src/app/service/media-player/media-player.service';
import { ConfigurationService } from './../../service/configuration.service';
import { LayoutService } from './../../service/layout.service';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { MyPlaylistService } from './../my-playlists/my-playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { ServerPlaylistDto } from 'src/app/service/dto';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatAnchor } from '@angular/material/button';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { DefaultPlaylistService } from 'src/app/mediaserver/popup/defaut-playlists/default-playlist.service';
import { MusicLibraryService } from 'src/app/service/music-library/music-library.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatButton,
    RouterLink,
    MatIcon,
    MatAnchor,
  ],
})
export class SidebarComponent {

  private routerMap = new Map<string, string>();

  activeId = signal<string>("");

  constructor(
    public deviceService: DeviceService,
    public playlistService: ServerPlaylistService,
    private myPlaylistService: MyPlaylistService,
    private router: Router,
    public musicLibraryService: MusicLibraryService,
    public layoutService: LayoutService,
    private defaultPlaylistService: DefaultPlaylistService,
    private configurationService: ConfigurationService,
    public mediaPlayerService: MediaPlayerService,
    public rendererService: RendererService) {

    this.routerMap.set("/", "-1");                // default route is music-library
    this.routerMap.set("/music-library", "-1");
    this.routerMap.set("/playlist", "-2");
    this.routerMap.set("/player", "-3");
    this.routerMap.set("/radio", "-4");
    this.routerMap.set("/myAlbums", "-5");
    this.routerMap.set("/settings", "-6");
    this.routerMap.set("/mediaPlayerConfig", "-7");
    this.routerMap.set("/ums-audioaddict", "-8");
    this.routerMap.set("/myPlaylists", "0");      // All positives ID's are playlists

    this.routerMap.set("/myTracks", "-999");      // not used yet
    /*
        this.routerMap.set("/networks", "-8");
        this.routerMap.set("/getNetworkChannels", "-8");
    */
    router.events.subscribe(event => this.calActiveId(event));
  }

  private calActiveId(nav: any) {
    if (nav instanceof NavigationEnd) {
      let url = nav.url;
      if (url.lastIndexOf("/") > 0) {
        url = url.substring(0, url.lastIndexOf("/"));
      }
      var targetId = this.routerMap.get(url);
      this.activeId.set(targetId);
      if (this.activeId() == "0") {
        this.activeId.set(this.myPlaylistService.activePlaylistId);
      }
    }
  }

  /**
   * Browse to last visited UMS directory
   * 
   * @param itemId Button ID
   */
  public musicLibraryClicked(): void {
    this.afterButtonClicked(this.routerMap.get("/music-library"));
  }

  /**
   * Browse to a UMS playlist
   * @param id playlist id
   */
  public browseToPlaylist(item: ServerPlaylistDto) {
    this.myPlaylistService.selectPlaylist(item.playlistId);
    this.afterButtonClicked(item.playlistId);
  }

  /**
   * My Album clicked.
   * 
   * @param id ID of my album button
   */
  public myAlbumClicked(): void {
    this.afterButtonClicked(this.routerMap.get("/myAlbums"));
  }

  /**
   * One Button is clicked. ID will be tracked for delivering the active class.
   * @param itemId 
   */
  public buttonClicked(routerUrl: string): void {
    this.afterButtonClicked(this.routerMap.get(routerUrl));
  }

  private afterButtonClicked(itemId: string): void {
    this.activeId.set(itemId);
  }

  get myPlaylistsAvailable(): boolean {
    return this.deviceService.selectedMediaServerDevice().extendedApi;
  }

  get serverPlaylists() {
    return this.playlistService.serverPl().serverPlaylists;
  }

  public getTextClass(url: string): string {
    let id = this.routerMap.get(url);
    if (id == this.activeId()) {
      return "active";
    }
    return "button-text";
  }

  public getPlaylistClass(id: string) {
    if (id == this.activeId()) {
      return "active";
    }
    return "button-text";
  }

  public get myFolderConfigured(): boolean {
    return this.configurationService.applicationConfig.myPlaylistFolderName?.length > 0;
  }

  public showPlaylistDialog(): void {
    this.defaultPlaylistService.openAddGlobalPlaylistDialogWithBackdrop(null, this.musicLibraryService.currentContainer());
  }

  get myMediaServerAvailable(): boolean {
    return this.deviceService.selectedMediaServerDevice().udn?.length > 0;
  }
}
