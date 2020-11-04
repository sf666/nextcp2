import { SearchContainerService } from './../../../service/search/search-container.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-result-item-multi',
  templateUrl: './search-result-item-multi.component.html',
  styleUrls: ['./search-result-item-multi.component.scss']
})
export class SearchResultItemMultiComponent {

  constructor(public searchContainerService: SearchContainerService) { }
}
