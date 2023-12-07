import { DisplayContainerComponent } from './../../mediaserver/display-container/display-container.component';
import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { GlobalSearchService } from './../../service/search/global-search.service';
import { LayoutService } from './../../service/layout.service';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto, ContainerItemDto } from './../../service/dto.d';
import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';

@Component({
  selector: 'search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss'],
  providers: [ContentDirectoryService, CdsBrowsePathService, { provide: 'uniqueId', useValue: 'search-result_' }] // uniqueId is used for CdsBrowseService !
})

export class SearchResultComponent implements AfterViewInit {

  private showTopHeader_ = false;

  @ViewChild(DisplayContainerComponent) dispContainer: DisplayContainerComponent;

  constructor(
    public layoutService: LayoutService,
    public globalSearchService: GlobalSearchService,
    private cdsBrowsePathService: CdsBrowsePathService,
  ) {
  }

  ngAfterViewInit(): void {
    this.layoutService.setFramedViewWithoutNavbar();
    // Navigate to search result
    //this.dispContainer.browseTo(this.globalSearchService.selectedRootContainer);
  }

  //
  // Nav-Bar bindngs
  //
  getParentTitle(): string {
    return "search result";
  }

  public backButtonPressed(event: any) {
    const currentParent = this.globalSearchService.contentDirectoryService?.currentContainerList?.currentContainer?.parentID;
    if (currentParent) {
      this.dispContainer.clearSearch();
//      this.dispContainer.browseToOid(currentParent, this.globalSearchService.selectedRootContainer.mediaServerUDN, false);
      return;
    }
  }

  public backButtonDisabled(): boolean {
    return this.globalSearchService.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.globalSearchService.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) {
    this.showTopHeader_ = true;
  }

  //
  // bindings
  // =======================================================================

  getContentHandler(): ScrollLoadHandler {
    return { cdsBrowsePathService: this.cdsBrowsePathService, contentDirectoryService: this.globalSearchService.contentDirectoryService, persistenceService: null }
  }

  showTopHeader() : boolean {
    return this.showTopHeader_;
  }


}
