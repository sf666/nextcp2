import { Component } from '@angular/core';

@Component({
  selector: 'mediaServer-browse-result',
  templateUrl: './browse-result.component.html',
  styleUrls: ['./browse-result.component.scss']
})
export class BrowseResultComponent {

  constructor() { }
  
  public showBlur() {
    return true;
  }
  
}