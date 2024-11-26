import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router, RouterLink } from '@angular/router';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, Output, EventEmitter, Input, input, output, computed } from '@angular/core';
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
  parentTitle = input<string>();
  backButtonDisabled = input<boolean>(false);
  contentHandler = input.required<ScrollLoadHandler>();


  // Inform parent about actions
  executeSearch = output<string>();
  searchKeyUp = output<KeyboardEvent>();
  backButtonPressed = output<any>();
  rootButtonPressed = output<any>();

  showParentFolder = computed(() =>  {
    if (!this.contentHandler().contentDirectoryService?.currentContainerList().currentContainer?.parentID ) {
      return false;
    } else {
      if (this.contentHandler().contentDirectoryService?.currentContainerList().currentContainer?.parentID === "0" || 
          this.contentHandler().contentDirectoryService?.currentContainerList().currentContainer?.parentID === "-1") {
        return false;
      }
      return true;
    }
  });

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
}
