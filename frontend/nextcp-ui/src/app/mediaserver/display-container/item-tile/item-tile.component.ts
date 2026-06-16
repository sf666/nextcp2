import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  output,
  signal,
  inject,
} from '@angular/core';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { ContainerDto, MusicItemDto } from 'src/app/service/dto';
import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { StarRatingComponent } from 'src/app/view/star-rating/star-rating.component';
import { SseService } from 'src/app/service/sse/sse.service';
import { DeviceService } from 'src/app/service/device.service';
import { QualityBadgeComponent } from 'src/app/util/comp/quality-badge/quality-badge.component';
import { RendererService } from 'src/app/service/renderer.service';

@Component({
  selector: 'item-tile',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [StarRatingComponent, QualityBadgeComponent],
  templateUrl: './item-tile.component.html',
  styleUrl: './item-tile.component.scss',
})
export class ItemTileComponent {
  private songOptionsServiceService = inject(SongOptionsServiceService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private timeDisplayService = inject(TimeDisplayService);
  private deviceService = inject(DeviceService);
  private sseService = inject(SseService);
  private rendererService = inject(RendererService);

  isListView = input<boolean>(false);
  contentDirectoryService = input.required<ContentDirectoryService>();
  quickSearchString = input<string>('');
  selectedGenres = input<Array<string>>([]);
  extendedApi = input<boolean>(true);
  // When true, an additional "Genre" column (upnp:genre) is shown in list view.
  showGenre = input<boolean>(false);

  playItemClicked = output<MusicItemDto>();
  addItemToPlaylistClicked = output<MusicItemDto>();
  addItemToPlaylistNextClicked = output<MusicItemDto>();

  // some calculated constants
  allTracksSameDisc = computed(() =>
    this.checkAllTracksSameDisc(this.allMusicTracks()),
  );
  allTracksSameAlbum = computed(() =>
    this.checkAllTracksSameAlbum(this.allMusicTracks()),
  );
  musicTracks = computed(() => this.filteredMusicTracks(this.allMusicTracks()));

  allMusicTracks = computed(() =>
    this.contentDirectoryService().musicTracks_(),
  );

  // True when the current view shows global "show all" search results.
  // Disc labels must not be rendered for search results (mixed albums/discs).
  isSearchResult = computed(
    () =>
      this.contentDirectoryService().currentContainerList().currentContainer
        ?.id === 'search_result',
  );

  currentUrl = signal<string>('');
  currentObjectId = signal<string>('');
  lastDiscLabel = '';

  constructor() {
    const deviceService = this.deviceService;
    const sseService = this.sseService;

    const currentTrack = this.rendererService.currentTrack();
    if (currentTrack?.streamingURL) {
      this.currentUrl.set(currentTrack.streamingURL);
    }
    if (currentTrack?.objectID) {
      this.currentObjectId.set(currentTrack.objectID);
    }

    sseService.mediaRendererTrackInfoChanged$.subscribe((data) => {
      if (
        data?.mediaRendererUdn &&
        data?.currentTrack &&
        deviceService.isMediaRendererSelected(data.mediaRendererUdn)
      ) {
        this.currentUrl.set(data.currentTrack.streamingURL);
        this.currentObjectId.set(data.currentTrack.objectID);
      }
    });
  }

  playItem(item: MusicItemDto) {
    console.log('playitem clicked : ' + item.title);
    this.playItemClicked.emit(item);
  }

  addItemToPlaylist(item: MusicItemDto) {
    this.addItemToPlaylistClicked.emit(item);
  }

  addItemToPlaylistNext(item: MusicItemDto) {
    this.addItemToPlaylistNextClicked.emit(item);
  }

  private checkAllTracksSameDisc(data: MusicItemDto[]): boolean {
    if (data.length > 0) {
      const firstTrackDisc = data[0]?.numberOfThisDisc;
      const sameDisc = !data?.find(
        (item) => item.numberOfThisDisc !== firstTrackDisc,
      );
      console.log('[item-tile] allTracksSameDisc : ' + sameDisc);
      return sameDisc;
    } else {
      return false;
    }
  }

  private checkAllTracksSameAlbum(data: MusicItemDto[]): boolean {
    if (this.contentDirectoryService().albumIdExists()) {
      console.log(
        '[item-tile] : album identified by musicBrainz or discogs id, assuming all tracks have same album',
      );
      return true;
    } else {
      if (data.length > 0) {
        console.log(
          '[item-tile] : no album id available, checking album title for all tracks',
        );
        const firstTrackAlbum = data[0].album;
        if (firstTrackAlbum != null) {
          const albumsWithOtherNames = data.filter(
            (item) => item.album !== firstTrackAlbum,
          ).length;
          console.log(
            '[item-tile] number of tracs with other album title : ' +
              albumsWithOtherNames,
          );
          return albumsWithOtherNames == 0;
        } else {
          console.log('[item-tile] no album information available');
          return false;
        }
      }
    }
    return false;
  }

  private filteredMusicTracks(data: MusicItemDto[]): MusicItemDto[] {
    console.log('filteredMusicTracks');
    let tracks: Array<MusicItemDto>;
    if (this.quickSearchString()) {
      tracks = data.filter((item) =>
        this.doFilterText(item.title, this.quickSearchString()),
      );
    } else {
      tracks = data;
    }
    if (this?.selectedGenres()?.length > 0) {
      tracks = tracks.filter((item) => this.doFilterGenre(item));
    }
    return tracks;
  }

  private doFilterGenre(item: MusicItemDto): boolean {
    let add = false;
    this.selectedGenres()?.forEach((genre) => {
      if (this.doFilterText(item.genre, genre)) {
        add = true;
      }
    });
    return add;
  }

  private doFilterText(title: string, filter: string): boolean {
    if (!filter) {
      return true;
    }
    if (!title && 'NONE' == filter) {
      return true;
    } else if (!title) {
      return false;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
  }

  //
  // CSS support like responsive grid layout counting in respect to first image visible or not
  // ===============================================================================================
  getColNumber(): number {
    let num = 5;
    if (this.extendedApi()) {
      num = num + 1;
    }
    if (!this.allTracksSameAlbum()) {
      num = num + 1;
    }
    return num;
  }

  // Builds the CSS grid template for the list view rows/header.
  // An optional genre column is inserted between Artist and Rating when showGenre() is set.
  gridColumns(sameAlbum: boolean): string {
    const firstAndTitle = sameAlbum ? '48px 2fr' : '64px minmax(100px, 2fr)';
    const genre = this.showGenre() ? ' minmax(120px, 1fr)' : '';
    return `${firstAndTitle} minmax(150px, 1fr)${genre} 128px 80px 48px`;
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateStringShort(
        item.audioFormat.durationInSeconds,
      );
    } else {
      return '';
    }
  }

  showSongPopup(event: MouseEvent, item: MusicItemDto): void {
    this.songOptionsServiceService
      .openOptionsDialog(event, item, this.currentContainer)
      .subscribe((result) => {
        // handle user action from dialog ...
        if (result) {
          if (result.type == 'add') {
            console.log('added song to playlist : ' + item); // check item
          } else if (result.type == 'delete') {
            console.log('deleted song from playlist : ');
            setTimeout(() => {
              this.contentDirectoryService().deleteMusicTrack(result.data);
            }, 200);
          } else if (result.type == 'download') {
            console.log('download song : ' + result.data.title);
          } else if (result.type == 'next') {
            console.log('play next song : ' + result.data.title);
            this.addItemToPlaylistNext(item);
          } else if (result.type == 'last') {
            console.log('add playlist at last position : ' + result.data.title);
            this.addItemToPlaylist(item);
          }
        }
      });
  }
  public get currentContainer(): ContainerDto {
    if (
      this.contentDirectoryService().currentContainerList().currentContainer
    ) {
      return this.contentDirectoryService().currentContainerList()
        .currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  public selectedRowClass(musicItemDto: MusicItemDto): string {
    if (this.isPlayingItem(musicItemDto)) {
      return 'selectRow';
    }
    return '';
  }

  public isPlayingItem(musicItemDto: MusicItemDto): boolean {
    if (!musicItemDto) {
      return false;
    }

    const matchByUrl =
      musicItemDto.streamingURL?.length > 0 &&
      musicItemDto.streamingURL === this.currentUrl();
    const matchByObjectId =
      musicItemDto.objectID?.length > 0 &&
      musicItemDto.objectID === this.currentObjectId();

    return matchByUrl || matchByObjectId;
  }

  // Disc Label support
  // Audio items must be already sorted.
  // TODO : sort items in UI

  getDiscLabel(item: MusicItemDto): string {
    if (item.numberOfThisDisc !== this.lastDiscLabel) {
      this.lastDiscLabel = item.numberOfThisDisc;
      return `Disk ${item.numberOfThisDisc}`;
    }
    return '';
  }

  newDiscLabel(item: MusicItemDto): boolean {
    if (item.numberOfThisDisc !== this.lastDiscLabel) {
      return true;
    }
    return false;
  }
}
