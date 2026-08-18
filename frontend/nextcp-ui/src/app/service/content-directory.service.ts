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

@Injectable()
export class ContentDirectoryService {
  configService = inject(ConfigurationService);
  private httpService = inject(HttpService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private deviceService = inject(DeviceService);
  private toastService = inject(ToastService);
  private serverPlaylistService = inject(ServerPlaylistService);

  baseUri = '/ContentDirectoryService';

  // for parent navigation back to last CDS objectId
  private lastBrowseRequest!: BrowseRequestDto;

  //
  // signals
  // ==========================================================

  public currentContainerList = signal<ContainerItemDto>(
    this.dtoGeneratorService.generateEmptyContainerItemDto(),
  );

  isCurrentContainerRoot = computed(() => {
    return (
      (isAssigned(this.currentContainerList().currentContainer) &&
        this.currentContainerList().currentContainer?.id === '0') ||
      this.currentContainerList().currentContainer?.parentID === '-1' ||
      this.currentContainerList().currentContainer?.id.length == 0
    );
  });

  isCurrentContainerRootOrHasParentRoot = computed(() => {
    return (
      this.isCurrentContainerRoot() ||
      this.currentContainerList().currentContainer.parentID === '0'
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

    // A jump we cannot place — a reload, a restored deep link, a search hit, or
    // coming back to a view that was rebuilt. The response carries no ancestor
    // titles (parentFolderTitle is a UI label like "back to music library" for
    // some responses, not a folder name), so show where we are now and fetch
    // the chain above it.
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

    const udn = from.mediaServerUDN || this.deviceService.selectedMediaServerDevice().udn;
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
    return this.httpService.post<ContainerItemDto>(
      this.baseUri,
      '/browseChildren',
      { ...request, start: 0, count: 1 },
    );
  }

  // result container split by types
  albumList_ = signal<ContainerDto[]>([]);
  containerList_ = signal<ContainerDto[]>([]);
  playlistList_ = signal<ContainerDto[]>([]);

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
  searchFinished$: Subject<ContainerItemDto> = new Subject();

  // to which page was browsed

  private TURN_PAGE_AFTER = 60;
  private MAX_REQUEST_ITEMS = 200;
  private PAGE_REQUEST_CONCURRENCY = 4;
  private turn_page_id: string | undefined;
  private browseRequestAbort$ = new Subject<void>();

  // search
  private lastSearchObject = signal<SearchRequestDto>(
    this.dtoGeneratorService.generateEmptySearchRequestDto(),
  );
  private lastSearchType = signal<string>('');

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
  ): Subject<ContainerItemDto> {
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
  ): Subject<ContainerItemDto> {
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
  ): Subject<ContainerItemDto> {
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
    return this.httpService.post<ContainerItemDto>(
      this.baseUri,
      '/browseChildren',
      { ...browseRequestDto, start: 0, count: this.MAX_REQUEST_ITEMS },
    );
  }

  public browseChildrenByContainer(
    containerDto: ContainerDto,
    sortCriteria?: string,
  ): Subject<ContainerItemDto> {
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
  ): Subject<ContainerItemDto> {
    if (!oid) {
      oid = '0';
    }
    let browseRequestDto = this.createBrowseRequest(oid, sortCriteria, udn);
    return this.browseChildrenByRequest(browseRequestDto);
  }

  public searchCurrentContainer(searchStr: string): Subject<ContainerItemDto> {
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
  ): Subject<ContainerItemDto> {
    if (browseRequestDto.mediaServerUDN?.length < 1) {
      console.log(this.id + ' UDN not set. Stop browsing.');
      return new Subject<ContainerItemDto>();
    }

    // Abort stale paging streams when a new browse request starts.
    this.browseRequestAbort$.next();
    this.lastBrowseRequest = browseRequestDto;
    const browseStartedAt = performance.now();

    const firstPage$ = this.httpService.post<ContainerItemDto>(
      this.baseUri,
      '/browseChildren',
      { ...browseRequestDto, start: 0, count: this.MAX_REQUEST_ITEMS },
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
                  this.httpService
                    .post<ContainerItemDto>(this.baseUri, '/browseChildren', {
                      ...browseRequestDto,
                      start: page * this.MAX_REQUEST_ITEMS,
                      count: this.MAX_REQUEST_ITEMS,
                    })
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
  public browseToNextPage(): Subject<ContainerItemDto> {
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
      this.containerList_.set(
        data.containerDto?.filter(
          (item) => item.objectClass !== 'object.container.playlistContainer',
        ),
      );
      this.playlistList_.set(
        data.containerDto?.filter(
          (item) => item.objectClass === 'object.container.playlistContainer',
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
        return [...v].concat(data.albumDto);
      });

      this.containerList_.update((v) => {
        return v.concat(
          data.containerDto.filter(
            (item) => item.objectClass !== 'object.container.playlistContainer',
          ),
        );
      });

      this.playlistList_.update((v) => {
        return v.concat(
          data.containerDto.filter(
            (item) => item.objectClass === 'object.container.playlistContainer',
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

  public searchAllItems(quickSearchDto: SearchRequestDto): void {
    console.log('CDS ' + this.id + ' : searchAllItems');
    const uri = '/searchAllItems';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('songs');
    console.log(this.id + 'performing search for all matching items ...');
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe({
        next: (data) => {
          console.log(
            'received ' +
              data.musicItems.length +
              ' items. Updating search result ...',
          );
          this.updateSearchResultItem(data.musicItems);
        },
        error: (error: any) => {
          console.error(this.id + 'searchAllItems error : ', error);
        },
      });
  }

  public searchAllPlaylist(
    quickSearchDto: SearchRequestDto,
  ): Observable<SearchResultDto> {
    console.log('CDS ' + this.id + ' : searchAllPlaylist');
    const uri = '/searchAllPlaylist';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('playlists');
    let result = this.httpService.post<SearchResultDto>(
      this.baseUri,
      uri,
      quickSearchDto,
    );
    result.subscribe((data) => {
      this.updateSearchResultContainer(data.playlistItems);
    });
    return result;
  }

  public searchAllAlbum(quickSearchDto: SearchRequestDto): void {
    console.log('CDS ' + this.id + ' : searchAllAlbum');
    const uri = '/searchAllAlbum';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('album');
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultContainer(data.albumItems);
      });
  }

  public searchAllArtists(quickSearchDto: SearchRequestDto): void {
    console.log('CDS ' + this.id + ' : searchAllArtists');
    const uri = '/searchAllArtists';
    this.lastSearchObject.set(quickSearchDto);
    this.lastSearchType.set('artists');
    this.httpService
      .post<SearchResultDto>(this.baseUri, uri, quickSearchDto)
      .subscribe((data) => {
        this.updateSearchResultContainer(data.artistItems);
      });
  }

  private updateSearchResultContainer(searchResultContainer: ContainerDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.containerDto = searchResultContainer;
    ci.currentContainer.parentID =
      this.lastBrowseRequest?.objectID !== undefined
        ? this.lastBrowseRequest.objectID
        : '0';
    ci.currentContainer.title =
      this.lastSearchType() +
      " matching '" +
      this.lastSearchObject().searchRequest +
      "'";
    ci.currentContainer.id = SEARCH_RESULT_CONTAINER_ID;
    ci.currentContainer.albumartUri = '/assets/images/search-icon.png';
    ci.parentFolderTitle = 'back to music library';
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
  }

  private updateSearchResultItem(searchResultItems: MusicItemDto[]) {
    let ci = this.dtoGeneratorService.generateEmptyContainerItemDto();
    ci.musicItemDto = searchResultItems;
    ci.currentContainer.albumartUri = '/assets/images/search-icon.png';
    ci.currentContainer.parentID = this.lastBrowseRequest.objectID;
    ci.currentContainer.id = SEARCH_RESULT_CONTAINER_ID;
    ci.currentContainer.title =
      this.lastSearchType() +
      " matching '" +
      this.lastSearchObject().searchRequest +
      "'";
    ci.currentContainer.childCount = searchResultItems.length;
    ci.parentFolderTitle = 'back to music library';
    console.log(
      this.id + ' : updating current container with search result ...',
    );
    this.updateContainer(ci);
    this.searchFinished$.next(ci);
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
