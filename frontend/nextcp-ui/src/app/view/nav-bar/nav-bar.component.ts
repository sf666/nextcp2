import { ModalSearchResultComponent } from './../modal-search-result/modal-search-result.component';
import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { DialogRole, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { FocusMonitor } from '@angular/cdk/a11y';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

  navbarOpen = false;
  modalDialog: MatDialogRef<ModalSearchResultComponent>;
  dialogConfig: MatDialogConfig;
  searchResultVisible : boolean;
  private quickSearch: string;

  @ViewChild('searchInput') searchInput: ElementRef;

  constructor() {
    this.searchResultVisible = false;
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

/**
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.screenHeight = window.innerHeight;
  }
*/

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  get quickSearchString() {
    return this.quickSearch;
  }

  set quickSearchString(value: string) {
    this.quickSearch = value;
    if (value == '') {
      this.searchResultVisible = false;
    } else {
      this.searchResultVisible = true;
    }
  }
}
