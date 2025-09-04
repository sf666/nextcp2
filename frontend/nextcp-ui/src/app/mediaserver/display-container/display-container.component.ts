import { ConfigurationService } from './../../service/configuration.service';
import { ScrollLoadHandler } from './defs.d';
import { TransportService } from 'src/app/service/transport.service';
import { PlaylistService } from './../../service/playlist.service';
import { TrackQualityService } from './../../util/track-quality.service';
import {
  MusicItemDto,
  ContainerDto,
  ContainerItemDto,
} from './../../service/dto.d';
import {
  Component,
  signal,
  ChangeDetectionStrategy,
  input,
  output,
  afterRenderEffect,
} from '@angular/core';
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
  providers: [{ provide: CdsBrowsePathService, useClass: CdsBrowsePathService },], // non singleton injections
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

  showTopHeader = input(true);
  extendedApi = input (true);
  contentHandler = input.required<ScrollLoadHandler>();


  // Inform parent about actions
  containerSelected = output<ContainerDto>();
  browseFinish = output<ContainerItemDto>();
  itemDeleted = output<MusicItemDto>();

  listView = signal<boolean>(true);
  displayFilterString = signal<string>("");
  selectedGenres = signal<Array<string>>([]);

  constructor(
    public playlistService: PlaylistService,
    public transportService: TransportService,
    private configurationService: ConfigurationService,
    private cdsBrowsePathService: CdsBrowsePathService,
    public trackQualityService: TrackQualityService
  ) {
      afterRenderEffect({
      read: (writeResult) => {
        const targetElement = document.getElementById(this.cdsBrowsePathService.scrollId()); // querySelector('#someElementId');
        if (targetElement) {
          console.log("[scrolling] to element ID : " + this.cdsBrowsePathService.scrollId());
          targetElement.focus();
        } else {
          console.log("[scrolling] target element not found for ID : " + this.cdsBrowsePathService.scrollId());
        }
      },
    });    
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
    return this.contentHandler().contentDirectoryService.musicTracks_();
  }

  get otherItems_() {
    return this.contentHandler().contentDirectoryService.otherItems_();
  }

  get albums(): ContainerDto[] {
    return this.contentHandler().contentDirectoryService.albumList_();
  }

  get playlists(): ContainerDto[] {
    return this.contentHandler().contentDirectoryService.playlistList_();
  }

  get container(): ContainerDto[] {
    return this.contentHandler().contentDirectoryService.containerList_();
  }

  //
  // Like section
  // ==============================================================================

  playPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  shufflePlaylist(container: ContainerDto): void {
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
    sortCriteria?: string
  ): Promise<boolean> {
    if (!this.contentHandler) {
      console.error('contentHandler not initialized.');
      return;
    }
    if (udn?.length < 1) {
      console.error('display-container : UDN not set');
      return
    }

    const promise = new Promise<boolean>((resolve, reject) => {
      if (this.contentHandler().persistenceService) {
        this.contentHandler().persistenceService.setCurrentObjectID(oid);
      }
      if (this.contentHandler().contentDirectoryService) {
        this.contentHandler().contentDirectoryService
          .browseChildrenByOID(oid, udn, '')
          .subscribe((data) => {
            this.browseFinished(data);
            if (data?.currentContainer?.id) {
              if (stepIn) {
                this.cdsBrowsePathService.stepIn(oid);
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
          'display-container.component: contentDirectoryService not set.'
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
    this.contentHandler().contentDirectoryService.browseToNextPage().subscribe();
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
    this.transportService.playResource(musicItemDto);
  }
}
