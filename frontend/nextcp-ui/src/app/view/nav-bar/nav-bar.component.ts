import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router} from '@angular/router';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, input, output, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GlobalSearchComponent } from 'src/app/util/comp/global-search/global-search.component';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss'],
  standalone: true,
  imports: [FormsModule, GlobalSearchComponent]
})

export class NavBarComponent {
  parentTitle = input<string>();
  backButtonDisabled = input<boolean>(true);
  contentHandler = input.required<ScrollLoadHandler>();


  // Inform parent about actions
  executeSearch = output<string>();
  searchKeyUp = output<KeyboardEvent>();
  backButtonPressed = output<any>();
  rootButtonPressed = output<any>();

  showParentFolder = computed(() => {
    console.log("[parent folder button] computing displaying parent folder text ... ");
    if (!this.contentHandler().contentDirectoryService?.currentContainerList().currentContainer?.parentID) {
      console.log("[parent folder button] returning false : No parent ID.");
      return false;
    } else {
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
