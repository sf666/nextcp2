import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
  imports: [StarRatingComponent, QualityBadgeComponent,],
  templateUrl: './item-tile.component.html',
  styleUrl: './item-tile.component.scss',
})
export class ItemTileComponent implements OnInit {
  @Input() isListView: boolean = false;
  @Input() contentDirectoryService: ContentDirectoryService;
  @Input() quickSearchString: string;
  @Input() selectedGenres: Array<string> = [];
  @Input() extendedApi: boolean = true;
  
  @Output() playItemClicked = new EventEmitter<MusicItemDto>();
  @Output() addItemToPlaylistClicked = new EventEmitter<MusicItemDto>();

  // some calculated constants
  private allTracksSameAlbum_: boolean;
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameDisc_: boolean;

  private currentUrl = '';
  private lastDiscLabel = '';

  constructor(
    private songOptionsServiceService: SongOptionsServiceService,
    private dtoGeneratorService: DtoGeneratorService,
    private timeDisplayService: TimeDisplayService,
    private deviceService: DeviceService,
    private sseService: SseService,
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
    if (this.contentDirectoryService) {
      this.contentDirectoryService.browseFinished$.subscribe((data) =>
        this.cdsBrowseFinished()
      );
    }
  }

  playItem(item: MusicItemDto) {
    this.playItemClicked.emit(item);
  }

  addItemToPlaylist(item: MusicItemDto) {
    this.addItemToPlaylistClicked.emit(item);
  }

  private cdsBrowseFinished() {
    this.checkAllTracksSameAlbum();
    this.checkAllTracksSameDisc();
  }

  get musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_;
  }

  public allTracksSameDisc(): boolean {
    return this.allTracksSameDisc_;
  }

  public allTracksSameAlbum(): boolean {
    return this.allTracksSameAlbum_;
  }

  private checkAllTracksSameDisc(): void {
    if (this.musicTracks?.length > 0) {
      const firstTrackDisc = this.musicTracks[0]?.numberOfThisDisc;
      this.allTracksSameDisc_ = !this.musicTracks?.find(
        (item) => item.numberOfThisDisc !== firstTrackDisc
      );
    }
    console.log('allTracksSameDisc_ : ' + this.allTracksSameDisc_);
  }

  private checkAllTracksSameAlbum(): void {
    const numtrack = this.musicTracks?.length;
    const numMbid = this.musicTracks?.filter(
      (item) => item.musicBrainzId?.ReleaseTrackId?.length > 0
    ).length;

    console.log('number of tracs : ' + numtrack);
    console.log('number of tracs with mbid: ' + numMbid);

    this.allTracksSameMusicBrainzReleaseId_ = false;

    if (numMbid > 0 && numtrack == numMbid) {
      const firstTrackMbid = this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId;
      const numSameMbid = this.musicTracks.filter(
        (item) => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid
      ).length;
      this.allTracksSameAlbum_ = numSameMbid == numMbid;
      this.allTracksSameMusicBrainzReleaseId_ = this.allTracksSameAlbum_;
      console.log(
        'number of tracs with same mbid like first track : ' + numSameMbid
      );
    } else {
      if (this.musicTracks?.length > 0) {
        const firstTrackAlbum = this.musicTracks[0].album;
        const albumsWithOtherNames = this.musicTracks.filter(
          (item) => item.album !== firstTrackAlbum
        ).length;
        this.allTracksSameAlbum_ = albumsWithOtherNames == 0;
        console.log(
          'number of tracs with other album title : ' + albumsWithOtherNames
        );
      }
    }
    console.log('checkAllTracksSameAlbum : ' + this.allTracksSameAlbum_);
    console.log(
      'checkAllTracksSameMusicbrainzReleaseId : ' +
        this.allTracksSameMusicBrainzReleaseId_
    );
  }

  get allMusicTracks() {
    return this.filteredMusicTracks(true);
  }
  
  private filteredMusicTracks(filter?: boolean): MusicItemDto[] {
    if (filter) {
      let tracks: Array<MusicItemDto>;
      if (this.quickSearchString) {
        tracks = this.musicTracks.filter((item) =>
          this.doFilterText(item.title, this.quickSearchString)
        );
      } else {
        tracks = this.musicTracks;
      }
      if (this?.selectedGenres?.length > 0) {
        tracks = tracks.filter((item) => this.doFilterGenre(item));
      }
      return tracks;
    } else {
      return this.musicTracks;
    }
  }

  private doFilterGenre(item: MusicItemDto): boolean {
    let add = false;
    this.selectedGenres?.forEach((genre) => {
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

  private doFilterGenreByContainer(container: ContainerDto): boolean {
    let add = false;
    this.selectedGenres?.forEach((genre) => {
      if (this.doFilterText(container.genre, genre)) {
        add = true;
      }
    });
    return add;
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
              this.contentDirectoryService.deleteMusicTrack(item);
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
    if (this.contentDirectoryService.currentContainerList?.currentContainer) {
      return this.contentDirectoryService.currentContainerList.currentContainer;
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
