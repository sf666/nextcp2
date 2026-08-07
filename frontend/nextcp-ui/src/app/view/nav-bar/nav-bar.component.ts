import { GlobalSearchService } from './../../service/search/global-search.service';
import { Router } from '@angular/router';
import { LayoutService } from './../../service/layout.service';
import {
  BrowseCrumb,
  ContentDirectoryService,
} from './../../service/content-directory.service';
import {
  Component,
  computed,
  inject,
  input,
  output,
  signal,
  ChangeDetectionStrategy,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GlobalSearchComponent } from 'src/app/util/comp/global-search/global-search.component';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';

/**
 * How many trailing crumbs stay visible when the path is too long to show in
 * full. Two keeps the current container plus the one it sits in, which is the
 * pair that tells you where you are; everything before it folds into the
 * overflow menu.
 */
const VISIBLE_TAIL = 2;

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
  public readonly searchContentDirectoryService = inject(
    ContentDirectoryService,
  );
  public readonly layoutService = inject(LayoutService);

  homeButtonVisible = input<boolean>(false);
  contentHandler = input.required<ScrollLoadHandler>();

  // Inform parent about actions
  executeSearch = output<string>();
  searchKeyUp = output<KeyboardEvent>();
  homeButtonPressed = output<any>();
  crumbPressed = output<BrowseCrumb>();

  /**
   * The clickable part of the path: everything ABOVE the current container.
   *
   * The container you are in is already the page headline, so repeating it here
   * would say the same thing twice and add an entry that cannot be clicked. The
   * last crumb is therefore the parent — one tap up.
   */
  private path = computed(() => {
    const full = this.searchContentDirectoryService.browsePath();
    return full.length > 0 ? full.slice(0, full.length - 1) : [];
  });

  /** True when the chain could not be traced back to the root. */
  pathTruncated = computed(() =>
    this.searchContentDirectoryService.browsePathTruncated(),
  );

  /**
   * Crumbs hidden behind the overflow button. Empty while the whole path fits.
   */
  hiddenCrumbs = computed(() => {
    const p = this.path();
    return p.length > VISIBLE_TAIL ? p.slice(0, p.length - VISIBLE_TAIL) : [];
  });

  /** Crumbs rendered inline, always including the current container. */
  visibleCrumbs = computed(() => {
    const p = this.path();
    return p.length > VISIBLE_TAIL ? p.slice(p.length - VISIBLE_TAIL) : p;
  });

  /**
   * Show the ellipsis when crumbs are folded away, or when we never knew the
   * start of the path — in both cases something precedes what is on screen.
   */
  overflowVisible = computed(
    () => this.hiddenCrumbs().length > 0 || this.pathTruncated(),
  );

  overflowOpen = signal(false);

  toggleOverflow(): void {
    this.overflowOpen.update((open) => !open);
  }

  gotoRoot(): void {
    this.overflowOpen.set(false);
    this.homeButtonPressed.emit('');
  }

  gotoCrumb(crumb: BrowseCrumb): void {
    this.overflowOpen.set(false);
    this.crumbPressed.emit(crumb);
  }
}
