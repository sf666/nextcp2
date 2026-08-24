import { ConfigurationService } from './configuration.service';
import { DeviceService } from './device.service';
import { ToastService } from './toast/toast.service';
import {
  PlaylistStructureChange,
  ServerPlaylistService,
} from './server-playlist.service';
import {
  map,
  mergeMap,
  Observable,
  range,
  Subject,
  take,
  takeUntil,
} from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { BrowseThrottleService } from './browse-throttle.service';
import { HttpService } from './http.service';
import {
  ContainerItemDto,
  BrowseRequestDto,
  ContainerDto,
  SearchRequestDto,
  SearchResultDto,
  MusicItemDto,
  MusicAlbumIds,
} from './dto.d';
import {
  computed,
  DestroyRef,
  inject,
  Injectable,
  signal,
} from '@angular/core';
import { isAssigned } from '../global';

/** One clickable step of the browse path shown in the nav bar. */
export interface BrowseCrumb {
  id: string;
  title: string;
}

/**
 * How far up the tree the breadcrumb will chase parent ids before giving up.
 * A guard against a cyclic or absurdly deep tree, not a real depth limit —
 * shares are rarely nested this far.
 */
const ANCESTOR_LOOKUP_LIMIT = 12;

/**
 * Object id of the synthetic container that holds search hits. It exists only in
 * the browser, so it must never be handed back to the media server as a browse
 * target.
 */
export const SEARCH_RESULT_CONTAINER_ID = 'search_result';

/**
 * Container classes that get a section of their own. Everything else in
 * containerDto is shown as a folder.
 */
const PLAYLIST_CONTAINER_CLASS = 'object.container.playlistContainer';
const ARTIST_CONTAINER_CLASS = 'object.container.person.musicArtist';

/**
 * What the view is showing when it shows search hits instead of a folder.
 *
 * Search results are not a place in the media server's tree, so the breadcrumb
 * cannot describe them and cannot lead out of them either. This carries the one
 * thing needed for a way back — the folder that was on screen when the search
 * started — next to what the result actually is.
 */
export interface SearchContext {
  /** What the user searched for. */
  query: string;
  /** Which of the four quick-search sections the hits came from. */
  type: ShowAllType;
  /** Object id of the folder to return to. */
  returnObjectId: string;
  /** Title of that folder, empty when it was never browsed in this view. */
  returnTitle: string;
  /**
   * Folder the search was limited to, empty when it covered the whole library.
   * The header names it, so "in Elektro-Klassiker" versus "in Music Library" is
   * visible rather than something the user has to remember toggling.
   */
  scopeTitle: string;
}

/**
 * Which of the four quick-search sections a "show all" came from.
 *
 * Declared here rather than next to the search service: that service already
 * imports this one, so the reverse direction would be a circular import.
 */
export type ShowAllType = 'items' | 'album' | 'artists' | 'playlists';

/** Backend endpoint per search type. */
const SEARCH_URI: Record<ShowAllType, string> = {
  items: '/searchAllItems',
  album: '/searchAllAlbum',
  artists: '/searchAllArtists',
  playlists: '/searchAllPlaylist',
};

/**
 * What the hits are called, for "42 albums" / "1 album". Exported because the
 * header names the result set and this is the only place the wording lives.
 */
export const SEARCH_TYPE_LABEL: Record<
  ShowAllType,
  { one: string; many: string }
> = {
  items: { one: 'track', many: 'tracks' },
  album: { one: 'album', many: 'albums' },
  artists: { one: 'artist', many: 'artists' },
  playlists: { one: 'playlist', many: 'playlists' },
};

@Injectable()
export class ContentDirectoryService {
  configService = inject(ConfigurationService);
  private httpService = inject(HttpService);
  private browseThrottle = inject(BrowseThrottleService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private deviceService = inject(DeviceService);
  private toastService = inject(ToastService);
  private serverPlaylistService = inject(ServerPlaylistService);

  baseUri = '/ContentDirectoryService';

  //
  // signals
  // ==========================================================

  public currentContainerList = signal<ContainerItemDto>(
    this.dtoGeneratorService.generateEmptyContainerItemDto(),
  );

  /**
   * Set while this view shows search hits, undefined while it shows a folder.
   * This is the *intent*, set the moment the request goes out — the view needs
   * to know not to browse long before the server answers.
   */
  public searchContext = signal<SearchContext | undefined>(undefined);

  /**
   * Whether what is actually rendered are search hits. This is the *fact*, so
   * unlike searchContext it does not flip while the previous folder is still on
   * screen — which is what anything describing the current content needs.
   */
  public isSearchResultDisplayed = computed(
    () =>
      this.currentContainerList().currentContainer?.id ===
      SEARCH_RESULT_CONTAINER_ID,
  );

  isCurrentContainerRoot = computed(() => {
    return (
      (isAssigned(this.currentContainerList().currentContainer) &&
        this.currentContainerList().currentContainer?.id === '0') ||
      this.currentContainerList().currentContainer?.parentID === '-1' ||
      this.currentContainerList().currentContainer?.id.length == 0
    );
  });

  //
  // Breadcrumb path
  // ==========================================================
  // The ancestors of the current container, root excluded (the home crumb is
  // always rendered). A browse response only ever describes the container it
  // returns plus its parent's title, so the chain is accumulated here as the
  // user navigates rather than fetched.

  public browsePath = signal<BrowseCrumb[]>([]);

  /**
   * The container this view treats as its top level. Each sidebar entry is its
   * own tree — Radio Networks starts at the AudioAddict node, My Albums at the
   * MyMusic node — so "root" cannot be hard-coded to object id 0. Views that
   * start elsewhere register their root via SETBROWSEROOT.
   */
  private browseRootId = '0';

  /**
   * Container whose ancestors are currently being looked up, so a second browse
   * of the same target does not start the walk again.
   */
  private ancestorLookupFor = '';

  /**
   * Bumped by every direct write to the path. An ancestor lookup carries the
   * value it started with and discards its result if it no longer matches, so a
   * slow walk can never overwrite a newer, better-informed path — most notably
   * the one SETBROWSEROOT writes once a view reports its own top level.
   */
  private pathGeneration = 0;

  /** Single point of truth for writing the path, so nothing can race it. */
  private setPath(crumbs: BrowseCrumb[], truncated: boolean): void {
    this.pathGeneration++;
    this.browsePath.set(crumbs);
    this.browsePathTruncated.set(truncated);
  }

  /**
   * True when we could not reconstruct the whole chain — after a reload, a
   * search hit or any other jump into the middle of the tree. The nav bar shows
   * an ellipsis so the path does not claim to start at the root.
   */
  public browsePathTruncated = signal<boolean>(false);

  /**
   * Declares which container is this view's top level.
   *
   * Views resolve their root asynchronously (the id behind a `$DBID$…` alias is
   * only known once the server answers), so this may arrive after the root page
   * has already been folded into the path — hence it also corrects what is
   * currently on screen instead of only affecting later browses.
   */
  public setBrowseRoot(id: string | undefined): void {
    if (!id) {
      return;
    }
    // Deliberately no early exit when the id is unchanged: a view re-announces
    // its root on every (re)load, and the path it needs to correct is a fresh
    // one each time.
    this.browseRootId = id;
    // Any ancestor walk started before we knew the root would climb past it —
    // into the media server's own tree — and report a parent this view should
    // never show. Invalidate it.
    this.ancestorLookupFor = '';

    const current = this.currentContainerList().currentContainer;
    if (!current?.id) {
      // Nothing on screen yet; still void any walk in flight.
      this.pathGeneration++;
      return;
    }

    if (current.id === id) {
      this.setPath([], false);
      return;
    }

    const at = this.browsePath().findIndex((crumb) => crumb.id === id);
    if (at >= 0) {
      this.setPath(this.browsePath().slice(at + 1), false);
      return;
    }

    // The root is neither where we are nor on the path: the path was built
    // without knowing it, so anything above the current container is suspect.
    // Start over from here and, if needed, walk again — this time the walk
    // knows where to stop.
    const parentIsRoot = !current.parentID || current.parentID === id;
    this.setPath([{ id: current.id, title: current.title }], !parentIsRoot);
    if (!parentIsRoot) {
      this.resolveAncestors(current);
    }
  }

  private isBrowseRoot(container: ContainerDto): boolean {
    return (
      container.id === this.browseRootId ||
      container.id === '0' ||
      container.parentID === '-1'
    );
  }

  /**
   * Folds a freshly browsed container into the path.
   *
   * Four cases: the root resets it, a container already on the path truncates
   * back to it (stepping out), a child of the last crumb extends it, and
   * anything else is a jump we cannot place — then we keep what the response
   * tells us (parent title + current) and mark the path truncated.
   */
  private reconcileBrowsePath(data: ContainerItemDto): void {
    const current = data?.currentContainer;
    if (!current?.id) {
      return;
    }
    // Search hits live in a synthetic container that exists only in the browser.
    // It is not a place in the tree, so it has no ancestors to show — and left
    // to the jump branch below, resolveAncestors would fire a dozen browse
    // requests building a chain above a container the server never heard of.
    // Resetting the path also bumps pathGeneration, voiding any walk in flight.
    if (current.id === SEARCH_RESULT_CONTAINER_ID) {
      this.setPath([], false);
      return;
    }
    if (this.isBrowseRoot(current)) {
      this.setPath([], false);
      return;
    }

    const path = this.browsePath();
    const known = path.findIndex((crumb) => crumb.id === current.id);
    if (known >= 0) {
      if (known < path.length - 1) {
        this.setPath(path.slice(0, known + 1), this.browsePathTruncated());
      }
      return;
    }

    const last = path[path.length - 1];
    if (last && last.id === current.parentID) {
      this.setPath(
        [...path, { id: current.id, title: current.title }],
        this.browsePathTruncated(),
      );
      return;
    }

    // A jump we cannot place — a reload, a restored deep link, or coming back to
    // a view that was rebuilt. The response carries no ancestor titles, so show
    // where we are now and fetch the chain above it.
    const parentId = current.parentID;
    const parentIsRoot =
      !parentId ||
      parentId === '0' ||
      parentId === '-1' ||
      parentId === this.browseRootId;
    this.setPath([{ id: current.id, title: current.title }], !parentIsRoot);
    if (!parentIsRoot) {
      this.resolveAncestors(current);
    }
  }

  /**
   * Walks parentID upwards and fills in the crumbs we could not derive.
   *
   * Without this the breadcrumb would show a lone "…" that opens an empty menu:
   * the path looks navigable but nothing is clickable. One request per level,
   * only after a jump, and capped so a cyclic or pathologically deep tree
   * cannot spin.
   */
  private resolveAncestors(from: ContainerDto): void {
    const anchorId = from.id;
    if (this.ancestorLookupFor === anchorId) {
      return;
    }
    this.ancestorLookupFor = anchorId;
    const generation = this.pathGeneration;

    const udn =
      from.mediaServerUDN || this.deviceService.selectedMediaServerDevice().udn;
    const chain: BrowseCrumb[] = [];
    const seen = new Set<string>([anchorId]);

    let finished = false;
    const stale = () =>
      // Someone wrote the path while we were fetching. That writer knew more
      // than we did when we started — a view declaring its own root, or the
      // user navigating on — so our result is obsolete either way.
      finished ||
      this.pathGeneration !== generation ||
      this.ancestorLookupFor !== anchorId ||
      this.currentContainerList().currentContainer?.id !== anchorId;

    const done = (truncated: boolean) => {
      if (stale()) {
        return;
      }
      finished = true;
      this.setPath([...chain, { id: from.id, title: from.title }], truncated);
    };

    const step = (parentId: string, depth: number): void => {
      if (stale()) {
        return;
      }
      if (depth >= ANCESTOR_LOOKUP_LIMIT || seen.has(parentId)) {
        done(true);
        return;
      }
      seen.add(parentId);

      this.browseContainerMeta(parentId, udn).subscribe({
        next: (data) => {
          const container = data?.currentContainer;
          if (!container?.id) {
            done(true);
            return;
          }
          chain.unshift({ id: container.id, title: container.title });
          const next = container.parentID;
          if (
            !next ||
            next === '0' ||
            next === '-1' ||
            next === this.browseRootId ||
            this.isBrowseRoot(container)
          ) {
            done(false);
            return;
          }
          step(next, depth + 1);
        },
        error: () => done(true),
      });
    };

    step(from.parentID, 0);
  }

  /**
   * Fetches just enough of a container to name it in the breadcrumb. Asks for a
   * single child so a folder with thousands of entries stays cheap; the answer
   * is never routed through UPDATECONTAINER, so it cannot disturb the view.
   */
  private browseContainerMeta(
    objectID: string,
    mediaServerUdn: string,
  ): Observable<ContainerItemDto> {
    const request = this.createBrowseRequest(objectID, '', mediaServerUdn);
    return this.browseThrottle.schedule(
      () =>
        this.httpService.post<ContainerItemDto>(this.baseUri, '/browseChildren', {
          ...request,
          start: 0,
          count: 1,
        }),
      'reading folder info',
    );
  }

  // result container split by types
  albumList_ = signal<ContainerDto[]>([]);
  containerList_ = signal<ContainerDto[]>([]);
  playlistList_ = signal<ContainerDto[]>([]);
  artistList_ = signal<ContainerDto[]>([]);

  // item treatment
  musicTracks_ = signal<MusicItemDto[]>([]);
  // Raw (unfiltered) non-audio items of the current browse result, accumulated across pages.
  private rawOtherItems_ = signal<MusicItemDto[]>([]);
  // Displayed non-audio items. Reactive: re-filters instantly when the browse result changes OR
  // the "show image items" setting is toggled - no re-browsing needed. Image items are hidden
  // unless the user enabled them (default off).
  otherItems_ = computed(() => {
    const showImages = this.configService.showImageItems();
    return this.rawOtherItems_().filter(
      (item) =>
        showImages ||
        item.objectClass?.lastIndexOf('object.item.imageItem', 0) !== 0,
    );
  });

  // notify other about content change
  browseFinished$: Subject<ContainerItemDto> = new Subject();

  // to which page was browsed

  private TURN_PAGE_AFTER = 60;
  private MAX_REQUEST_ITEMS = 200;
  private PAGE_REQUEST_CONCURRENCY = 4;
  private turn_page_id: string | undefined;
  private browseRequestAbort$ = new Subject<void>();
  private searchRequestAbort$ = new Subject<void>();

  private id = 'id_' + Math.random().toString(16).slice(2);
  private destroyRef = inject(DestroyRef);

  constructor() {
    const configService = this.configService;

    console.log('[ContentDirectoryService-' + this.id + '] : constructor call');
    // Initialize empty result object
    if (configService.applicationConfig.nextPageAfter) {
      this.TURN_PAGE_AFTER = configService.applicationConfig.nextPageAfter;
    }
    if (configService.applicationConfig.itemsPerPage) {
      this.MAX_REQUEST_ITEMS = configService.applicationConfig.itemsPerPage;
    }

    this.serverPlaylistService.playlistStructureChanged$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((change) => this.afterPlaylistStructureChanged(change));
  }

  /**
   * Creating or deleting a playlist happens outside any browse view — from the
   * sidebar, from a song's context menu — so the list on screen still shows the
   * state of the last browse. Browse again, but only in the view that is
   * actually affected: every view has its own instance of this service, and a
   * search result panel must not have its hits replaced by a browse.
   */
  private afterPlaylistStructureChanged(change: PlaylistStructureChange): void {
    const current = this.currentContainerID;
    // Nothing shown yet, or what is shown are search hits — a container the
    // media server does not know, so it cannot be browsed again.
    if (!current || current === SEARCH_RESULT_CONTAINER_ID) {
      return;
    }

    // What is on screen is the playlist that was just deleted. Browsing it again
    // would ask the server for an object it no longer has, so step out to the
    // folder it was in — the only place left to show.
    if (change.removedObjectId === current) {
      this.browseToParent('');
      return;
    }

    const createdHere = change.containerId === current;
    const removedFromHere =
      !!change.removedObjectId && this.isDisplayed(change.removedObjectId);
    // The playlist folder holds exactly what just changed, so it is stale even
    // when the id lookup above found nothing: the playlist dialog also lists
    // playlists it found by search, and a search hit can carry a different
    // object id than the same playlist has when browsed.
    const showsPlaylistFolder = current === this.configuredPlaylistFolderId();

    if (createdHere || removedFromHere || showsPlaylistFolder) {
      this.refreshCurrentContainer();
    }
  }

  /** Object id of the folder holding the user's playlists, '' if unconfigured. */
  private configuredPlaylistFolderId(): string {
    const udn = this.deviceService.selectedMediaServerDevice().udn;
    return this.configService.findServerConfig(udn)?.playistObjectId ?? '';
  }

  /** True if the given object id is one of the rows currently on screen. */
  private isDisplayed(objectId: string): boolean {
    return (
      this.playlistList_().some((pl) => pl.id === objectId) ||
      this.artistList_().some((artist) => artist.id === objectId) ||
      this.containerList_().some((c) => c.id === objectId) ||
      this.musicTracks_().some((item) => item.objectID === objectId) ||
      this.rawOtherItems_().some((item) => item.objectID === objectId)
    );
  }

  //
  // Container and item lists of current media folder
  // --------------------------------------------------------------------------------------------
  //

  public minimTagsList(): ContainerDto[] {
    return this.currentContainerList().minimServerSupportTags;
  }

  public browseToParent(
    sortCriteria: string,
    mediaServerUdn?: string,
  ): Observable<ContainerItemDto> {
    if (!this.isCurrentContainerRoot()) {
      return this.browseChildren(
        this.currentContainerList().currentContainer.parentID,
        sortCriteria,
        mediaServerUdn,
      );
    }
    return new Subject<ContainerItemDto>();
  }

  /**
   *
   * @param objectID
   * @param sortCriteria
   * @param mediaServerUdn
   */
  public browseChildren(
    objectID: string,
    sortCriteria: string,
    mediaServerUdn?: string,
  ): Observable<ContainerItemDto> {
    if (!mediaServerUdn) {
      if (!this.deviceService.selectedMediaServerDevice().udn) {
        this.toastService.error('select media server', 'MediaLibrary');
        return new Subject<ContainerItemDto>();
      } else {
        mediaServerUdn = this.deviceService.selectedMediaServerDevice().udn;
      }
    }
    let browseRequestDto = this.createBrowseRequest(
      objectID,
      sortCriteria,
      mediaServerUdn,
    );
    return this.browseChildrenByRequest(browseRequestDto);
  }

  /**
   * Asks the media server only for the number of children of a container.
   *
   * A count is cheap to ask for but not cheap to answer: a server has to know its children to count
   * them, which for a playlist means reading it and resolving every track. So this is called where
   * the number is actually shown, never to build a list.
   *
   * @param objectID container to count
   * @param mediaServerUdn media server to ask, defaults to the selected one
   */
  public browseChildCount(
    objectID: string,
    mediaServerUdn?: string,
  ): Observable<number> {
    if (!mediaServerUdn) {
      mediaServerUdn = this.deviceService.selectedMediaServerDevice().udn;
    }
    const browseRequestDto = this.createBrowseRequest(objectID, '', mediaServerUdn);
    return this.browseThrottle
      .schedule(
        () =>
          this.httpService.post<ContainerItemDto>(this.baseUri, '/browseChildren', {
            ...browseRequestDto,
            start: 0,
            count: 0,
          }),
        'counting entries',
      )
      .pipe(map((res) => res.totalMatches));
  }

  /**
   * Browse a container WITHOUT updating the displayed content signals.
   *
   * Use this to inspect a container (e.g. resolve the real root object id or
   * check service availability) when the result must NOT be rendered to the
   * user. The returned data is delivered only to the caller's subscription.
   *
   * @param objectID
   * @param sortCriteria
   * @param mediaServerUdn
   */
  public browseChildrenMetadataOnly(
    objectID: string,
    sortCriteria: string,
    mediaServerUdn?: string,
  ): Observable<ContainerItemDto> {
    if (!mediaServerUdn) {
      mediaServerUdn = this.deviceService.selectedMediaServerDevice().udn;
    }
    if (!mediaServerUdn || mediaServerUdn.length < 1) {
      console.log(this.id + ' UDN not set. Stop metadata browse.');
      return new Subject<ContainerItemDto>();
    }
    const browseRequestDto = this.createBrowseRequest(
      objectID,
      sortCriteria,
      mediaServerUdn,
    );
    return this.browseThrottle.schedule(
      () =>
        this.httpService.post<ContainerItemDto>(this.baseUri, '/browseChildren', {
          ...browseRequestDto,
          start: 0,
          count: this.MAX_REQUEST_ITEMS,
        }),
      'loading folder',
    );
  }

  public browseChildrenByContainer(
    containerDto: ContainerDto,
    sortCriteria?: string,
  ): Observable<ContainerItemDto> {
    return this.browseChildrenByOID(
      containerDto.id,
      containerDto.mediaServerUDN,
      sortCriteria,
    );
  }

  public browseChildrenByOID(
    oid: string,
    udn: string,
    sortCriteria?: string,
  ): Observable<ContainerItemDto> {
    if (!oid) {
      oid = '0';
    }
    let browseRequestDto = this.createBrowseRequest(oid, sortCriteria, udn);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public searchCurrentContainer(searchStr: string): Observable<ContainerItemDto> {
    // At this time, we filter the content by posting a browse request and afterwards a manual filter (backend)
    return this.browseChildrenByRequest(
      this.createBrowseRequest(
        this.currentContainerList().currentContainer.id,
        '',
        this.currentContainerList().currentContainer.mediaServerUDN,
        searchStr,
      ),
    );
  }

  private browseChildrenByRequest(
    browseRequestDto: BrowseRequestDto,
  ): Observable<ContainerItemDto> {
    if (browseRequestDto.mediaServerUDN?.length < 1) {
      console.log(this.id + ' UDN not set. Stop browsing.');
      return new Subject<ContainerItemDto>();
    }

    // Abort stale paging streams when a new browse request starts.
    this.browseRequestAbort$.next();
    // A real browse replaces whatever hits were shown, so there is nothing left
    // to go back from. Done here — the single funnel for every browse that
    // renders — and at request time rather than on the response, so the view
    // never sees a stale search context while a browse is already on its way.
    this.searchRequestAbort$.next();
    this.searchContext.set(undefined);
    const browseStartedAt = performance.now();

    const firstPage$ = this.browseThrottle.schedule(
      () =>
        this.httpService.post<ContainerItemDto>(this.baseUri, '/browseChildren', {
          ...browseRequestDto,
          start: 0,
          count: this.MAX_REQUEST_ITEMS,
        }),
      'loading folder',
    );

    firstPage$
      .pipe(
        take(1),
        takeUntil(this.browseRequestAbort$),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (firstPage) => {
          this.updateContainer(firstPage);

          const firstPageDuration = Math.round(
            performance.now() - browseStartedAt,
          );
          console.log(
            this.id + ' : first page loaded in ' + firstPageDuration + ' ms',
          );

          const totalItems = firstPage.totalMatches;
          const totalPages = Math.ceil(totalItems / this.MAX_REQUEST_ITEMS);
          console.log(
            'total items: ' + totalItems + ', total pages: ' + totalPages,
          );
          if (totalPages <= 1) {
            console.log(
              this.id +
                ' : browse finished in ' +
                firstPageDuration +
                ' ms (single page)',
            );
            return;
          }

          console.log(
            this.id + ' : loading ' + (totalPages - 1) + ' remaining pages',
          );

          const bufferedPages = new Map<number, ContainerItemDto>();
          let nextPageToApply = 1;

          range(1, totalPages - 1)
            .pipe(
              mergeMap(
                (page) =>
                  this.browseThrottle
                    .schedule(
                      () =>
                        this.httpService.post<ContainerItemDto>(
                          this.baseUri,
                          '/browseChildren',
                          {
                            ...browseRequestDto,
                            start: page * this.MAX_REQUEST_ITEMS,
                            count: this.MAX_REQUEST_ITEMS,
                          },
                        ),
                      'loading page ' + (page + 1),
                    )
                    .pipe(
                      take(1),
                      map((data) => ({ page, data })),
                    ),
                this.PAGE_REQUEST_CONCURRENCY,
              ),
              takeUntil(this.browseRequestAbort$),
              takeUntilDestroyed(this.destroyRef),
            )
            .subscribe({
              next: ({ page, data }) => {
                bufferedPages.set(page, data);
                while (bufferedPages.has(nextPageToApply)) {
                  this.addContainer(bufferedPages.get(nextPageToApply)!);
                  bufferedPages.delete(nextPageToApply);
                  nextPageToApply++;
                }

                if (nextPageToApply >= totalPages) {
                  const totalDuration = Math.round(
                    performance.now() - browseStartedAt,
                  );
                  console.log(
                    this.id +
                      ' : browse finished in ' +
                      totalDuration +
                      ' ms (' +
                      totalPages +
                      ' pages)',
                  );
                }
              },
              error: (err) =>
                console.error(this.id + ' : browse page error', err),
            });
        },
        error: (err) => console.error(this.id + ' : browse error', err),
      });

    return firstPage$;
  }

  private getPageItemCount(data: ContainerItemDto): number {
    return (
      (data.albumDto?.length ?? 0) +
      (data.containerDto?.length ?? 0) +
      (data.musicItemDto?.length ?? 0)
    );
  }

  // Pagination is handled automatically in browseChildrenByRequest.
  public browseToNextPage(): Observable<ContainerItemDto> {
    return new Subject<ContainerItemDto>();
  }

  public refreshCurrentContainer(): void {
    const browseRequestDto = this.createBrowseRequest(
      this.currentContainerID,
      '',
      this.deviceService.selectedMediaServerDevice().udn,
    );
    this.browseChildrenByRequest(browseRequestDto);
  }

  /**
   * @param data Gets called after a browse request returns ...
   */
  public updateContainer(data: ContainerItemDto): void {
    //    console.log("CDS " + this.id + " : updating container with " + data.musicItemDto.length + " items.");
    if (data) {
      console.log(
        'Album ids MBID / discogs : ' +
          data.allTracksSameAlbumIds?.musicBrainzAlbumId +
          ' / ' +
          data.allTracksSameAlbumIds?.discogsReleaseId,
      );
      this.currentContainerList.set(data);
      this.reconcileBrowsePath(data);
      this.updatePageTurnId(data);
      this.albumList_.set(data.albumDto);
      // Folders are what is left over once the classes with their own section
      // have been taken out, so a new section only has to be added here and to
      // addContainer below.
      this.containerList_.set(
        data.containerDto?.filter(
          (item) =>
            item.objectClass !== PLAYLIST_CONTAINER_CLASS &&
            item.objectClass !== ARTIST_CONTAINER_CLASS,
        ),
      );
      this.playlistList_.set(
        data.containerDto?.filter(
          (item) => item.objectClass === PLAYLIST_CONTAINER_CLASS,
        ),
      );
      this.artistList_.set(
        data.containerDto?.filter(
          (item) => item.objectClass === ARTIST_CONTAINER_CLASS,
        ),
      );
      this.musicTracks_.set(
        data.musicItemDto?.filter(
          (item) =>
            item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0,
        ),
      );
      this.rawOtherItems_.set(
        data.musicItemDto?.filter(
          (item) =>
            item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0,
        ),
      );
      this.browseFinished$.next(data);
    } else {
      console.log('CDS ' + this.id + ' : no search result was provided.');
    }
  }

  /**
   *
   * @param data Adding new data to existing array.
   */
  public addContainer(data: ContainerItemDto): void {
    if (data) {
      this.currentContainerList.set(data);
      this.updatePageTurnId(data);

      this.albumList_.update((v) => {
        // concat already returns a new array — the extra spread copied the whole
        // listing a second time on every page of a paged browse.
        return v.concat(data.albumDto);
      });

      this.containerList_.update((v) => {
        return v.concat(
          data.containerDto.filter(
            (item) =>
              item.objectClass !== PLAYLIST_CONTAINER_CLASS &&
              item.objectClass !== ARTIST_CONTAINER_CLASS,
          ),
        );
      });

      this.playlistList_.update((v) => {
        return v.concat(
          data.containerDto.filter(
            (item) => item.objectClass === PLAYLIST_CONTAINER_CLASS,
          ),
        );
      });

      this.artistList_.update((v) => {
        return v.concat(
          data.containerDto.filter(
            (item) => item.objectClass === ARTIST_CONTAINER_CLASS,
          ),
        );
      });

      this.musicTracks_.update((v) => {
        return v.concat(
          data.musicItemDto.filter(
            (item) =>
              item.objectClass.lastIndexOf('object.item.audioItem', 0) === 0,
          ),
        );
      });

      this.rawOtherItems_.update((v) => {
        return v.concat(
          data.musicItemDto.filter(
            (item) =>
              item.objectClass.lastIndexOf('object.item.audioItem', 0) !== 0,
          ),
        );
      });

      this.browseFinished$.next(data);
    }
  }

  public getPageTurnId(): string {
    return this.turn_page_id ?? '';
  }

  private updatePageTurnId(data: ContainerItemDto): string | undefined {
    let idxObj: number;
    let dataArrayLen: number;
    if (data.albumDto?.length) {
      dataArrayLen = data.albumDto.length;
    } else {
      dataArrayLen = 0;
    }

    idxObj = this.TURN_PAGE_AFTER - dataArrayLen;
    if (idxObj <= 0) {
      this.turn_page_id = data.albumDto[dataArrayLen + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.containerDto?.length;
    if (idxObj <= 0) {
      this.turn_page_id =
        data.containerDto[data.containerDto.length + idxObj - 1].id;
      return;
    }

    idxObj = idxObj - data.musicItemDto?.length;
    if (idxObj <= 0) {
      this.turn_page_id =
        data.musicItemDto[data.musicItemDto.length + idxObj - 1].objectID;
      return;
    }

    this.turn_page_id = undefined;
  }

  private createBrowseRequest(
    objectID: string,
    sortCriteria?: string,
    mediaServerUdn?: string,
    searchInOID?: string,
  ): BrowseRequestDto {
    const br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn ?? '',
      objectID: objectID,
      sortCriteria: sortCriteria ?? '',
      start: 0,
      filter: '*',
      count: 999,
      searchInOID: '',
    };
    return br;
  }

  get currentContainerID(): string {
    return this.currentContainerList().currentContainer.id;
  }
  //
  // Search Section
  // =====================================================================================
  //
  public quickSearch(
    searchQuery: string,
    sortCriteria: string,
    mediaServerUdn: string,
    objectID: string,
  ): Subject<SearchResultDto> {
    return this.quickSearchByDto(
      this.dtoGeneratorService.generateQuickSearchDto(
        searchQuery,
        mediaServerUdn,
        sortCriteria,
        objectID,
      ),
    );
  }

  public quickSearchByDto(
    quickSearchDto: SearchRequestDto,
  ): Subject<SearchResultDto> {
    const uri = '/quickSearch';
    console.log(this.id + ' : do quick search');
    return this.httpService.post<SearchResultDto>(
      this.baseUri,
      uri,
      quickSearchDto,
    );
  }

  public rescanContent(mediaServerUdn: string): void {
    const uri = '/rescanContent';
    this.httpService.post(this.baseUri, uri, mediaServerUdn).subscribe();
  }

  /**
   * Runs a "show all" search and makes its hits the displayed content.
   *
   * A search and a browse write the same signals, so they are made mutually
   * exclusive here: whichever starts last cancels the other. Without that, the
   * automatic browse of the last folder and a search started in the same tick
   * both ran and the slower server answer won.
   *
   * @param returnTo the folder to offer as the way back. The view has to supply
   *        it: this service is created per view and a freshly built one has not
   *        browsed anything yet, so it cannot know where the user came from.
   */
  public searchAll(
    type: ShowAllType,
    request: SearchRequestDto,
    returnTo: BrowseCrumb,
  ): void {
    this.browseRequestAbort$.next();
    this.searchRequestAbort$.next();

    // Set before the request goes out, not when it returns: the view reads this
    // to decide that it must not browse its last folder, and it decides that
    // now.
    const scoped = !!request.parentObjectID && request.parentObjectID !== '0';
    this.searchContext.set({
      query: request.searchRequest,
      type: type,
      returnObjectId: returnTo.id || '0',
      returnTitle: returnTo.title ?? '',
      scopeTitle: scoped ? (returnTo.title ?? '') : '',
    });

    console.log(
      'CDS ' +
        this.id +
        ' : searchAll ' +
        type +
        " for '" +
        request.searchRequest +
        "'",
    );

    this.httpService
      .post<SearchResultDto>(this.baseUri, SEARCH_URI[type], request)
      .pipe(
        take(1),
        takeUntil(this.searchRequestAbort$),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (data) => this.applySearchResult(type, data),
        error: (error: any) => {
          console.error(this.id + ' : searchAll ' + type + ' failed', error);
          // Nothing was found, so there is nothing to go back from either.
          this.searchContext.set(undefined);
          this.toastService.error('search failed', 'MediaLibrary');
        },
      });
  }

  /**
   * Fetches playlists WITHOUT touching the displayed content — the same split as
   * browseChildrenMetadataOnly versus browseChildren. Used by the "add to
   * playlist" dialog, which only wants the data.
   */
  public searchPlaylistsMetadataOnly(
    request: SearchRequestDto,
  ): Observable<SearchResultDto> {
    return this.httpService.post<SearchResultDto>(
      this.baseUri,
      SEARCH_URI.playlists,
      request,
    );
  }

  private applySearchResult(type: ShowAllType, data: SearchResultDto): void {
    const ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    // Each kind of hit goes into the list the display container renders it with.
    // Albums in particular have to land in albumDto: everything in containerDto
    // is shown as a folder, which is why album hits used to appear under a
    // "Folders" heading and lost the album tiles and album sorting with it.
    let count = 0;
    switch (type) {
      case 'items':
        ci.musicItemDto = data.musicItems ?? [];
        count = ci.musicItemDto.length;
        break;
      case 'album':
        ci.albumDto = data.albumItems ?? [];
        count = ci.albumDto.length;
        break;
      case 'artists':
        ci.containerDto = data.artistItems ?? [];
        count = ci.containerDto.length;
        break;
      case 'playlists':
        // Playlist containers carry object.container.playlistContainer, which
        // updateContainer picks out of containerDto into the playlist section.
        ci.containerDto = data.playlistItems ?? [];
        count = ci.containerDto.length;
        break;
    }
    ci.currentContainer.childCount = count;
    ci.totalMatches = count;
    this.describeSearchResult(ci);
    this.updateContainer(ci);
  }

  /** Dresses the synthetic container the hits are presented in. */
  private describeSearchResult(ci: ContainerItemDto): void {
    const context = this.searchContext();
    ci.currentContainer.id = SEARCH_RESULT_CONTAINER_ID;
    // Step-out target, so browseToParent lands where the user came from.
    ci.currentContainer.parentID = context?.returnObjectId ?? '0';
    // Just the query: it is the subject of the page, and the header sets the
    // type and the count around it. The old "album matching 'dark'" read as a
    // broken sentence and stayed singular for 42 hits.
    ci.currentContainer.title = context?.query ?? 'search result';
    // No artwork: the header draws a colour bloom from the hits instead of
    // standing a placeholder cover where an album cover would be.
    ci.currentContainer.albumartUri = '';
  }

  public deleteMusicTrack(item: MusicItemDto) {
    this.musicTracks_.update((v) =>
      v.filter((listitem) => listitem.songId !== item.songId),
    );
  }

  public getCurrentAlbumIds(): MusicAlbumIds | undefined {
    const albumIds = this.currentContainerList()?.allTracksSameAlbumIds;

    if (!isAssigned(albumIds)) {
      return undefined;
    }

    return albumIds;
  }

  albumIdExists = computed(() => {
    const albumIds = this.getCurrentAlbumIds();

    if (!albumIds) {
      console.log(
        'like not possible for container : ' +
          this.currentContainerList().currentContainer.title,
      );
      return false;
    }

    const exists =
      isAssigned(albumIds.discogsReleaseId) ||
      isAssigned(albumIds.musicBrainzAlbumId);

    if (exists) {
      console.log(
        'like possible for container : ' +
          this.currentContainerList().currentContainer.title,
      );
    } else {
      console.log(
        'like not possible for container : ' +
          this.currentContainerList().currentContainer.title,
      );
    }

    return exists;
  });
}
