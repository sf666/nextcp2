import { GlobalSearchService } from './../../../service/search/global-search.service';
import { TransportService } from '../../../service/transport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { RouteReuseStrategy, Router } from '@angular/router';
import { Component, ViewChild } from '@angular/core';
import { MusicLibraryComponent } from '../../music-library/music-library.component';
import { QualityBadgeComponent } from '../../../util/comp/quality-badge/quality-badge.component';
import { NgIf, NgFor } from '@angular/common';

@Component({
    selector: 'app-modal-search-result',
    templateUrl: './modal-search-result.component.html',
    styleUrls: ['./modal-search-result.component.scss'],
    providers: [ContentDirectoryService],
    standalone: true,
    imports: [NgIf, NgFor, QualityBadgeComponent]
})

export class ModalSearchResultComponent {

  constructor(
    private searchItemService: SearchItemService,
    public globalSearchService: GlobalSearchService,
    private transportService: TransportService,
    private musicLibraryComponent: MusicLibraryComponent,
    private router: Router) {
  }

  //
  // single item selected
  //

  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug("music item selected : " + musicItem);
    this.searchItemService.musicItem = musicItem;
    this.globalSearchService.clearSearch();
    this.transportService.playResource(musicItem);
    this.globalSearchService.clearSearch();
  }

  containerSelected(container: ContainerDto): void {
    console.debug("container selected : " + container);
    this.globalSearchService.quickSearchPanelVisible = false;
    this.musicLibraryComponent.contentDirectoryService.browseChildrenByContainer(container);
    void this.router.navigateByUrl('music-library');
    this.globalSearchService.clearSearch();
  }
}
