import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  public sidebarVisible = true;
  public footerVisible = true;
  public headerVisible = true;

  public setFramedView() : void{ 
    this.sidebarVisible = true;
    this.footerVisible = true;
    this.headerVisible = true;
  }

  public setPlainView(): void { 
    this.sidebarVisible = false;
    this.footerVisible = false;
    this.headerVisible = false;
  }

  public isMobileDevice(): boolean { 
    const isTouchDevice = window.screenX === 0 && ('ontouchstart' in window || 'onmsgesturechange' in window);
    return isTouchDevice;
  }  
}
