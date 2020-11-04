import { AvtransportService } from './../../../service/avtransport.service';
import { SearchItemService } from './../../../service/search/search-item.service';
import { Component} from '@angular/core';

@Component({
  selector: 'app-search-result-item-single',
  templateUrl: './search-result-item-single.component.html',
  styleUrls: ['./search-result-item-single.component.scss']
})
export class SearchResultItemSingleComponent {

  constructor(public searchItemService: SearchItemService, private avtransportService: AvtransportService) {
  }

  play() {
    this.avtransportService.playResource(this.searchItemService.musicItem);
  }
}
