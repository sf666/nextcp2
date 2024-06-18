import { DeviceService } from 'src/app/service/device.service';
import { SseService } from './../../service/sse/sse.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ConfigurationService } from './../../service/configuration.service';
import { ScrollLoadHandler } from './defs.d';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';
import { TransportService } from 'src/app/service/transport.service';
import { PlaylistService } from './../../service/playlist.service';
import { TrackQualityService } from './../../util/track-quality.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { MyMusicService } from './../../service/my-music.service';
import {
  MusicItemDto,
  ContainerDto,
  ContainerItemDto,
} from './../../service/dto.d';
import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  signal,
} from '@angular/core';
import { debounce } from 'src/app/global';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { MatOption } from '@angular/material/core';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { MatInput, MatInputModule } from '@angular/material/input';
import {
  MatFormField,
  MatLabel,
  MatPrefix,
  MatSuffix,
} from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DomChangedDirective } from '../../directive/watch-dom-tree.directive';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { ContainerTileComponent } from './container-tile/container-tile.component';
import { DisplayContainerHeaderComponent } from './display-container-header/display-container-header.component';
import { ItemTileComponent } from './item-tile/item-tile.component';
import { OtherItemTileComponent } from './other-item-tile/other-item-tile.component';

@Component({
  selector: 'mediaServer-display-container',
  templateUrl: './display-container.component.html',
  styleUrls: ['./display-container.component.scss'],
  providers: [{ provide: 'uniqueId', useValue: 'default_display_container' }],
  standalone: true,
  imports: [
    DomChangedDirective,
    MatButtonModule,
    MatIconModule,
    MatFormField,
    MatLabel,
    MatPrefix,
    MatInputModule,
    MatSuffix,
    MatSelectModule,
    ReactiveFormsModule,
    MatOption,
    QualityBadgeComponent,
    StarRatingComponent,
    ContainerTileComponent,
    ItemTileComponent,
    OtherItemTileComponent,
    DisplayContainerHeaderComponent,
  ],
})
export class DisplayContainerComponent {

  @Input() showTopHeader = true;
  @Input() extendedApi: boolean = true;
  @Input() contentHandler: ScrollLoadHandler;


  // Inform parent about actions
  @Output() containerSelected = new EventEmitter<ContainerDto>();
  @Output() browseFinish = new EventEmitter<ContainerItemDto>();
  @Output() itemDeleted = new EventEmitter<MusicItemDto>();

  listView = true;

  displayFilterString = signal<string>("");
  genresList: Set<String>;
  genresListSorted: Array<String>;
  selectedGenres: Array<string> = [];

  constructor(
    public playlistService: PlaylistService,
    public transportService: TransportService,
    private configurationService: ConfigurationService,
    public trackQualityService: TrackQualityService
  ) {

  }

  domChange(event: any): void {
    console.log('DOM changed event ... ');
  }

  // quick search callbacks

  displayFilterChanged(newQuickSerchText: string) {
    this.displayFilterString.set(newQuickSerchText);
  }

  displayFilterGenreChanged(newGenres : Array<string>) {
    this.selectedGenres = newGenres;
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
    return this.contentHandler.contentDirectoryService.musicTracks_;
  }

  get otherItems_() {
    return this.contentHandler.contentDirectoryService.otherItems_;
  }

  get albums(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.albumList_;
  }

  get playlists(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.playlistList_;
  }

  get container(): ContainerDto[] {
    return this.contentHandler.contentDirectoryService.containerList_;
  }

  //
  // Like section
  // ==============================================================================

  toggleListView(): void {
    this.listView = !this.listView;
    console.log('list view is now : ' + this.listView);
  }

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

  // other container
  get containerList(): ContainerDto[] {
    return this.container;
  }

  get items(): MusicItemDto[] {
    return this.otherItems_;
  }

  isListView(): boolean {
    return this.listView;
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

    const promise = new Promise<boolean>((resolve, reject) => {
      if (this.contentHandler.persistenceService) {
        this.contentHandler.persistenceService.setCurrentObjectID(oid);
      }
      if (this.contentHandler.contentDirectoryService) {
        this.contentHandler.contentDirectoryService
          .browseChildrenByOID(oid, udn, '')
          .subscribe((data) => {
            this.browseFinished(data);
            if (data?.currentContainer?.id) {
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
    this.contentHandler.contentDirectoryService.browseToNextPage().subscribe();
  }

  addPlaylist(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylist(container);
  }

  addItemToPlaylist(item: MusicItemDto): void {
    this.playlistService.addToPlaylist(item);
  }

  playAlbum(container: ContainerDto): void {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  playItem(musicItemDto: MusicItemDto): void {
    this.transportService.playResource(musicItemDto);
  }
}
