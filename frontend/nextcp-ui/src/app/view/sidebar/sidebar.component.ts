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
export class SidebarComponent  {

  private _mediaServerUdn : string;
  private _mediaRendererUdn : string;

  constructor(
    public deviceService: DeviceService,
    private contentDirectoryService: ContentDirectoryService,
    public rendererService: RendererService) {
    deviceService.mediaRendererChanged$.subscribe(data => this._mediaRendererUdn = data.udn);
    deviceService.mediaServerChanged$.subscribe(data => this._mediaServerUdn = data.udn);
  }

  public musicLibraryClicked() : void{
    if (!this.contentDirectoryService.currentContainerList.currentContainer) {
      this.contentDirectoryService.browseToRoot('');
    }
  }
  
  public getTextClass(el : MatAnchor) : string {
    if (el._elementRef.nativeElement.classList.contains("active")) {
      return "";
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

  public myAlbum() : void {
    this.contentDirectoryService.browseToMyMusic();
  }
}
