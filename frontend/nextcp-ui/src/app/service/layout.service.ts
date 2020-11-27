import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  public sidebarVisible: boolean = true;
  public footerVisible: boolean = true;
  public headerVisible: boolean = true;

  constructor() {

  }

  public setFramedView() { 
    this.sidebarVisible = true;
    this.footerVisible = true;
    this.headerVisible = true;
  }

  public setPlainView() { 
    this.sidebarVisible = false;
    this.footerVisible = false;
    this.headerVisible = false;
  }
}
