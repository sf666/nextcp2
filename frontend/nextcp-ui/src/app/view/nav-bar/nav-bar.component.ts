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

  showBackButton = false;

  private currentPath: string;

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private router: Router,
    public playlistService: PlaylistService,
    private deviceService: DeviceService) {

    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        this.currentPath = event.url;
      }
    });
  }

  // UI state management for navbar 

  get musicLibraryVisible(): boolean {
    return this.currentPath === '/music-library';
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

  // music-library
  public get currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }  

  public get parentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }  

  // Search
  get quickSearchString() {
    return this.contentDirectoryService.quickSearchQueryString;
  }

  set quickSearchString(value: string) {
    this.contentDirectoryService.quickSearchQueryString = value;
    if (value == '') {
      this.contentDirectoryService.quickSearchPanelVisible = false;
    } else {
      if (value && value.length > 2) {
        this.contentDirectoryService.quickSearchPanelVisible = true;
        this.contentDirectoryService.quickSearch(value, "", this.deviceService.selectedMediaServerDevice.udn);
      }
    }
  }

  clearSearch() {
    this.quickSearchString = "";
  }

  isDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0';
  }

  gotoParent() {
    this.contentDirectoryService.browseChildren(this.contentDirectoryService.currentContainerList.currentContainer.parentID, "",
      this.contentDirectoryService.currentContainerList.currentContainer.mediaServerUDN);
  }
}

/**
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.screenHeight = window.innerHeight;
  }
*/
