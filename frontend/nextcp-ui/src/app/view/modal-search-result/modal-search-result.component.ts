import { MusicItemDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss']
})
export class ModalSearchResultComponent {

  public searchVisible : boolean;

  constructor(public contentDirectoryService: ContentDirectoryService) { 
    this.searchVisible = false;    
  }

  musicItemSelected(musicItem : MusicItemDto) {
    
  }
}
