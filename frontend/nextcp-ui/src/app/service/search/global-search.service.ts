import { Router } from '@angular/router';
import { ConfigurationService } from './../configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import {
  ContentDirectoryService,
  SEARCH_RESULT_CONTAINER_ID,
  ShowAllType,
} from './../content-directory.service';
import {
  SearchResultDto,
  ContainerDto,
  MusicItemDto,
  SearchRequestDto,
} from './../dto.d';
import { Injectable, signal, inject } from '@angular/core';
import { debounce } from 'src/app/global';
import { Subject } from 'rxjs';
import { MusicLibraryService } from '../music-library/music-library.service';

export interface ShowAllRequest {
  type: ShowAllType;
  request: SearchRequestDto;
}

@Injectable({
  providedIn: 'root',
})
export class GlobalSearchService {
  contentDirectoryService = inject(ContentDirectoryService);
  private deviceService = inject(DeviceService);
  private musicLibraryService = inject(MusicLibraryService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private router = inject(Router);
  private configurationService = inject(ConfigurationService);

  public globalSearch = signal(true);

  //
  // Event publishing
  //

  // User clicked on an quick search music item
  musicItemClicked$: Subject<MusicItemDto> = new Subject();

  /**
   * The "show all" request waiting to be run, or undefined when there is none.
   *
   * Deliberately state and not an event: clicking "show all" navigates to the
   * music library, and that navigation frequently destroys the view and builds a
   * new one (`/music-library/:objectId` and `/music-library` are two routes).
   * A Subject fired before the new view existed was simply lost, and the fresh
   * view browsed the last folder instead — a signal is still there to be read
   * whenever the view gets around to looking.
   *
   * Read it with consumePendingShowAll(), which clears it, so a view that
   * initialises along several paths at once still runs the search exactly once.
   */
  private pendingShowAll_ = signal<ShowAllRequest | undefined>(undefined);
  public pendingShowAll = this.pendingShowAll_.asReadonly();

  // QuickSearch Support (Global search)
  public quickSearchResultList = signal<SearchResultDto>(
    this.dtoGeneratorService.generateEmptySearchResultDto(),
  );
  public quickSearchQueryString = signal('');
  private quickSearchPanelVisible_ = signal(false);
  private MIN_SEARCH_LEN = 2;

  private currentSearchText = '';
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private doSearchFunc: any;

  toggleGlobalSearch(): void {
    this.globalSearch.update((value) => !value);
  }

  private doSearchThrotteled = (): void => {
    let containerId = '0';
    if (!this.globalSearch()) {
      containerId = this.musicLibraryService.currentContainerId();
    }
    this.contentDirectoryService
      .quickSearch(
        this.currentSearchText,
        '',
        this.deviceService.selectedMediaServerDevice().udn,
        containerId,
      )
      .subscribe((data) => this.searchResultReceived(data));
  };

  constructor() {
    this.doSearchFunc = debounce(
      this.getSearchDelay(),
      this.doSearchThrotteled,
    );
  }

  getSearchDelay(): number {
    const delay =
      this.configurationService.serverConfig?.applicationConfig
        ?.globalSearchDelay != null
        ? this.configurationService.serverConfig.applicationConfig
            ?.globalSearchDelay
        : 600;
    return Math.max(300, delay);
  }

  get quickSearchPanelVisible() {
    return this.quickSearchPanelVisible_();
  }

  set quickSearchPanelVisible(value: boolean) {
    if (value && this.quickSearchQueryString().length > this.MIN_SEARCH_LEN) {
      this.quickSearchPanelVisible_.set(value);
    }
    if (value == false) {
      this.clearSearch();
    }
  }

  public showQuickSearchPanel(): void {
    this.quickSearchPanelVisible_.set(true);
    this.executeSearchWithCurrentQuickSearchValue();
  }

  public hideQuickSearchPanel(): void {
    this.quickSearchPanelVisible_.set(false);
  }

  private searchResultReceived(data: SearchResultDto): void {
    this.quickSearchResultList.set(data);
    console.debug(
      'search result received total music items count: ' + data.musicItemsTotal,
    );
    console.debug(
      'search result received total album items count: ' + data.albumItemsTotal,
    );
    console.debug(
      'search result received total artist items count: ' +
        data.artistItemsTotal,
    );
    console.debug(
      'search result received total playlist items count: ' +
        data.playlistItemsTotal,
    );
  }

  private doSearch(): void {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    if (this.currentSearchText.length > this.MIN_SEARCH_LEN) {
      this.doSearchFunc();
    }
  }

  //
  // Search
  // =========================================================================

  public updateQuickSearchQueryString(value: string): void {
    this.quickSearchQueryString.set(value);
    this.executeSearchWithCurrentQuickSearchValue();
  }

  public executeSearchWithCurrentQuickSearchValue(): void {
    if (this.quickSearchQueryString() === '') {
      this.quickSearchPanelVisible_.set(false);
    } else {
      if (
        this.quickSearchQueryString() &&
        this.quickSearchQueryString().length > this.MIN_SEARCH_LEN
      ) {
        this.quickSearchPanelVisible_.set(true);
        this.currentSearchText = this.quickSearchQueryString().replace(
          /"/g,
          '""',
        );
        this.doSearch();
      }
    }
  }

  clearSearch(): void {
    this.quickSearchQueryString.set('');
    this.quickSearchResultList.set(
      this.dtoGeneratorService.generateEmptySearchResultDto(),
    );
    this.quickSearchPanelVisible_.set(false);
    this.currentSearchText = '';
  }

  public setSelectedContainer(container: ContainerDto): void {
    this.contentDirectoryService.browseChildrenByContainer(container);
  }

  //
  // item or container selected
  //
  musicItemSelected(musicItem: MusicItemDto): void {
    console.debug('music item selected : ' + musicItem);
    this.quickSearchPanelVisible = false;
    this.clearSearch();
    this.musicItemClicked$.next(musicItem);
  }

  containerSelected(container: ContainerDto): void {
    console.debug('container selected : ' + container.id);
    this.quickSearchPanelVisible = false;
    this.clearSearch();
    this.router.navigateByUrl('/music-library/' + container.id);
  }

  //
  // show all clicked
  //

  /**
   * How many result rows a "show all" asks for. The quick-search dropdown shows
   * a handful; this is the full page.
   */
  private static readonly SHOW_ALL_COUNT = 100;

  /**
   * The container a "show all" searches in.
   *
   * The global-search toggle is the only thing that decides this: on, the whole
   * library from the root; off, the folder the library view is showing. Three of
   * the four sections used to read the container from the root
   * ContentDirectoryService instead — an instance the library view never
   * browses, so the id was usually empty and the toggle had no effect on them.
   */
  private showAllContainerId(): string {
    if (this.globalSearch()) {
      return '0';
    }
    const id = this.musicLibraryService.currentContainerId();
    // Searching "in this folder" from a result page must not search inside the
    // synthetic search container.
    return id && id !== SEARCH_RESULT_CONTAINER_ID ? id : '0';
  }

  /** Sort criteria the backend expects per search type. */
  private static readonly SHOW_ALL_SORT: Record<ShowAllType, string> = {
    items: '',
    album: '-ums:likedAlbum',
    artists: '',
    playlists: '',
  };

  showAllItem(): void {
    this.showAllType('items');
  }

  showAllAlbum(): void {
    this.showAllType('album');
  }

  showAllItemArtist(): void {
    this.showAllType('artists');
  }

  showAllPlaylist(): void {
    this.showAllType('playlists');
  }

  /**
   * Shows every hit of one type. Reached from the quick-search dropdown and from
   * the result page's own type bar, which is why it is public: switching from
   * albums to tracks used to mean reopening the dropdown.
   */
  public showAllType(type: ShowAllType): void {
    this.requestShowAll(type, GlobalSearchService.SHOW_ALL_SORT[type]);
  }

  /**
   * How many hits of a type the current query has.
   *
   * Comes from the quick search that filled the dropdown — one request already
   * reports the totals for all four types, so the result page can label its type
   * bar without asking again. Zero once the search is cleared, which is only
   * after the result page has been left.
   */
  public resultTotal(type: ShowAllType): number {
    const result = this.quickSearchResultList();
    switch (type) {
      case 'items':
        return result.musicItemsTotal ?? 0;
      case 'album':
        return result.albumItemsTotal ?? 0;
      case 'artists':
        return result.artistItemsTotal ?? 0;
      case 'playlists':
        return result.playlistItemsTotal ?? 0;
    }
  }

  /**
   * Parks the request, then navigates. The order does not matter any more — the
   * music library view picks the request up whenever it is ready, whether it was
   * already on screen or is being built by this very navigation.
   */
  private requestShowAll(type: ShowAllType, sortCriteria: string): void {
    const request = this.dtoGeneratorService.generateQuickSearchDto(
      this.quickSearchQueryString(),
      this.deviceService.selectedMediaServerDevice().udn,
      sortCriteria,
      this.showAllContainerId(),
      0,
      GlobalSearchService.SHOW_ALL_COUNT,
    );
    console.log('show all ' + type + ' clicked ... ');
    this.pendingShowAll_.set({ type: type, request: request });
    // Panel closed, but the query stays in the box: it labels the page you land
    // on, and searching again from there should not start from nothing.
    this.hideQuickSearchPanel();
    void this.router.navigateByUrl('/music-library').then((navigated) => {
      if (!navigated) {
        // We never arrived, so nobody will pick the request up. Dropping it
        // keeps it from surprising the next visit to the music library.
        this.pendingShowAll_.set(undefined);
      }
    });
  }

  /**
   * Returns the waiting request and clears it, so it cannot be run twice.
   */
  public consumePendingShowAll(): ShowAllRequest | undefined {
    const pending = this.pendingShowAll_();
    if (pending) {
      this.pendingShowAll_.set(undefined);
    }
    return pending;
  }
}
