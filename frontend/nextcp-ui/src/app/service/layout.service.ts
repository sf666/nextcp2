import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  // Layout flags as signals so OnPush components react to changes automatically.
  public readonly sidebarVisible = signal(true);
  public readonly footerVisible = signal(true);
  public readonly headerVisible = signal(true);

  public setFramedView() : void{
    this.sidebarVisible.set(true);
    this.footerVisible.set(true);
    this.headerVisible.set(true);
  }

  public setFramedViewWithoutNavbar() : void{
    this.sidebarVisible.set(true);
    this.footerVisible.set(true);
    this.headerVisible.set(false);
  }

  public setPlainView(): void {
    this.sidebarVisible.set(false);
    this.footerVisible.set(false);
    this.headerVisible.set(false);
  }

  public isMobileDevice(): boolean {
      return false;
  }  
}
