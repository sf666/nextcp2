import { SearchItemService } from './../../../service/search/search-item.service';
import { MusicItemDto } from './../../../service/dto.d';
import { Component, OnInit, Injectable, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-search-result-item-single',
  templateUrl: './search-result-item-single.component.html',
  styleUrls: ['./search-result-item-single.component.scss']
})
export class SearchResultItemSingleComponent {

  musicItem: MusicItemDto;

  constructor(public searchItemService: SearchItemService) {
  }

  play() {

  }
}
