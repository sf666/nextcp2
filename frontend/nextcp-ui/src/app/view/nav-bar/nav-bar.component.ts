import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component } from '@angular/core';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})

export class NavBarComponent {

  navbarOpen = false;

  constructor(
    public contentDirectoryService: ContentDirectoryService, 
    private deviceService: DeviceService) {
  }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

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
