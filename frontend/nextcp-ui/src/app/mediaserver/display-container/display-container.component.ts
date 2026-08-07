import { ConfigurationService } from './../../service/configuration.service';
import { ScrollLoadHandler } from './defs.d';
import { TransportService } from 'src/app/service/transport.service';
import { PlaylistService } from './../../service/playlist.service';
import { DeviceService } from './../../service/device.service';
import { LocalPlayerService } from './../../service/local-player.service';
import { TrackQualityService } from './../../util/track-quality.service';
import {
  MusicItemDto,
  ContainerDto,
  ContainerItemDto,
} from './../../service/dto.d';
import { RatingFilter } from 'src/app/service/rating-service.service';
import {
  filterContainers,
  matchesTextFilter,
} from 'src/app/util/browse-filter';
import {
  Component,
  computed,
  signal,
  ChangeDetectionStrategy,
  input,
  output,
  effect,
  viewChild,
  inject,
  DestroyRef,
} from '@angular/core';
import { debounceTime, Subscription } from 'rxjs';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ContainerTileComponent } from './container-tile/container-tile.component';
import { DisplayContainerHeaderComponent } from './display-container-header/display-container-header.component';
import { ItemTileComponent } from './item-tile/item-tile.component';
import { OtherItemTileComponent } from './other-item-tile/other-item-tile.component';
import { CdsBrowsePathService } from 'src/app/util/cds-browse-path.service';

@Component({
  selector: 'mediaServer-display-container',
  templateUrl: './display-container.component.html',
  styleUrls: ['./display-container.component.scss'],
  providers: [
    { provide: CdsBrowsePathService, useClass: CdsBrowsePathService },
  ], // non singleton injections
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    ContainerTileComponent,
    ItemTileComponent,
    OtherItemTileComponent,
    DisplayContainerHeaderComponent,
  ],
})
export class DisplayContainerComponent {
  playlistService = inject(PlaylistService);
  transportService = inject(TransportService);
  private deviceService = inject(DeviceService);
  private localPlayer = inject(LocalPlayerService);
  private configurationService = inject(ConfigurationService);
  private cdsBrowsePathService = inject(CdsBrowsePathService);
  trackQualityService = inject(TrackQualityService);

  showTopHeader = input(true);
  extendedApi = input(true);
  enableAlbumSort = input(false);
  // When true, the track list view shows an additional "Genre" column (upnp:genre).
  showGenre = input(false);
  contentHandler = input<ScrollLoadHandler | undefined>(undefined);

  // Inform parent about actions
  containerSelected = output<ContainerDto>();
  browseFinish = output<ContainerItemDto>();
  itemDeleted = output<MusicItemDto>();

  listView = signal<boolean>(true);
  displayFilterString = signal<string>('');
  selectedGenres = signal<Array<string>>([]);
  sortCriteria = signal<string>('NONE');
  ratingFilter = signal<RatingFilter>('ANY');

  /**
   * True while anything narrows the listing. Used to tell "3 items" from
   * "1 of 3 items" — without it, a filtered section still claims the full count and
   * the user cannot see that something is being held back.
   */
  filterActive = computed(
    () =>
      this.displayFilterString().length > 0 ||
      this.selectedGenres().length > 0 ||
      this.ratingFilter() !== 'ANY',
  );

  /**
   * @param visible how many rows the section shows after filtering
   * @param total how many the server delivered
   */
  countLabel(visible: number, total: number): string {
    if (!this.filterActive() || visible === total) {
      return `${total} items`;
    }
    return `${visible} of ${total} items`;
  }

  // The (virtualized) album + folder grids — used to restore scroll to a
  // specific entry even when it is not currently in the DOM.
  private albumTile = viewChild<ContainerTileComponent>('albumTile');
  private folderTile = viewChild<ContainerTileComponent>('folderTile');

  /**
   * How many rows a section shows after filtering.
   *
   * Applies the same filter the tiles apply, so the heading cannot claim a number the
   * grid does not show. Deliberately computed from our own lists instead of asking the
   * tile components: the headings render before their tile, whose required `container`
   * input is not bound at that point.
   */
  visibleAlbums = computed(() => this.narrowed(this.albumList).length);
  visibleFolders = computed(() => this.narrowed(this.container).length);
  visiblePlaylists = computed(() => this.narrowed(this.playlistList).length);
  visibleOtherItems = computed(
    () =>
      this.otherItems_.filter((item) =>
        matchesTextFilter(item.title, this.displayFilterString()),
      ).length,
  );

  private narrowed(containers: ContainerDto[]): ContainerDto[] {
    return filterContainers(
      containers,
      this.displayFilterString(),
      this.selectedGenres(),
      this.ratingFilter(),
    );
  }
  private readonly destroyRef = inject(DestroyRef);
  private subscribedCds: ContentDirectoryService | null = null;
  private restoreSub?: Subscription;

  constructor() {
    // Restore scroll after a browse settles. browseFinished$ fires once per
    // loaded page, so debounce until the last page has arrived — otherwise the
    // restore target (e.g. a mid-list album) may not be loaded yet. Prefer the
    // virtualized album/folder grids (target may not be in the DOM); fall back
    // to DOM-id focus for the small non-virtualized lists (playlists / tracks).
    //
    // Subscribe ONCE per ContentDirectoryService instance: the ScrollLoadHandler
    // wrapper is recreated on every change detection, so re-subscribing in the
    // effect body would tear the pending debounce timer down before it fires.
    effect(() => {
      const cds = this.contentHandler()?.contentDirectoryService;
      if (!cds || cds === this.subscribedCds) {
        return;
      }
      this.restoreSub?.unsubscribe();
      this.subscribedCds = cds;
      this.restoreSub = cds.browseFinished$
        .pipe(debounceTime(250))
        .subscribe(() => {
          const id = this.cdsBrowsePathService.scrollToID;
          if (
            this.albumTile()?.scrollToId(id) ||
            this.folderTile()?.scrollToId(id)
          ) {
            return;
          }
          this.cdsBrowsePathService.scrollIntoViewID(id);
        });
    });

    this.destroyRef.onDestroy(() => this.restoreSub?.unsubscribe());
  }

  /**
   * @param elementID ATTENTION: elementID needs to have tabindex set to '-1': <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID: string): boolean {
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      targetElement.focus();
      console.log('scrolled to element ID : ' + elementID);
      return true;
    }
    return false;
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

  //
  // Accessor. Delivers the buckets for the display components
  //

  get musicTracks(): MusicItemDto[] {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return [];
    }
    return handler.contentDirectoryService.musicTracks_();
  }

  get otherItems_() {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return [];
    }
    return handler.contentDirectoryService.otherItems_();
  }

  get albums(): ContainerDto[] {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return [];
    }
    return handler.contentDirectoryService.albumList_();
  }

  get playlists(): ContainerDto[] {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return [];
    }
    return handler.contentDirectoryService.playlistList_();
  }

  get container(): ContainerDto[] {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return [];
    }
    return handler.contentDirectoryService.containerList_();
  }

  //
  // Like section
  // ==============================================================================

  playPlaylist(container: ContainerDto): void {
    // For the local browser player there is no server-side playlist; queue the currently displayed
    // tracks and play them in the shown order.
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.playQueue(this.musicTracks, false);
      return;
    }
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  shufflePlaylist(container: ContainerDto): void {
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.playQueue(this.musicTracks, true);
      return;
    }
    this.playlistService.addContainerToPlaylistAndPlay(container, true);
  }

  //
  // Sorted container access to albums, playlists or other container
  // ===============================================================================================

  get allMusicTracks() {
    return this.musicTracks;
  }

  get albumList(): ContainerDto[] {
    return this.albums;
  }

  get playlistList(): ContainerDto[] {
    return this.playlists;
  }

  isListView(): boolean {
    return this.listView();
  }

  public browseToOid(
    oid: string,
    udn: string,
    stepIn: boolean,
    sortCriteria?: string,
  ): Promise<boolean> | undefined {
    const handler = this.contentHandler();
    if (!handler) {
      console.error('contentHandler not initialized.');
      return;
    }
    if (udn?.length < 1) {
      console.error('display-container : UDN not set');
      return;
    }

    const promise = new Promise<boolean>((resolve, reject) => {
      if (handler.persistenceService != undefined) {
        handler.persistenceService?.setCurrentObjectID(oid);
      }
      if (handler.contentDirectoryService) {
        handler.contentDirectoryService
          .browseChildrenByOID(oid, udn, '')
          .subscribe((data) => {
            this.browseFinished(data);
            if (data?.currentContainer?.id) {
              if (stepIn) {
                this.cdsBrowsePathService.stepIn(oid);
              } else if (data.currentContainer.id === '0') {
                // Home is not one step out — drop the whole trail, otherwise it
                // keeps growing across navigations and points into stale views.
                this.cdsBrowsePathService.clear();
              } else {
                this.cdsBrowsePathService.stepOut();
              }
              resolve(true);
            } else {
              resolve(false);
            }
          });
      } else {
        console.error(
          'display-container.component: contentDirectoryService not set.',
        );
        reject('display-container.component: contentDirectoryService not set.');
      }
    });

    return promise;
  }

  //
  // Actions (click events)
  // ===============================================================================================

  public browseTo(containerDto: ContainerDto): void {
    this.browseToOid(containerDto.id, containerDto.mediaServerUDN, true, '');
    this.containerSelected.emit(containerDto);
  }

  private browseFinished(data: ContainerItemDto) {
    this.browseFinish.emit(data);
  }

  public loadNextBrowsePage() {
    const handler = this.contentHandler();
    if (!handler?.contentDirectoryService) {
      return;
    }
    handler.contentDirectoryService.browseToNextPage().subscribe();
  }

  addPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylist(container);
  }

  addItemToPlaylist(item: MusicItemDto): void {
    this.playlistService.addToPlaylist(item);
  }

  addItemToPlaylistNext(item: MusicItemDto): void {
    this.playlistService.addToPlaylistNext(item);
  }

  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  playItem(musicItemDto: MusicItemDto): void {
    // For the local browser player, play the displayed track list starting at the clicked track, so
    // the rest of the list keeps playing after it.
    if (this.deviceService.isLocalBrowserSelected()) {
      this.localPlayer.playQueueFrom(this.musicTracks, musicItemDto);
      return;
    }
    // Broadcast/streaming routing (Radio source vs Playlist/AVTransport) is handled centrally in
    // TransportService.playResource.
    this.transportService.playResource(musicItemDto);
  }
}
