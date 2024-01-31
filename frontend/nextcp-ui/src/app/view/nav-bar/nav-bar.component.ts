import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router, NavigationStart, Event as NavigationEvent } from '@angular/router';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, Output, EventEmitter, Input } from '@angular/core';
import { ModalSearchResultComponent } from '../search/modal-search-result/modal-search-result.component';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatMiniFabButton } from '@angular/material/button';


@Component({
    selector: 'nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss'],
    standalone: true,
    imports: [MatMiniFabButton, MatInput, FormsModule, ModalSearchResultComponent]
})

export class NavBarComponent {
  @Input() parentTitle: string;
  @Input() backButtonDisabled: boolean = false;

  // Inform parent about actions
  @Output() executeSearch = new EventEmitter<string>();
  @Output() searchKeyUp = new EventEmitter<KeyboardEvent>();
  @Output() backButtonPressed = new EventEmitter<any>();

  showBackButton = false;

  constructor(
    private router: Router,
    public globalSearchService: GlobalSearchService,
    public searchContentDirectoryService: ContentDirectoryService) {

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

  blur(): void {
    //this.globalSearchService.clearSearch();
  }

  isDisabled(): boolean {
    return this.backButtonDisabled;
  }
}
