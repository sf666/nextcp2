import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss']
})
export class ModalSearchResultComponent implements OnInit {

  public searchVisible : boolean;

  constructor() { 
    this.searchVisible = false;
  }


  ngOnInit(): void {
  }

}
