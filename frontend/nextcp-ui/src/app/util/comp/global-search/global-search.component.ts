import {
  Component,
  ElementRef,
  HostListener,
  ChangeDetectionStrategy,
  inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GlobalSearchService } from 'src/app/service/search/global-search.service';
import { ModalSearchResultComponent } from 'src/app/view/search/modal-search-result/modal-search-result.component';

@Component({
  selector: 'global-search',
  standalone: true,
  imports: [FormsModule, ModalSearchResultComponent],
  templateUrl: './global-search.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrl: './global-search.component.scss',
})
export class GlobalSearchComponent {
  globalSearchService = inject(GlobalSearchService);
  private elementRef = inject<ElementRef<HTMLElement>>(ElementRef);

  @HostListener('document:mousedown', ['$event'])
  onDocumentMouseDown(event: MouseEvent): void {
    if (!this.globalSearchService.quickSearchPanelVisible) {
      return;
    }

    const target = event.target as Node | null;
    if (target && !this.elementRef.nativeElement.contains(target)) {
      this.globalSearchService.clearSearch();
    }
  }

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.globalSearchService.clearSearch();
    }
  }

  focus(): void {
    this.globalSearchService.quickSearchPanelVisible = true;
  }

  blur(): void {
    //this.globalSearchService.clearSearch();
  }

  toggleGlobalSearch(): void {
    this.globalSearchService.toggleGlobalSearch();
  }
}
