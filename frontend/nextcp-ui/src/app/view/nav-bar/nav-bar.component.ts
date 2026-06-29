import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router } from '@angular/router';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import {
  Component,
  inject,
  input,
  output,
  ChangeDetectionStrategy,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GlobalSearchComponent } from 'src/app/util/comp/global-search/global-search.component';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule, GlobalSearchComponent],
})
export class NavBarComponent {
  private readonly router = inject(Router);
  public readonly globalSearchService = inject(GlobalSearchService);
  public readonly searchContentDirectoryService = inject(ContentDirectoryService);
  public readonly layoutService = inject(LayoutService);

  parentTitle = input<string>();
  homeButtonVisible = input<boolean>(false);
  backButtonVisible = input<boolean>(false);
  contentHandler = input.required<ScrollLoadHandler>();

  // Inform parent about actions
  executeSearch = output<string>();
  searchKeyUp = output<KeyboardEvent>();
  backButtonPressed = output<any>();
  homeButtonPressed = output<any>();

  gotoParent(): void {
    this.backButtonPressed.emit('');
  }

  gotoRoot(): void {
    this.homeButtonPressed.emit('');
  }
}
