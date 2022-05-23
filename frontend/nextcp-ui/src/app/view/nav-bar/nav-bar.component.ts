import { ConfigurationService } from './../../service/configuration.service';
import { debounce } from 'src/app/global';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { ContainerDto } from './../../service/dto.d';
import { PlaylistService } from './../../service/playlist.service';
import { Router, NavigationStart, Event as NavigationEvent } from '@angular/router';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component } from '@angular/core';


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})

export class NavBarComponent {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private doSearchFunc: any;

  showBackButton = false;

  private currentPath: string;
  private currentSearchText: string;

  private doSearchThrotteled = (): void => this.contentDirectoryService.quickSearch(this.currentSearchText, "", this.deviceService.selectedMediaServerDevice.udn);

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private router: Router,
    private cdsBrowsePathService: CdsBrowsePathService,
    public playlistService: PlaylistService,
    private configurationService: ConfigurationService,
    private deviceService: DeviceService) {

    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        this.currentPath = event.url;
      }
    });
    this.doSearchFunc = debounce(this.getSearchDelay(), this.doSearchThrotteled);
  }

  // UI state management for navbar   

  get musicLibraryVisible(): boolean {
    return this.currentPath === '/music-library' || this.currentPath === '/' || this.currentPath === '/myAlbums';
  }
  get playlistVisible(): boolean {
    return this.currentPath === '/playlist';
  }
  get playerVisible(): boolean {
    return this.currentPath === '/player';
  }
  get radioVisible(): boolean {
    return this.currentPath === '/radio';
  }
  get searchResultSingleVisible(): boolean {
    return this.currentPath === '/searchResultSingleItem';
  }
  get searchResultMultiVisible(): boolean {
    return this.currentPath === '/searchResultContainer';
  }
  get settingsVisible(): boolean {
    return this.currentPath === '/settings';
  }
  get inputOutputVisible(): boolean {
    return this.currentPath === '/input-output';
  }

  get searchResultVisible(): boolean {
    return this.searchResultSingleVisible || this.searchResultMultiVisible;
  }

  // music-library
  public get currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }

  public get parentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }

  gotoParent(): void {
    this.contentDirectoryService.gotoParent();
  }
  
  searchBackPressed(): void {
    //    this.contentDirectoryService.browseToRoot("", this.contentDirectoryService.currentContainerList.currentContainer.mediaServerUDN);
    void this.router.navigateByUrl('music-library');
  }

  //
  // Search
  // =========================================================================

  get quickSearchString(): string {
    return this.contentDirectoryService.quickSearchQueryString;
  }

  set quickSearchString(value: string) {
    this.contentDirectoryService.quickSearchQueryString = value;
    if (value == '') {
      this.contentDirectoryService.quickSearchPanelVisible = false;
    } else {
      if (value && value.length > 2) {
        this.contentDirectoryService.quickSearchPanelVisible = true;
        this.currentSearchText = value;
        this.doSearch();
      }
    }
  }

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.contentDirectoryService.clearSearch();
      this.quickSearchString = '';
    }
  }

  focus(): void {
    // this.quickSearchString = this.quickSearchString;
  }

  private doSearch(): void {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    this.doSearchFunc();
  }

  clearSearch(): void {
    this.quickSearchString = "";
  }

  isDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }

  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }
}