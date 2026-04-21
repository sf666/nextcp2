import { Router } from '@angular/router';
import { ConfigurationService } from './../configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ContentDirectoryService } from './../content-directory.service';
import {
  SearchResultDto,
  ContainerDto,
  MusicItemDto,
  SearchRequestDto,
} from './../dto.d';
import { Injectable, signal } from '@angular/core';
import { debounce } from 'src/app/global';
import { Subject } from 'rxjs';
import { MusicLibraryService } from '../music-library/music-library.service';

@Injectable({
  providedIn: 'root',
})
export class GlobalSearchService {
  public globalSearch = signal(true);

  //
  // Event publishing
  //

  // User clicked on an quick search music item
  musicItemClicked$: Subject<MusicItemDto> = new Subject();

  // User clicked on an quick search container like album, person or playlist
  containerClicked$: Subject<ContainerDto> = new Subject();

  // User clicked on a SHOW ALL button
  showAllItemClicked$: Subject<SearchRequestDto> = new Subject();
  showAllAlbumClicked$: Subject<SearchRequestDto> = new Subject();
  showAllArtistClicked$: Subject<SearchRequestDto> = new Subject();
  showAllPlaylistClicked$: Subject<SearchRequestDto> = new Subject();

  // QuickSearch Support (Global search)
  public quickSearchResultList = signal<SearchResultDto>(this.dtoGeneratorService.generateEmptySearchResultDto());
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
        containerId
      )
      .subscribe((data) => this.searchResultReceived(data));
  };

  constructor(
    public contentDirectoryService: ContentDirectoryService,
    private deviceService: DeviceService,
    private musicLibraryService: MusicLibraryService,
    private dtoGeneratorService: DtoGeneratorService,
    private router: Router,
    private configurationService: ConfigurationService
  ) {
    this.doSearchFunc = debounce(
      this.getSearchDelay(),
      this.doSearchThrotteled
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
    console.debug('search result received total music items count: ' + data.musicItemsTotal);
    console.debug('search result received total album items count: ' + data.albumItemsTotal);
    console.debug('search result received total artist items count: ' + data.artistItemsTotal);
    console.debug('search result received total playlist items count: ' + data.playlistItemsTotal);
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
        this.currentSearchText = this.quickSearchQueryString().replace(/"/g, '""');
        this.doSearch();
      }
    }
  }

  clearSearch(): void {
    this.quickSearchQueryString.set('');
    this.quickSearchResultList.set(this.dtoGeneratorService.generateEmptySearchResultDto());
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

  get currentContainerID(): string {
    return this.contentDirectoryService.currentContainerList().currentContainer
      .id;
  }

  showAllItem(): void {
    let containerId = '0';
    if (!this.globalSearch()) {
      containerId = this.musicLibraryService.currentContainerId();
    }    
    const sr = this.dtoGeneratorService.generateQuickSearchDto(
      this.quickSearchQueryString(),
      this.deviceService.selectedMediaServerDevice().udn,
      '',
      containerId,
      0,
      100
    );
    this.hideQuickSearchPanel();
    this.router.navigateByUrl('/music-library');
    console.log('show all item clicked ... ');
    this.showAllItemClicked$.next(sr);
  }

  showAllAlbum(): void {
    const sr = this.dtoGeneratorService.generateQuickSearchDto(
      this.quickSearchQueryString(),
      this.deviceService.selectedMediaServerDevice().udn,
      '-ums:likedAlbum',
      this.currentContainerID,
      0,
      100
    );
    this.hideQuickSearchPanel();
    this.router.navigateByUrl('/music-library');
    console.log('show all album clicked ... ');
    this.showAllAlbumClicked$.next(sr);
  }

  showAllItemArtist(): void {
    const sr = this.dtoGeneratorService.generateQuickSearchDto(
      this.quickSearchQueryString(),
      this.deviceService.selectedMediaServerDevice().udn,
      '',
      this.currentContainerID,
      0,
      100
    );
    this.hideQuickSearchPanel();
    this.router.navigateByUrl('/music-library');
    console.log('show all artists clicked ... ');
    this.showAllArtistClicked$.next(sr);
  }

  showAllPlaylist(): void {
    const sr = this.dtoGeneratorService.generateQuickSearchDto(
      this.quickSearchQueryString(),
      this.deviceService.selectedMediaServerDevice().udn,
      '',
      this.currentContainerID,
      0,
      100
    );
    this.hideQuickSearchPanel();
    this.router.navigateByUrl('/music-library');
    console.log('show all playlist clicked ... ');
    this.showAllPlaylistClicked$.next(sr);
  }
}
