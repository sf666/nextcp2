import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  constructor() {

  }

  public sidebarVisible = true;
  public footerVisible = true;
  public headerVisible = true;

  public setFramedView() : void{ 
    this.sidebarVisible = true;
    this.footerVisible = true;
    this.headerVisible = true;
  }

  public setFramedViewWithoutNavbar() : void{ 
    this.sidebarVisible = true;
    this.footerVisible = true;
    this.headerVisible = false;
  }

  public setPlainView(): void { 
    this.sidebarVisible = false;
    this.footerVisible = false;
    this.headerVisible = false;
  }

  public isMobileDevice(): boolean {
      return false;
  }  
}
