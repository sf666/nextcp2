import { ContainerItemDto } from './../../../service/dto.d';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  input,
  output,
  signal,
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

@Component({
  selector: 'item-tile',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [StarRatingComponent, QualityBadgeComponent],
  templateUrl: './item-tile.component.html',
  styleUrl: './item-tile.component.scss',
})
export class ItemTileComponent implements OnInit {
  isListView = input<boolean>(false);
  contentDirectoryService = input.required<ContentDirectoryService>();
  quickSearchString = input<string>('');
  selectedGenres = input<Array<string>>([]);
  extendedApi = input<boolean>(true);

  playItemClicked = output<MusicItemDto>();
  addItemToPlaylistClicked = output<MusicItemDto>();

  // some calculated constants
  allTracksSameDisc = computed(() => this.checkAllTracksSameDisc(this.allMusicTracks()));
  allTracksSameAlbum = computed(() => this.checkAllTracksSameAlbum(this.allMusicTracks()));
  musicTracks = computed(() => this.filteredMusicTracks(this.allMusicTracks()));

  allMusicTracks = signal<MusicItemDto[]>([]);

  private currentUrl = '';
  private lastDiscLabel = '';

  constructor(
    private songOptionsServiceService: SongOptionsServiceService,
    private dtoGeneratorService: DtoGeneratorService,
    private timeDisplayService: TimeDisplayService,
    private deviceService: DeviceService,
    private sseService: SseService
  ) {
    sseService.mediaRendererTrackInfoChanged$.subscribe((data) => {
      if (
        data?.mediaRendererUdn &&
        data?.currentTrack &&
        deviceService.isMediaRendererSelected(data.mediaRendererUdn)
      ) {
        this.currentUrl = data.currentTrack.streamingURL;
      }
    });
  }

  ngOnInit(): void {
    if (this.contentDirectoryService()) {
      this.contentDirectoryService().browseFinished$.subscribe((data) =>
        this.cdsBrowseFinished(data)
      );
    }
  }

  playItem(item: MusicItemDto) {
    this.playItemClicked.emit(item);
  }

  addItemToPlaylist(item: MusicItemDto) {
    this.addItemToPlaylistClicked.emit(item);
  }

  private cdsBrowseFinished(data: ContainerItemDto) {
    this.allMusicTracks.set(data.musicItemDto);
  }

  private checkAllTracksSameDisc(data: MusicItemDto[]): boolean {
    if (data.length > 0) {
      const firstTrackDisc = data[0]?.numberOfThisDisc;
      const sameDisc = !data?.find(
        (item) => item.numberOfThisDisc !== firstTrackDisc
      );
      console.log('allTracksSameDisc : ' + sameDisc);
      return sameDisc;
    }
  }

  private checkAllTracksSameAlbum(data: MusicItemDto[]): boolean {
    const numtrack = this.musicTracks?.length;
    const numMbid = this.musicTracks()?.filter(
      (item) => item.musicBrainzId?.ReleaseTrackId?.length > 0
    ).length;

    console.log('number of tracs : ' + numtrack);
    console.log('number of tracs with mbid: ' + numMbid);

    if (numMbid > 0 && numtrack == numMbid) {
      const firstTrackMbid = data[0]?.musicBrainzId?.ReleaseTrackId;
      const numSameMbid = data.filter(
        (item) => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid
      ).length;
      console.log(
        'number of tracs with same mbid like first track : ' + numSameMbid
      );
      return numSameMbid == numMbid;
    } else {
      if (data.length > 0) {
        const firstTrackAlbum = data[0].album;
        const albumsWithOtherNames = data.filter(
          (item) => item.album !== firstTrackAlbum
        ).length;
        console.log(
          'number of tracs with other album title : ' + albumsWithOtherNames
        );
        return albumsWithOtherNames == 0;
      }
    }
  }

  private filteredMusicTracks(data: MusicItemDto[]): MusicItemDto[] {
    console.log("filteredMusicTracks");
    let tracks: Array<MusicItemDto>;
    if (this.quickSearchString()) {
      tracks = data.filter((item) =>
        this.doFilterText(item.title, this.quickSearchString())
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

  private doFilterText(title: string, filter?: string): boolean {
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
  getTitleResponsiveClass(): string {
    if (!this.allTracksSameAlbum()) {
      return 'col-8 col-sm-7 col-md-5 col-lg-5 col-xl-3';
    } else {
      return 'col-11 col-sm-9 col-md-7 col-lg-6 col-xl-6';
    }
  }

  getDuration(item: MusicItemDto): string {
    if (item.audioFormat?.durationInSeconds) {
      return this.timeDisplayService.convertLongToDateStringShort(
        item.audioFormat.durationInSeconds
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
            console.log('added song to playlist : ');
          } else if (result.type == 'delete') {
            console.log('deleted song from playlist : ');
            setTimeout(() => {
              this.contentDirectoryService().deleteMusicTrack(item);
            }, 200);
          } else if (result.type == 'download') {
            console.log('download song : ');
          } else if (result.type == 'next') {
            console.log('play next song : ');
          }
        }
      });
  }
  public get currentContainer(): ContainerDto {
    if (this.contentDirectoryService().currentContainerList?.currentContainer) {
      return this.contentDirectoryService().currentContainerList
        .currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  public selectedRowClass(musicItemDto: MusicItemDto): string {
    if (this.currentUrl && musicItemDto?.streamingURL == this.currentUrl) {
      return 'selectRow';
    }
    return '';
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
