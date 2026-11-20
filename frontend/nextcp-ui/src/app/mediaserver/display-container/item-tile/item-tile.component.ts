import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  input,
  output,
  inject,
  untracked,
} from '@angular/core';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { ContainerDto, ItemDto } from 'src/app/service/dto';
import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { StarRatingComponent } from 'src/app/view/star-rating/star-rating.component';
import { RatingFilter } from 'src/app/service/rating-service.service';
import { filterMusicItems } from 'src/app/util/browse-filter';
import { QualityBadgeComponent } from 'src/app/util/comp/quality-badge/quality-badge.component';
import { LiveBadgeComponent } from 'src/app/util/comp/live-badge/live-badge.component';
import { isBroadcastItem } from 'src/app/util/broadcast-item';
import { RendererService } from 'src/app/service/renderer.service';
import { BROWSE_WINDOW_THRESHOLD, BrowseWindowDirective } from '../browse-window.directive';

type TrackRow = { key: string; item: ItemDto } | { key: string; disc: string };

@Component({
  selector: 'item-tile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [StarRatingComponent, QualityBadgeComponent, LiveBadgeComponent, BrowseWindowDirective],
  templateUrl: './item-tile.component.html',
  styleUrl: './item-tile.component.scss',
})
export class ItemTileComponent {
  private songOptionsServiceService = inject(SongOptionsServiceService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private timeDisplayService = inject(TimeDisplayService);
  private rendererService = inject(RendererService);

  isListView = input<boolean>(false);
  contentDirectoryService = input.required<ContentDirectoryService>();
  allMusicTracks = input.required<ItemDto[]>({ alias: 'tracks' });
  quickSearchString = input<string>('');
  selectedGenres = input<Array<string>>([]);
  // Narrows the listing down by rating.
  ratingFilter = input<RatingFilter>('ANY');
  extendedApi = input<boolean>(true);
  // When true, an additional "Genre" column (upnp:genre) is shown in list view.
  showGenre = input<boolean>(false);

  playItemClicked = output<ItemDto>();
  addItemToPlaylistClicked = output<ItemDto>();
  addItemToPlaylistNextClicked = output<ItemDto>();

  // some calculated constants
  allTracksSameDisc = computed(() =>
    this.checkAllTracksSameDisc(this.allMusicTracks()),
  );
  // Search hits have their own covers even if they all come from one album.
  allTracksSameAlbum = computed(() =>
    !this.contentDirectoryService().isSearchResultDisplayed() &&
    this.checkAllTracksSameAlbum(this.allMusicTracks()),
  );
  musicTracks = computed(() => this.filteredMusicTracks(this.allMusicTracks()));

  protected readonly windowed = computed(() => {
    const listing = this.contentDirectoryService().currentContainerList();
    return Math.max(listing.totalMatches ?? 0, this.allMusicTracks().length) >= BROWSE_WINDOW_THRESHOLD;
  });

  protected readonly trackRows = computed(() => {
    const withDiscs = this.allTracksSameAlbum() && !this.allTracksSameDisc();
    const rows: TrackRow[] = [];
    let lastDisc = '';
    for (const item of this.musicTracks()) {
      const disc = item.numberOfThisDisc;
      if (withDiscs && disc && disc !== lastDisc) {
        rows.push({ key: 'disc:' + item.objectID, disc });
      }
      lastDisc = disc;
      rows.push({ key: item.objectID, item });
    }
    return rows;
  });

  constructor() {
    // Keep the filter in view and start at the first match when narrowing a long list.
    let initialized = false;
    effect(() => {
      this.quickSearchString();
      this.selectedGenres();
      this.ratingFilter();
      if (initialized && untracked(this.windowed)) {
        const parent = document.getElementById('mainContent');
        if (parent) {
          parent.scrollTop = 0;
        }
      }
      initialized = true;
    });
  }

  playItem(item: ItemDto) {
    console.log('playitem clicked : ' + item.title);
    this.playItemClicked.emit(item);
  }

  addItemToPlaylist(item: ItemDto) {
    this.addItemToPlaylistClicked.emit(item);
  }

  addItemToPlaylistNext(item: ItemDto) {
    this.addItemToPlaylistNextClicked.emit(item);
  }

  private checkAllTracksSameDisc(data: ItemDto[]): boolean {
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

  private checkAllTracksSameAlbum(data: ItemDto[]): boolean {
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

  // Filtering lives in browse-filter.ts because the header's play / shuffle /
  // add-to-queue actions have to narrow the listing exactly the same way — what
  // reaches the renderer must be what this list shows.
  private filteredMusicTracks(data: ItemDto[]): ItemDto[] {
    return filterMusicItems(
      data,
      this.quickSearchString(),
      this.selectedGenres(),
      this.ratingFilter(),
    );
  }

  isBroadcast(item: ItemDto): boolean {
    return isBroadcastItem(item);
  }

  artistLabel(item: ItemDto): string {
    if (item.artistName) {
      return item.artistName;
    }
    if (this.isBroadcast(item) && !this.showGenre()) {
      return item.genre ?? '';
    }
    return '';
  }

  isSubstituteArtist(item: ItemDto): boolean {
    return !item.artistName && this.artistLabel(item).length > 0;
  }

  getDuration(item: ItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateStringShort(
        item.audioFormat.durationInSeconds,
      );
    } else {
      return '';
    }
  }

  showSongPopup(event: MouseEvent, item: ItemDto): void {
    this.songOptionsServiceService
      .openOptionsDialog(event, item, this.currentContainer)
      .subscribe((result) => {
        // handle user action from dialog ...
        if (result) {
          if (result.type == 'add') {
            console.log('added song to playlist : ' + item); // check item
          } else if (result.type == 'delete') {
            // Immediately, not on a timer: the delayed call used to land after the
            // refresh browse had already replaced the list.
            this.contentDirectoryService().deleteMusicTrack(result.data);
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

  public selectedRowClass(musicItemDto: ItemDto): string {
    if (this.isPlayingItem(musicItemDto)) {
      return 'selectRow';
    }
    return '';
  }

  // Compares against the renderer's currently-playing track *reactively*. Using
  // rendererService.currentTrack() (a signal that reflects the active player)
  // instead of manually-maintained fields fed only by the UPnP SSE event — that
  // never fires for the local "This device" browser player, so the indicator had
  // stopped appearing for local playback. Reading the signal here keeps the row
  // in sync for both UPnP renderers and the local player.
  public isPlayingItem(musicItemDto: ItemDto): boolean {
    if (!musicItemDto) {
      return false;
    }
    const current = this.rendererService.currentTrack();
    if (!current) {
      return false;
    }

    const matchByUrl =
      musicItemDto.streamingURL?.length > 0 &&
      musicItemDto.streamingURL === current.streamingURL;
    const matchByObjectId =
      musicItemDto.objectID?.length > 0 &&
      musicItemDto.objectID === current.objectID;

    return matchByUrl || matchByObjectId;
  }

}
