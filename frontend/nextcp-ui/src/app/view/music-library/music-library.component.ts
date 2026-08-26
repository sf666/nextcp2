import {
  ContainerDto,
  ContainerItemDto,
  MusicItemDto,
} from './../../service/dto.d';
import { GlobalSearchService } from './../../service/search/global-search.service';
import {
  AfterViewInit,
  Component,
  inject,
  input,
  viewChild,
  ChangeDetectionStrategy,
} from '@angular/core';
import { combineLatest, distinctUntilChanged, map } from 'rxjs';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';
import {
  BrowseCrumb,
  ContentDirectoryService,
  SEARCH_RESULT_CONTAINER_ID,
} from 'src/app/service/content-directory.service';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { DisplayContainerComponent } from 'src/app/mediaserver/display-container/display-container.component';
import { LayoutService } from 'src/app/service/layout.service';
import { DeviceService } from 'src/app/service/device.service';
import { PersistenceService } from 'src/app/service/persistence/persistence.service';
import { ActivatedRoute } from '@angular/router';
import { MusicLibraryService } from 'src/app/service/music-library/music-library.service';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { isAssigned } from 'src/app/global';

@Component({
  selector: 'app-music-library',
  standalone: true,
  imports: [NavBarComponent, DisplayContainerComponent],
  providers: [
    { provide: ContentDirectoryService, useClass: ContentDirectoryService },
  ], // non singleton injections
  templateUrl: './music-library.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrl: './music-library.component.scss',
})
export class MusicLibraryComponent implements AfterViewInit {
  readonly dispContainer = viewChild(DisplayContainerComponent);
  readonly objectId = input<string>();
  private viewReady = false;
  private pendingBrowse: { udn: string; objectId: string } | null = null;

  private readonly route = inject(ActivatedRoute);
  public readonly contentDirectoryService = inject(ContentDirectoryService);
  public readonly layoutService = inject(LayoutService);
  private readonly persistenceService = inject(PersistenceService);
  public readonly deviceService = inject(DeviceService);
  private readonly musicLibraryService = inject(MusicLibraryService);
  private readonly globalSearchService = inject(GlobalSearchService);

  constructor() {
    const globalSearchService = this.globalSearchService;
    console.log('constructor call : MusicLibraryComponent');

    // A "show all" parks its request on the service and navigates here. Reading
    // it as an observable covers both ways this view can be the target: a fresh
    // instance gets the parked request as the first emission, an instance that
    // was already on screen gets it when it is set.
    toObservable(globalSearchService.pendingShowAll)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.runPendingShowAll());

    this.getContentHandler()
      .contentDirectoryService.browseFinished$.pipe(takeUntilDestroyed())
      .subscribe((data) => this.browseFinished(data));
    // The only place that starts a browse: one emission per change of media server or route
    // parameter. There used to be four ways in - this subscription, the flush in ngAfterViewInit,
    // a route.params subscription nested inside mediaServerInitiated$ (which also leaked one
    // subscription per emission) and a bare initViewData at the end of ngAfterViewInit - and a
    // single navigation browsed the root several times. Not an effect(): initViewData reads the
    // very signals the browse writes, so it would loop.
    combineLatest([
      toObservable(this.deviceService.selectedMediaServerDevice),
      this.route.params,
    ])
      .pipe(
        map(([server, params]) => ({
          udn: server.udn,
          objectId: (params['objectId'] as string) ?? '',
        })),
        distinctUntilChanged(
          (a, b) => a.udn === b.udn && a.objectId === b.objectId,
        ),
        takeUntilDestroyed(),
      )
      .subscribe(({ udn }) => {
        if (udn?.length > 0) {
          this.initViewData(udn);
        } else {
          console.log('no media server device selected.');
        }
      });
  }

  /**
   * Takes over a "show all" request parked before this view existed, and runs it
   * on this view's own content directory service — the instance whose result is
   * on screen.
   *
   * @returns true when a search was started, i.e. the caller must not browse.
   */
  private runPendingShowAll(): boolean {
    const pending = this.globalSearchService.consumePendingShowAll();
    if (!pending) {
      return false;
    }
    // Any browse waiting for the view to be ready is obsolete now.
    this.pendingBrowse = null;
    this.contentDirectoryService.searchAll(
      pending.type,
      pending.request,
      this.searchReturnTarget(),
    );
    return true;
  }

  /**
   * The folder a search-result page offers as the way back.
   *
   * Worth the four steps: the reported bug is precisely the case where this view
   * was built by the navigation the search triggered, so it has browsed nothing
   * and the obvious sources are all empty.
   */
  private searchReturnTarget(): BrowseCrumb {
    // Searching again from a result page must keep the original origin.
    const active = this.contentDirectoryService.searchContext();
    if (active) {
      return { id: active.returnObjectId, title: active.returnTitle };
    }
    // This view is standing in a folder (instance was reused).
    const shown =
      this.contentDirectoryService.currentContainerList().currentContainer;
    if (shown?.id && shown.id !== SEARCH_RESULT_CONTAINER_ID) {
      return { id: shown.id, title: shown.title };
    }
    // Freshly built: the folder the previous instance left behind. The music
    // library service is root-scoped, so it outlives that instance.
    const remembered = this.musicLibraryService.currentContainer();
    if (remembered?.id && remembered.id !== SEARCH_RESULT_CONTAINER_ID) {
      return { id: remembered.id, title: remembered.title };
    }
    // Cold start or reload: we know where to go, not what it is called.
    return { id: this.persistenceService.getLastObjectId() || '0', title: '' };
  }

  ngAfterViewInit(): void {
    this.viewReady = true;
    this.layoutService.setFramedView();
    // The browse that arrived before the view could run it.
    this.flushPendingBrowse();
  }

  private browseFinished(data: ContainerItemDto): void {
    // Search hits are not a folder. Publishing them would make the app-wide
    // "current library folder" the synthetic search container — and that folder
    // is the scope a non-global search runs in, so searching from a result page
    // would have searched inside the previous result.
    if (data?.currentContainer?.id === SEARCH_RESULT_CONTAINER_ID) {
      return;
    }
    this.musicLibraryService.updateCurrentContainer(data);
  }

  private initViewData(udn: string): void {
    console.log('Music Library : initViewData ...');
    this.layoutService.setFramedView();

    // Take a parked "show all" first: reading it here is synchronous, so which
    // of this view's several init paths runs first no longer decides anything.
    if (this.runPendingShowAll()) {
      return;
    }

    // Hits are already on screen. Restoring the last folder would throw away
    // what the user asked for — and this method runs more than once per
    // construction. Only an explicit navigation leaves a search result.
    if (this.contentDirectoryService.searchContext()) {
      console.log('Music Library : showing search result, no browse');
      return;
    }

    const objectId = this.objectId();
    if (objectId) {
      console.log('browse to injected OID : ' + objectId);
      this.browseToUid(udn, objectId);
    } else {
      if (
        this.persistenceService.getLastObjectId() !== undefined &&
        this.persistenceService.getLastObjectId() !== ''
      ) {
        console.log(
          'browse to last persistent object ID : ' +
            this.persistenceService.getLastObjectId(),
        );
        let lastOid = this.persistenceService.getLastObjectId();
        if (lastOid == undefined || lastOid.length == 0) {
          lastOid = '0';
        }
        this.browseToUid(udn, lastOid);
      } else {
        console.log('browse to object ID : 0');
        this.browseToUid(udn, '0');
      }
    }
  }

  private browseToUid(udn: string, objectId: string) {
    const dispContainer = this.dispContainer();
    if (!this.viewReady || !dispContainer) {
      this.pendingBrowse = { udn, objectId };
      return;
    }

    if (!(udn?.length > 0)) {
      console.log('last media server device not found ... ');
      udn = this.deviceService.selectedMediaServerDevice().udn;
      console.log('Music Library - initial : selected media server : ' + udn);
      objectId = '0';
    } else {
      console.log();
      let prom = this.browseToOid(objectId, udn, true, '');
      if (prom) {
        prom.then(
          (val) => {
            if (!val) this.browseToRoot(udn);
          },
          (err) => console.error(err),
        );
      }
    }
  }

  private flushPendingBrowse(): void {
    if (!this.pendingBrowse || !this.viewReady || !this.dispContainer()) {
      return;
    }

    const nextBrowse = this.pendingBrowse;
    this.pendingBrowse = null;
    // A "show all" took over while this browse was waiting for the view. Running
    // it now would replace the hits with the folder — the original bug.
    if (this.contentDirectoryService.searchContext()) {
      return;
    }
    this.browseToUid(nextBrowse.udn, nextBrowse.objectId);
  }

  private browseToOid(
    oid: string,
    udn: string,
    stepIn: boolean,
    sortCriteria?: string,
  ): Promise<boolean> | undefined {
    const dispContainer = this.dispContainer();
    if (dispContainer) {
      return dispContainer.browseToOid(oid, udn, stepIn, sortCriteria);
    }
  }

  public browseToRoot(udn: string, sortCriteria?: string): void {
    console.log('browsing to root folder ... ');
    this.browseToOid('0', udn, true, sortCriteria);
  }

  //
  // Events
  //
  containerSelected(event: ContainerDto) {
    // no special activities yet ...
  }

  itemDeleted(event: MusicItemDto) {
    // The row is already gone locally (item-tile did that); browse again so the
    // server has the last word - the playlist view does the same.
    this.contentDirectoryService.refreshCurrentContainer();
  }

  //
  // bindings
  // =======================================================================

  showTopHeader(): boolean {
    return true;
  }

  getContentHandler(): ScrollLoadHandler {
    return {
      contentDirectoryService: this.contentDirectoryService,
      persistenceService: this.persistenceService,
    };
  }

  //
  // Nav-Bar bindings
  //
  homeButtonPressed(event: any) {
    this.globalSearchService.clearSearch();
    this.browseToOid(
      '0',
      this.deviceService.selectedMediaServerDevice().udn,
      false,
      '',
    );
  }

  /**
   * Jump straight to an ancestor from the breadcrumb. Not a step out — the
   * target may be several levels up — so the scroll stack is left to
   * reconcile itself on the next browse.
   */
  crumbPressed(crumb: BrowseCrumb) {
    this.globalSearchService.clearSearch();
    this.browseToOid(
      crumb.id,
      this.deviceService.selectedMediaServerDevice().udn,
      false,
      '',
    );
  }

  selectServer(udn: string): void {
    this.persistenceService.setCurrentObjectID('0');
    this.deviceService.setMediaServerByUdn(udn);
    this.browseToOid('0', udn, false, '');
  }

  mediaServerSelected(): boolean {
    return this.deviceService.selectedMediaServerDevice()?.udn?.length > 0;
  }
}
