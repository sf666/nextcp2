import { GlobalSearchService } from './../../../service/search/global-search.service';
import { TransportService } from '../../../service/transport.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { QualityBadgeComponent } from '../../../util/comp/quality-badge/quality-badge.component';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';
import { MusicItemDto } from 'src/app/service/dto';

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
    private songOptionsServiceService: SongOptionsServiceService,
    private gss: GlobalSearchService,
  ) {}

  get globalSearchService() : GlobalSearchService {
    return this.gss;
  }

  showSongPopup(event: MouseEvent, item: MusicItemDto): void {
      this.songOptionsServiceService
        .openOptionsDialog(event, item, undefined)
        .subscribe((result) => {
          // handle individual user action from dialog if needed
        });
    }
}
