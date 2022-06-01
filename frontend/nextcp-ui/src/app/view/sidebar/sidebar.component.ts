import { ContainerDto } from './../../service/dto.d';
import { PlaylistService } from './../../service/playlist.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { Component, ViewEncapsulation } from '@angular/core';
import { MatAnchor } from '@angular/material/button';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {

  private _mediaServerUdn: string;
  private _mediaRendererUdn: string;

  private activeId: number;
  private rememberedLastCdsObjectId: ContainerDto;

  constructor(
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    private contentDirectoryService: ContentDirectoryService,
    public rendererService: RendererService) {
    deviceService.mediaRendererChanged$.subscribe(data => this._mediaRendererUdn = data.udn);
    deviceService.mediaServerChanged$.subscribe(data => this._mediaServerUdn = data.udn);

    this.activeId = -1; // default active route is "Media Library"
  }

  /**
   * Browse to last visited UMS directory
   * 
   * @param itemId Button ID
   */
  public musicLibraryClicked(itemId: number): void {
    if (this.rememberedLastCdsObjectId) {
      this.contentDirectoryService.browseChildrenByContiner(this.rememberedLastCdsObjectId);
    }
    this.activeId = itemId;
    if (!this.contentDirectoryService.currentContainerList.currentContainer) {
      this.contentDirectoryService.browseToRoot('');
    }
  }

  /**
   * Browse to a UMS playlist
   * @param id playlist id
   */
  public browseToPlaylist(id: number) {
    this.afterButtonClicked(id);
  }

  /**
   * My Album clicked.
   * 
   * @param id ID of my album button
   */
  public myAlbumClicked(id: number): void {
    this.contentDirectoryService.browseToMyMusic();
    this.afterButtonClicked(id);
  }

  /**
   * One Button is clicked. ID will be tracked for delivering the active class.
   * @param itemId 
   */
  public buttonClicked(itemId: number): void {
    this.afterButtonClicked(itemId);
  }

  private afterButtonClicked(itemId: number): void {
    this.rememberedLastCdsObjectId = this.contentDirectoryService.currentContainerList.currentContainer;
    this.activeId = itemId;
  }

  public getTextClass(id: number): string {
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
