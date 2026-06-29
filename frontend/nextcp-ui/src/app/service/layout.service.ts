import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  // Layout flags as signals so OnPush components react to changes automatically.
  public readonly sidebarVisible = signal(true);
  public readonly footerVisible = signal(true);
  public readonly headerVisible = signal(true);

  // Mobile drawer state for the sidebar. On md+ the sidebar is always shown via CSS;
  // this flag only drives the slide-in overlay below the md breakpoint.
  public readonly sidebarOpen = signal(false);

  public toggleSidebar(): void {
    this.sidebarOpen.update((open) => !open);
  }

  public closeSidebar(): void {
    this.sidebarOpen.set(false);
  }

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
