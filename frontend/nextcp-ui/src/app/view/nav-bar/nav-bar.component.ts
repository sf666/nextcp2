import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { ContainerDto } from './../../service/dto.d';
import { PlaylistService } from './../../service/playlist.service';
import { Router, NavigationStart, Event as NavigationEvent } from '@angular/router';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component } from '@angular/core';
import * as _ from "lodash";


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})

export class NavBarComponent {

  showBackButton = false;

  private currentPath: string;
  private currentSearchText: string;
  private throttledSearch;

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private router: Router,
    private cdsBrowsePathService: CdsBrowsePathService,
    public playlistService: PlaylistService,
    private deviceService: DeviceService) {

    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        this.currentPath = event.url;
      }
      this.throttledSearch = _.throttle(this.searchBound, 500)
    });
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  public searchBound = () => {
    return this.doSearch();
  };

  // UI state management for navbar 

  get musicLibraryVisible(): boolean {
    return this.currentPath === '/music-library' || this.currentPath === '/';
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
        // eslint-disable-next-line @typescript-eslint/no-unsafe-call
        this.throttledSearch();
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
    this.contentDirectoryService.quickSearch(this.currentSearchText, "", this.deviceService.selectedMediaServerDevice.udn);
  }

  clearSearch(): void {
    this.quickSearchString = "";
  }

  isDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }
}
