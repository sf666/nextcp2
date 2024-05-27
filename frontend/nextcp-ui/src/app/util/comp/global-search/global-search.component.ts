import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatMiniFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { GlobalSearchService } from 'src/app/service/search/global-search.service';
import { ModalSearchResultComponent } from 'src/app/view/search/modal-search-result/modal-search-result.component';

@Component({
  selector: 'global-search',
  standalone: true,
  imports: [
    MatMiniFabButton,
    MatInput,
    MatIcon,
    FormsModule,
    ModalSearchResultComponent,
  ],
  templateUrl: './global-search.component.html',
  styleUrl: './global-search.component.scss',
})
export class GlobalSearchComponent {
  constructor(public globalSearchService: GlobalSearchService) {}


  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.globalSearchService.clearSearch();
    }
  }

  focus(): void {
    this.globalSearchService.quickSearchPanelVisible = true;;
  }

  blur(): void {
    //this.globalSearchService.clearSearch();
  }

}
