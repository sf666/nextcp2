import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router, Event as NavigationEvent, RouterLink } from '@angular/router';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, Output, EventEmitter, Input } from '@angular/core';
import { ModalSearchResultComponent } from '../search/modal-search-result/modal-search-result.component';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatButton, MatMiniFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { GlobalSearchComponent } from 'src/app/util/comp/global-search/global-search.component';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';


@Component({
    selector: 'nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss'],
    standalone: true,
    imports: [MatMiniFabButton, MatInput, MatIcon, FormsModule, ModalSearchResultComponent, GlobalSearchComponent, 
      MatButton, RouterLink]
})

export class NavBarComponent {
  @Input() parentTitle: string;
  @Input() backButtonDisabled: boolean = false;
  @Input() contentHandler: ScrollLoadHandler;


  // Inform parent about actions
  @Output() executeSearch = new EventEmitter<string>();
  @Output() searchKeyUp = new EventEmitter<KeyboardEvent>();
  @Output() backButtonPressed = new EventEmitter<any>();
  @Output() rootButtonPressed = new EventEmitter<any>();

  showBackButton = false;

  constructor(
    private router: Router,
    public globalSearchService: GlobalSearchService,
    public searchContentDirectoryService: ContentDirectoryService) {

  }

  gotoParent(): void {
    this.backButtonPressed.emit("");
  }

  gotoRoot(): void {
    this.rootButtonPressed.emit("");
  }

  /**
   * 
   */
  searchBackPressed(): void {
    void this.router.navigateByUrl('music-library');
  }

  showRootFolder() : boolean {
    return true;
  }

  showParentFolder() : boolean {
    if (!this.contentHandler?.contentDirectoryService?.currentContainerList?.currentContainer?.parentID ) {
      return false;
    } else {
      if (this.contentHandler?.contentDirectoryService?.currentContainerList?.currentContainer?.parentID === "0" || 
          this.contentHandler?.contentDirectoryService?.currentContainerList?.currentContainer?.parentID === "-1") {
        return false;
      }
      return true;
    }
  }
}
