import { Router } from '@angular/router';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto } from './../../../service/dto.d';
import { SearchItemService } from './../../../service/search/search-item.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-result-item-multi',
  templateUrl: './search-result-item-multi.component.html',
  styleUrls: ['./search-result-item-multi.component.scss']
})
export class SearchResultItemMultiComponent {

  constructor(
    public searchItemService: SearchItemService,
    private contentDirectoryService: ContentDirectoryService,
    private router: Router) { 

  }

  browseToContainer(container: ContainerDto) {
    this.contentDirectoryService.browseChildrenByContiner(container);
    this.router.navigateByUrl('music-library');    
  }
}
