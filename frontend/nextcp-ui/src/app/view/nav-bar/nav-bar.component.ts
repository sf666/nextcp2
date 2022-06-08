import { GlobalSearchService } from './../../service/search/global-search.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { SearchResultDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
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

  showBackButton = false;

  private currentPath: string;



  constructor(
    private router: Router,
    public globalSearchService: GlobalSearchService,
    public searchContentDirectoryService: ContentDirectoryService) {

    this.router.events.subscribe((event: NavigationEvent) => {
      if (event instanceof NavigationStart) {
        this.currentPath = event.url;
      }
    });
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
   * 
   */
  searchBackPressed(): void {
    //    this.contentDirectoryService.browseToRoot("", this.contentDirectoryService.currentContainerList.currentContainer.mediaServerUDN);
    void this.router.navigateByUrl('music-library');
  }

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.globalSearchService.clearSearch();
    }
  }

  focus(): void {
    // this.quickSearchString = this.quickSearchString;
  }

  isDisabled(): boolean {
    return this.backButtonDisabled;
  }
}
