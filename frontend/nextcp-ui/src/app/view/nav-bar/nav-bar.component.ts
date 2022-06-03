import { ConfigurationService } from './../../service/configuration.service';
import { debounce } from 'src/app/global';
import { Router, NavigationStart, Event as NavigationEvent } from '@angular/router';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, Output, EventEmitter, Input } from '@angular/core';


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})

export class NavBarComponent {
  @Input() parentTitle: string;
  @Input() backButtonDisabled: boolean = false;

  // Inform parent about actions
  @Output() executeSearch = new EventEmitter<string>();
  @Output() searchKeyUp = new EventEmitter<KeyboardEvent>();
  @Output() backButtonPressed = new EventEmitter<any>();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private doSearchFunc: any;

  showBackButton = false;

  private currentPath: string;
  private currentSearchText: string;

  private doSearchThrotteled = (): void => this.contentDirectoryService.quickSearch(this.currentSearchText, "", this.deviceService.selectedMediaServerDevice.udn);

  constructor(
    private router: Router,
    public contentDirectoryService: ContentDirectoryService,
    private configurationService: ConfigurationService,
    private deviceService: DeviceService) {

    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        this.currentPath = event.url;
      }
    });
    this.doSearchFunc = debounce(this.getSearchDelay(), this.doSearchThrotteled);
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

  gotoParent(): void {
    this.backButtonPressed.emit("");
  }
  
  /**
   * Close serach ?
   */
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
        this.doSearch();
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
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    this.doSearchFunc();
  }

  clearSearch(): void {
    this.quickSearchString = "";
  }

  isDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }

  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }
}