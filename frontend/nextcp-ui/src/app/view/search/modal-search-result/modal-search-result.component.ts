import { GlobalSearchService } from './../../../service/search/global-search.service';
import { TransportService } from '../../../service/transport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { QualityBadgeComponent } from '../../../util/comp/quality-badge/quality-badge.component';

@Component({
    selector: 'modal-search-result',
    templateUrl: './modal-search-result.component.html',
    styleUrls: ['./modal-search-result.component.scss'],
    providers: [ContentDirectoryService],
    standalone: true,
    imports: [QualityBadgeComponent]
})

/**
 * This class supports the search popup dialog.
 */
export class ModalSearchResultComponent{

  constructor(
    public globalSearchService: GlobalSearchService,
    private transportService: TransportService,
    private router: Router) {
  }
}
