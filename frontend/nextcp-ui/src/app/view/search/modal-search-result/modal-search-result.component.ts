import { GlobalSearchService } from './../../../service/search/global-search.service';
import { TransportService } from '../../../service/transport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { QualityBadgeComponent } from '../../../util/comp/quality-badge/quality-badge.component';

@Component({
    selector: 'modal-search-result',
    templateUrl: './modal-search-result.component.html',
    styleUrls: ['./modal-search-result.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    // non singleton injections for global search service    
    providers: [
      { provide: ContentDirectoryService, useClass: ContentDirectoryService },
    ], 
    standalone: true,
    imports: [QualityBadgeComponent]
})

/**
 * This class supports the search popup dialog.
 */
export class ModalSearchResultComponent{

  constructor(
    private gss: GlobalSearchService,
  ) {}

  get globalSearchService() : GlobalSearchService {
    return this.gss;
  }
}
