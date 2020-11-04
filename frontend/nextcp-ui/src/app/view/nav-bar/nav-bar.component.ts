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

  constructor(public contentDirectoryService: ContentDirectoryService, private deviceService: DeviceService) {
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
}

/**
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.screenHeight = window.innerHeight;
  }

  ngOnInit(): void {
    if (this.modalDialog == null) {
      this.dialogConfig = new MatDialogConfig();
      this.dialogConfig.hasBackdrop = false;
      this.dialogConfig.restoreFocus = true;
      this.dialogConfig.autoFocus = false;
      this.dialogConfig.id = "modal-search";
      this.dialogConfig.panelClass = "searchModalClass";
    }
  }

*/


