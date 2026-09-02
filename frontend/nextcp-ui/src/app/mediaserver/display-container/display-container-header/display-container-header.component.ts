import { ContainerDto, ContainerItemDto } from './../../../service/dto.d';
import {
  Component,
  OnInit,
  input,
  model,
  output,
  signal,
  computed,
  ChangeDetectionStrategy,
  ElementRef,
  inject,
  afterNextRender,
  DestroyRef,
  HostListener,
} from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import {
  ContentDirectoryService,
  SEARCH_TYPE_LABEL,
  ShowAllType,
} from 'src/app/service/content-directory.service';
import { MusicItemDto } from 'src/app/service/dto';
import { GlobalSearchService } from 'src/app/service/search/global-search.service';
import {
  RATING_LIKED,
  RatingFilter,
  RatingServiceService,
} from 'src/app/service/rating-service.service';
import { DeviceService } from 'src/app/service/device.service';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { AlbumArtService } from 'src/app/util/album-art.service';
import { DisplayHeaderOptionsComponent } from '../../popup/display-header-options/display-header-options.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'display-container-header',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  // No Material components left in the template — the header buttons, filter
  // dropdowns and inputs are all plain elements styled in the component's SCSS.
  imports: [FormsModule, ReactiveFormsModule, MatDialogModule],
  templateUrl: './display-container-header.component.html',
  styleUrl: './display-container-header.component.scss',
})
export class DisplayContainerHeaderComponent implements OnInit {
  private dialog = inject(MatDialog);
  private ratingService = inject(RatingServiceService);
  private deviceService = inject(DeviceService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private backgroundImageService = inject(BackgroundImageService);
  private timeDisplayService = inject(TimeDisplayService);
  readonly albumArt = inject(AlbumArtService);
  private destroyRef = inject(DestroyRef);
  private globalSearchService = inject(GlobalSearchService);
  private hostRef: ElementRef<HTMLElement> = inject(ElementRef);

  //
  // Tailwind filter dropdowns (sort / genres)
  /////////////////////////////////////

  // Which custom dropdown is open, if any.
  openMenu = signal<'sort' | 'genres' | 'rating' | null>(null);

  // Kept short on purpose: a digit plus the heart says it without a sentence. ANY
  // has no digit, it is the switched-off state and carries an icon instead. The
  // trailing plus is what tells the reader the entry is a lower bound; 5 has none
  // because there is nothing above it, and 0 is the disliked bucket, not a bound.
  readonly ratingOptions: ReadonlyArray<{
    value: RatingFilter;
    digits?: string;
    icon?: string;
    title: string;
  }> = [
    { value: 'ANY', icon: 'filter_alt_off', title: 'Any rating' },
    { value: '5', digits: '5', title: 'Liked, 5' },
    { value: '4', digits: '4+', title: '4 and better' },
    { value: '3', digits: '3+', title: '3 and better' },
    { value: '2', digits: '2+', title: '2 and better' },
    { value: '1', digits: '1+', title: '1 and better' },
    { value: '0', digits: '0', title: 'Disliked, 0' },
  ];

  ratingOption = computed(
    () =>
      this.ratingOptions.find((o) => o.value === this.ratingFilter()) ??
      this.ratingOptions[0],
  );

  readonly sortOptions: ReadonlyArray<{ value: string; label: string }> = [
    { value: 'NONE', label: 'None' },
    { value: 'TITLE', label: 'Album Title' },
    { value: 'ARTIST', label: 'Album Artist' },
    { value: 'GENRE', label: 'Genre' },
  ];

  sortLabel = computed(
    () =>
      this.sortOptions.find((o) => o.value === this.sortCriteria())?.label ??
      'None',
  );

  genresSummary = computed(() => {
    const g = this.selectedGenres();
    if (!g?.length) {
      return 'Any';
    }
    return g.length === 1 ? g[0] : `${g.length} selected`;
  });

  toggleMenu(menu: 'sort' | 'genres' | 'rating', event: MouseEvent): void {
    event.stopPropagation();
    this.openMenu.update((cur) => (cur === menu ? null : menu));
  }

  closeMenu(): void {
    if (this.openMenu() !== null) {
      this.openMenu.set(null);
    }
  }

  selectSort(value: string): void {
    this.sortCriteria.set(value);
    this.openMenu.set(null);
  }

  isGenreSelected(genre: string): boolean {
    return this.selectedGenres()?.includes(genre) ?? false;
  }

  toggleGenre(genre: string, event: MouseEvent): void {
    // Keep the menu open for multi-select.
    event.stopPropagation();
    const current = this.selectedGenres() ?? [];
    this.selectedGenres.set(
      current.includes(genre)
        ? current.filter((g) => g !== genre)
        : [...current, genre],
    );
  }

  // Close any open dropdown when clicking outside this component.
  @HostListener('document:click')
  onDocumentClick(): void {
    this.closeMenu();
  }

  //
  // Collapsing sticky header
  /////////////////////////////////////

  // Scroll-linked collapse progress, 0 (full hero) .. 1 (compact bar).
  // Driven continuously by scroll position so the header shrinks gradually
  // instead of snapping — a binary toggle fed back into layout height and
  // caused flicker around the threshold.
  collapse = signal<number>(0);
  // Compact mode: buttons move to the right and the page-filter tools hide.
  // Switched once, early in the scroll, so it never affects header height.
  condensed = computed(() => this.collapse() > 0.06);

  // Scroll distance (px) over which the header fully collapses.
  private readonly COLLAPSE_RANGE = 150;
  // Bottom zone (px) where collapse is frozen to avoid macOS overscroll wobble.
  private readonly COLLAPSE_FREEZE_ZONE = 28;
  private scrollParent: HTMLElement | null = null;
  private rafPending = false;
  // The sticky track-list column header, which parks below this one. Cached
  // because it is written to on every scrolled frame; re-resolved whenever the
  // cached node has left the document (a browse replaced the list).
  private listColHeader: HTMLElement | null = null;

  private readonly onScroll = (): void => {
    if (this.rafPending) {
      return;
    }
    this.rafPending = true;
    requestAnimationFrame(() => {
      this.rafPending = false;
      const el = this.scrollParent;
      if (!el) {
        return;
      }
      const y = el.scrollTop;
      // Near the very bottom, macOS inertial overscroll makes scrollTop jitter.
      // Because the collapsing header changes the scrollable height, that jitter
      // would feed back and wobble the header. Hold the collapse value in this
      // bottom zone (the header shouldn't keep changing there anyway) to break
      // the loop — without needing a bottom spacer.
      if (el.scrollHeight - el.clientHeight - y < this.COLLAPSE_FREEZE_ZONE) {
        return;
      }
      const next =
        Math.round(Math.min(1, Math.max(0, y / this.COLLAPSE_RANGE)) * 100) /
        100;
      if (next !== this.collapse()) {
        this.collapse.set(next);
        this.publishCollapse(next);
      }
    });
  };

  /**
   * Hands the current collapse value to the elements whose size depends on it.
   *
   * Deliberately NOT written on the scroll container: a custom property is
   * inherited, so changing it there invalidated the computed style of every node
   * on the page — with a few hundred album tiles in the list that was tens of
   * milliseconds of style recalculation per scrolled frame. The only two
   * consumers are this header and the sticky column header of a track list, so
   * the value goes straight onto those instead.
   */
  private publishCollapse(value: number): void {
    const collapse = String(value);
    this.hostRef.nativeElement.style.setProperty('--collapse', collapse);
    if (!this.listColHeader?.isConnected) {
      // Only a track listing has one, so on an album or folder page this must
      // not turn into a DOM query per scrolled frame. Once found it is kept
      // until the node is replaced by the next browse.
      this.listColHeader = this.hasSongs()
        ? document.querySelector<HTMLElement>(
            '#display-container-main-content.with-sticky-header .list-col-header',
          )
        : null;
    }
    this.listColHeader?.style.setProperty('--collapse', collapse);
  }

  constructor() {
    afterNextRender(() => {
      this.scrollParent = document.getElementById('mainContent');
      this.scrollParent?.addEventListener('scroll', this.onScroll, {
        passive: true,
      });
      this.destroyRef.onDestroy(() => {
        this.scrollParent?.removeEventListener('scroll', this.onScroll);
      });
    });
  }

  //
  // signals
  /////////////////////////////////////

  contentDirectoryService = input.required<ContentDirectoryService>();

  listView = model<boolean>();
  quickSearchString = model<string>();
  selectedGenres = model<Array<string>>([]);
  // Album sort/group control (only shown when enableAlbumSort is true).
  sortCriteria = model<string>('NONE');
  // Narrows the listing down by rating. A model so the container view can pass it
  // on to the tiles, like the other filters.
  ratingFilter = model<RatingFilter>('ANY');
  enableAlbumSort = input<boolean>(false);

  playClicked = output<ContainerDto>();
  shuffleClicked = output<ContainerDto>();
  addToPlaylistClicked = output<ContainerDto>();

  genresList = signal<Array<string>>([]);
  // Rating of the container currently being browsed. 5 is a like, undefined means
  // not rated. The media server resolves an album folder to its release id on its
  // own, so one code path covers albums, folders and playlists alike.
  currentContainerRating = signal<number | undefined>(undefined);
  totalPlaytimeShort = computed(() =>
    this.calcTotalPlaytimeShort(this.contentDirectoryService().musicTracks_()),
  );
  totalPlaytime = computed(() =>
    this.calcTotalPlaytimeLong(this.contentDirectoryService().musicTracks_()),
  );

  // Every container can be liked, not only albums carrying a MusicBrainz or
  // Discogs id. The media server only needs the objectID.
  likePossible = computed(
    () =>
      this.deviceService.selectedMediaServerDevice().extendedApi &&
      (this.currentContainer?.id ?? '').length > 0,
  );
  // An album wears its like next to the title — that is part of what the album
  // is. A plain folder is a place, so liking it is housekeeping and lives in the
  // options menu instead, where the other folder actions are.
  showTitleLike = computed(
    () => this.likePossible() && this.containerType() === 'Album',
  );

  allTracksSameAlbum_ = signal<boolean>(false);
  mediaServerExists = signal<boolean>(false);

  genresForm = new FormControl('');

  /**
   * Reads explicit state instead of sniffing the placeholder artwork's file name.
   * Deliberately the "is displayed" flag and not the search context: the context
   * is set the moment the request goes out, and the header would then already
   * claim "Search" while the previous folder is still on screen.
   */
  isSearchResult = computed(() =>
    this.contentDirectoryService().isSearchResultDisplayed(),
  );

  containerType = computed(() => {
    if (this.isSearchResult()) {
      return 'Search';
    } else if (
      this.currentContainer.objectClass === 'object.container.playlistContainer'
    ) {
      return 'Playlist';
    } else if (
      this.currentContainer.objectClass === 'object.container.album.musicAlbum'
    ) {
      return 'Album';
    } else {
      return 'Folder';
    }
  });

  /**
   * Dominant colours of the first few hits, feeding the hero's bloom.
   *
   * A result set has no cover of its own, and standing a placeholder where an
   * album cover would be is what made this page a black void. So the page takes
   * its colour from what it actually found — the same living-canvas idea the
   * rest of the app applies to a single album, applied to a set.
   */
  private bloomColors = signal<string[]>([]);

  /** Where the bloom's blobs sit, spread across the width of the hero. */
  private static readonly BLOOM_SPOTS = [
    { x: 12, y: 60 },
    { x: 36, y: 45 },
    { x: 62, y: 62 },
    { x: 86, y: 44 },
  ];

  /** How many covers the bloom is mixed from. */
  private static readonly BLOOM_SOURCES = 4;

  searchContext = computed(() =>
    this.contentDirectoryService().searchContext(),
  );

  /**
   * Order of the type bar. Albums first because that is what most searches are
   * aimed at, tracks after the two container kinds, playlists last — they are
   * the rarest hit.
   */
  private static readonly SEARCH_TABS: ShowAllType[] = [
    'album',
    'artists',
    'items',
    'playlists',
  ];

  /**
   * The result page's type bar: every kind of hit the current query has, so
   * switching from albums to tracks is one click instead of reopening the
   * quick-search dropdown.
   */
  searchTabs = computed(() => {
    const context = this.searchContext();
    if (!context) {
      return [];
    }
    return DisplayContainerHeaderComponent.SEARCH_TABS.map((type) => {
      const many = SEARCH_TYPE_LABEL[type].many;
      return {
        type: type,
        label: many.charAt(0).toUpperCase() + many.slice(1),
        count: this.globalSearchService.resultTotal(type),
        active: type === context.type,
      };
    });
  });

  /** Runs the same query against another type. */
  selectSearchType(type: ShowAllType): void {
    if (this.searchContext()?.type === type) {
      return;
    }
    this.globalSearchService.showAllType(type);
  }

  /** "Albums", "Tracks", … — the kind of hits, for the eyebrow. */
  searchTypeLabel = computed(() => {
    const context = this.searchContext();
    if (!context) {
      return '';
    }
    const many = SEARCH_TYPE_LABEL[context.type].many;
    return many.charAt(0).toUpperCase() + many.slice(1);
  });

  /** "42 albums in Music Library" — what was found, and where. */
  searchSummary = computed(() => {
    const context = this.searchContext();
    if (!context) {
      return '';
    }
    const count =
      this.contentDirectoryService().currentContainerList().currentContainer
        .childCount;
    const words = SEARCH_TYPE_LABEL[context.type];
    const noun = count === 1 ? words.one : words.many;
    const where = context.scopeTitle || 'Music Library';
    return count + ' ' + noun + ' in ' + where;
  });

  /**
   * The bloom as a stack of soft radial gradients, or '' while no colour could
   * be read (greyscale covers, no artwork, a media server without CORS) — then
   * the hero simply stays dark instead of showing a broken half-effect.
   */
  bloomBackground = computed(() => {
    const colors = this.bloomColors();
    if (colors.length === 0) {
      return '';
    }
    return colors
      .map((color, i) => {
        const spot =
          DisplayContainerHeaderComponent.BLOOM_SPOTS[
            i % DisplayContainerHeaderComponent.BLOOM_SPOTS.length
          ];
        return (
          'radial-gradient(38% 70% at ' +
          spot.x +
          '% ' +
          spot.y +
          '%, ' +
          color +
          ' 0%, transparent 70%)'
        );
      })
      .join(', ');
  });

  ngOnInit(): void {
    if (this.contentDirectoryService) {
      this.contentDirectoryService()
        .browseFinished$.pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((data) => this.cdsBrowseFinished());
      // browseFinished$ is a plain Subject, and this header only enters the DOM once a container
      // has been browsed - so on a reload straight into a container the emission has already been
      // and gone before there was anyone to hear it, and the page kept the black background until
      // the next browse. Apply what is on screen once, which is what the subscription would have.
      if ((this.currentContainer?.id ?? '').length > 0) {
        this.cdsBrowseFinished();
      }
    }
  }

  private cdsBrowseFinished() {
    console.log('cdsBrowseFinished ... ');
    // A fresh browse result starts at the top, so show the full hero header.
    this.collapse.set(0);
    // The list is being replaced — drop the cached node with it.
    this.listColHeader = null;
    this.publishCollapse(0);
    this.clearSearch();
    this.fillGenres();
    this.readContainerRating();
    // A result set has no cover of its own. Blowing one hit's artwork up behind
    // the whole page would misrepresent the set, so the image washes stay empty
    // here and the hero's bloom carries the colour instead.
    const searching = this.isSearchResult();
    const artUrl = searching ? '' : this.currentContainer.albumartUri;
    // A container that was re-read because the server said it changed is still the same container,
    // so a reply without a cover means "not reported this time", not "has none". Clearing on that
    // drops the wash to black for a moment and it flickers back - keep what is up instead. Only a
    // real navigation may clear, because there the empty genuinely belongs to the new container.
    const keepLastImage =
      !artUrl && this.contentDirectoryService().isInPlaceRefresh();
    if (!keepLastImage) {
      this.backgroundImageService.setDisplayContainerHeaderImage(artUrl);
      // Drive the full-screen "living canvas" wash from the item currently being
      // browsed (always present), so the frosted chrome reliably picks up the
      // colour you are looking at — instead of the often-dark now-playing art.
      this.backgroundImageService.setBackgroundImageMainScreen(artUrl);
    }
    if (searching) {
      this.updateSearchBloom();
    } else {
      this.bloomColors.set([]);
      // Extract the cover's dominant colour to reliably tint the sidebar.
      if (!keepLastImage) {
        this.backgroundImageService.applyAmbientTint(artUrl);
      }
    }
    // Scroll restore is centralized in DisplayContainerComponent (it must prefer
    // the virtualized album grid, whose target may not be in the DOM). Do not
    // trigger a competing getElementById-based restore here.
  }

  /**
   * Reads the dominant colour of the first few hits and hands the set to the
   * bloom. Colours that cannot be read are dropped rather than substituted, so
   * the bloom is always made of real artwork or is not drawn at all.
   */
  private updateSearchBloom(): void {
    const urls = this.searchBloomSources();
    if (urls.length === 0) {
      this.bloomColors.set([]);
      this.backgroundImageService.applyAmbientTint('');
      return;
    }
    // The first hit also tints the chrome, so sidebar and footer belong to the
    // same result the bloom does.
    this.backgroundImageService.applyAmbientTint(urls[0]);
    Promise.all(
      urls.map((url) => this.backgroundImageService.extractTintColor(url)),
    ).then((colors) => {
      // Still the same result on screen? A fast second search must not repaint
      // the new hero with the old hits' colours.
      if (this.isSearchResult()) {
        this.bloomColors.set(
          colors.filter((color): color is string => !!color),
        );
      }
    });
  }

  /** Artwork of the first few hits, whichever kind of hit this result holds. */
  private searchBloomSources(): string[] {
    const cds = this.contentDirectoryService();
    const urls = [
      ...cds.albumList_().map((album) => album.albumartUri),
      ...cds.artistList_().map((artist) => artist.albumartUri),
      ...cds.playlistList_().map((playlist) => playlist.albumartUri),
      ...cds.containerList_().map((container) => container.albumartUri),
      ...cds.musicTracks_().map((track) => track.albumArtUrl),
    ];
    return urls
      .filter((url) => !!url)
      .slice(0, DisplayContainerHeaderComponent.BLOOM_SOURCES);
  }

  get albums(): ContainerDto[] {
    return this.contentDirectoryService().albumList_();
  }

  // Runs over the whole browse result, so it stays free of per-item logging: on a
  // library with a few thousand albums those calls were a measurable part of the
  // time between the browse response and the first painted tile.
  private fillGenres(): void {
    const mySet = new Set<string>();
    this.musicTracks?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          mySet.add(gen.trim());
        });
      }
    });
    this.albums?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          mySet.add(gen.trim());
        });
      }
    });

    this.genresList.set(Array.from(mySet.values()).sort());
  }

  /**
   * The rating of the current container comes with the browse result, so no extra
   * roundtrip is needed.
   */
  private readContainerRating() {
    this.currentContainerRating.set(this.currentContainer?.rating ?? undefined);
  }

  get isContainerAlbum(): boolean {
    // TODO can/should also be identified by other means
    return this.likePossible();
  }

  hasSongs(): boolean {
    return this.musicTracks?.length > 0 ? true : false;
  }

  // True when the current container actually lists albums (e.g. "My Albums").
  // The album sort/group control only makes sense then — not on a single
  // album's track list or a plain folder.
  hasAlbums(): boolean {
    return this.albums?.length > 0 ? true : false;
  }

  hasGenres(): boolean {
    return this.genresList()?.length > 0 ? true : false;
  }

  //
  // Search support
  // ===============================================================================================

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.clearSearch();
    }
  }

  public clearSearch(): void {
    this.quickSearchString.set('');
  }

  public clearGenres(event: MouseEvent): void {
    event.stopPropagation();
    this.selectedGenres.set([]);
  }

  //
  // Like section
  // ==============================================================================

  isLiked(): boolean {
    return this.currentContainerRating() === RATING_LIKED;
  }

  /**
   * Toggles the like of the folder currently being browsed. The media server
   * decides what the like belongs to: a folder holding exactly one release is
   * rated as that album and shows up in My Albums, anything else is rated under
   * its own resource key. Removing a like clears the rating, it does not store a
   * dislike.
   */
  toggleLike(): void {
    if (!this.likePossible()) {
      console.log('cannot rate the current container');
      return;
    }
    const previousRating = this.currentContainerRating();
    const newRating = this.isLiked() ? undefined : RATING_LIKED;
    this.ratingService
      .setResourceRating(
        this.currentContainer.id,
        previousRating,
        newRating,
        this.currentContainer.id,
        this.currentContainer.objectClass,
      )
      .subscribe({
        next: () => this.currentContainerRating.set(newRating),
        error: (err) => console.log('cannot rate container : ' + err),
      });
  }

  selectRating(value: RatingFilter): void {
    this.ratingFilter.set(value);
    this.openMenu.set(null);
  }

  toggleListView(): void {
    if (this.listView) {
      this.listView.update((lv) => (lv = !lv));
    }
  }

  //
  // Accessor
  //
  get musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService().musicTracks_();
  }

  //
  // Statistics
  // ===============================================================================================

  public get musicItemsCount(): number {
    if (this.musicTracks?.length) {
      return this.musicTracks.length;
    } else {
      return 0;
    }
  }

  private calcTotalPlaytimeLong(tracks: MusicItemDto[]): string {
    const completeTime = this.getTotalTimeSeconds(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return '';
  }

  private calcTotalPlaytimeShort(tracks: MusicItemDto[]): string {
    const completeTime = this.getTotalTimeSeconds(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateStringShort(completeTime);
    } else {
      return '';
    }
  }

  private getTotalTimeSeconds(tracks: MusicItemDto[]): number {
    let completeTime: number;
    completeTime = 0;
    if (tracks.length > 0) {
      tracks.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0)),
      );
    }
    console.log('total playtime seconds : ' + completeTime);
    return completeTime;
  }

  //
  // Button actions
  // ===============================================================================================
  public playContainer(): void {
    console.log('play container clicked ... ');
    this.playClicked.emit(this.currentContainer);
  }

  public shuffleContainer(): void {
    console.log('shuffle container clicked ... ');
    this.shuffleClicked.emit(this.currentContainer);
  }

  public addToPlaylist(): void {
    console.log('add to playlist clicked ... ');
    this.addToPlaylistClicked.emit(this.currentContainer);
  }

  public openOptionsDialog(event: MouseEvent): Observable<any> {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(DisplayHeaderOptionsComponent, {
      hasBackdrop: true,
      // Transparent, shadow-less wrapper — the component's glass panel is the
      // whole surface (see app-styles.scss).
      panelClass: ['popup-glass'],
      data: {
        trigger: target,
        addToPlaylistOutput: this.addToPlaylistClicked,
        event: event,
        currentContainer: this.currentContainer,
        // The like only shows up here for containers that do not wear it next
        // to their title (see showTitleLike).
        canLike: this.likePossible() && !this.showTitleLike(),
        isLiked: this.isLiked(),
        // Child folders only (albums live in albumDto) - "Set artist folder" needs a folder
        // that holds artist folders, so the menu decides from this count.
        childFolderCount: this.currentContainerItem().containerDto?.length ?? 0,
      },
    });
    // The menu only reports the choice; the rating call stays here, where the
    // container's rating state lives.
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'toggleLike') {
        this.toggleLike();
      }
    });
    return dialogRef.afterClosed();
  }

  // Other
  public get currentContainer(): ContainerDto {
    if (
      this.contentDirectoryService().currentContainerList().currentContainer
    ) {
      return this.contentDirectoryService().currentContainerList()
        .currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  public currentContainerItem(): ContainerItemDto {
    if (this.contentDirectoryService().currentContainerList()) {
      return this.contentDirectoryService().currentContainerList();
    }

    console.log('no current container item found, returning empty one');
    return this.dtoGeneratorService.generateEmptyContainerItemDto();
  }
}
