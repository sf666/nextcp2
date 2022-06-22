import { InputFieldConfig } from './../../popup/input-field-dialog/input-field-dialog.d';
import { InputFieldDialogComponent } from './../../popup/input-field-dialog/input-field-dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NavigationEnd, Router, RouterEvent } from '@angular/router';
import { MyPlaylistService } from './../my-playlists/my-playlist.service';
import { PlaylistService } from './../../service/playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';
import { ServerPlaylistDto } from 'src/app/service/dto';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {

  private _mediaServerUdn: string;
  private _mediaRendererUdn: string;
  public routerMap = new Map<string, number>();

  // Dialogs
  createPlaylistDialogRef: MatDialogRef<InputFieldDialogComponent>;

  private activeId: number;

  constructor(
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    private myPlaylistService: MyPlaylistService,
    private router: Router,
    private dialog: MatDialog,
    public rendererService: RendererService) {
    deviceService.mediaRendererChanged$.subscribe(data => this._mediaRendererUdn = data.udn);
    deviceService.mediaServerChanged$.subscribe(data => this._mediaServerUdn = data.udn);

    this.routerMap.set("/", -1);                // default route is music-library
    this.routerMap.set("/music-library", -1);
    this.routerMap.set("/playlist", -2);
    this.routerMap.set("/player", -3);
    this.routerMap.set("/radio", -4);
    this.routerMap.set("/myAlbums", -5);
    this.routerMap.set("/settings", -6);
    this.routerMap.set("/myPlaylists", 0);      // All positives ID's are playlists

    this.routerMap.set("/myTracks", -999);      // not used yet

    router.events.subscribe(event => this.calActiveId(event));
  }

  private calActiveId(nav: any) {
    if (nav instanceof NavigationEnd) {
      this.activeId = this.routerMap.get(nav.url);
      if (this.activeId == 0) {
        this.activeId = this.myPlaylistService.activePlaylistId;
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


  public createPlaylistClicked(): void {
    let inputFieldConfig: InputFieldConfig = {
      cancelButtonText: "cancel",
      placeholder: "enter playlist name",
      submitButtonText: "add",
      title: "Create new playlist"
    }

    this.createPlaylistDialogRef = this.dialog.open(InputFieldDialogComponent, {
      data: inputFieldConfig,
      hasBackdrop: true
    });

    this.createPlaylistDialogRef.afterClosed().subscribe(data => { if (data) { this.playlistService.createPlaylist(data) } });
  }

  /**
   * One Button is clicked. ID will be tracked for delivering the active class.
   * @param itemId 
   */
  public buttonClicked(routerUrl: string): void {
    this.afterButtonClicked(this.routerMap.get(routerUrl));
  }

  private afterButtonClicked(itemId: number): void {
    this.activeId = itemId;
  }

  get myPlaylistsAvailable(): boolean {
    return this.playlistService.serverPl.serverPlaylists.length > 0;
  }

  public getTextClass(url: string): string {
    let id = this.routerMap.get(url);
    if (id == this.activeId) {
      return "active";
    }
    return "button-text";
  }

  public getPlaylistClass(id: number) {
    if (id == this.activeId) {
      return "active";
    }
    return "button-text";
  }

  /**
   * Getter mediaServerUdn
   * @return {string}
   */
  public get mediaServerUdn(): string {
    return this._mediaServerUdn;
  }

  /**
   * Getter mediaRendererUdn
   * @return {string}
   */
  public get mediaRendererUdn(): string {
    return this._mediaRendererUdn;
  }

  /**
   * Setter mediaServerUdn
   * @param {string} value
   */
  public set mediaServerUdn(value: string) {
    this._mediaServerUdn = value;
    this.deviceService.setMediaServerByUdn(value);
  }

  /**
   * Setter mediaRendererUdn
   * @param {string} value
   */
  public set mediaRendererUdn(value: string) {
    this._mediaRendererUdn = value;
    this.deviceService.setMediaRendererByUdn(value);
  }
}
