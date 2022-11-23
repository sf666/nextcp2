import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { GlobalSearchService } from './../../service/search/global-search.service';
import { LayoutService } from './../../service/layout.service';
import { DeviceService } from 'src/app/service/device.service';
import { Subject } from 'rxjs';
import { PersistenceService } from './../../service/persistence/persistence.service';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto, ContainerItemDto } from './../../service/dto.d';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss'],
  providers: [ContentDirectoryService, CdsBrowsePathService, { provide: 'uniqueId', useValue: 'search-result_' }]
})

export class SearchResultComponent implements OnInit {

  private showTopHeader_ = false;

  constructor(
    public layoutService: LayoutService,
    public globalSearchService: GlobalSearchService,
    private cdsBrowsePathService: CdsBrowsePathService,
  ) {
  }

  ngOnInit(): void {
  }

  //
  // Nav-Bar bindngs
  //
  getParentTitle(): string {
    return "search result for ... ";
  }

  public backButtonPressed(event: any) {
    this.showTopHeader_ = false;
    this.globalSearchService.backToLastSearch();
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

  currentContainer(): ContainerDto {
    return this.globalSearchService.contentDirectoryService.currentContainerList.currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.globalSearchService.contentDirectoryService.musicTracks_;
  }

  otherItems_(): MusicItemDto[] {
    return this.globalSearchService.contentDirectoryService.otherItems_;
  }

  albums(): ContainerDto[] {
    return this.globalSearchService.contentDirectoryService.currentContainerList.albumDto;
  }

  playlists(): ContainerDto[] {
    return this.globalSearchService.contentDirectoryService.playlistList_;
  }

  otherContainer(): ContainerDto[] {
    return this.globalSearchService.contentDirectoryService.containerList_;
  }
}
