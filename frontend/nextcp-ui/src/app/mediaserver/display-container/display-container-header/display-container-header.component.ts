import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatOption, MatSelectModule } from '@angular/material/select';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import {
  ContainerDto,
  ContainerItemDto,
  MusicItemDto,
} from 'src/app/service/dto';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';

@Component({
  selector: 'display-container-header',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, MatInputModule, FormsModule, MatSelectModule, ReactiveFormsModule, MatOption],
  templateUrl: './display-container-header.component.html',
  styleUrl: './display-container-header.component.scss',
})
export class DisplayContainerHeaderComponent implements OnInit {
  @Input() contentDirectoryService: ContentDirectoryService;

  @Output() playClicked = new EventEmitter<ContainerDto>();
  @Output() shuffleClicked = new EventEmitter<ContainerDto>();
  @Output() toggleListViewClicked = new EventEmitter<any>();
  @Output() addToPlaylistClicked = new EventEmitter<any>();
  @Output() toggleLikeAlbumClicked = new EventEmitter<any>();

  genresForm = new FormControl('');

  // search
  quickSearchString: string;
  genresList: Set<String>;
  genresListSorted: Array<String>;
  selectedGenres: Array<string> = [];

  // like member
  currentAlbumLiked = false;
  private allTracksSameMusicBrainzReleaseId_: boolean;
  private allTracksSameAlbum_: boolean;


  constructor(
    private dtoGeneratorService: DtoGeneratorService,
    private backgroundImageService: BackgroundImageService,
    private timeDisplayService: TimeDisplayService
  ) {}

  ngOnInit(): void {
    if (this.contentDirectoryService) {
      this.contentDirectoryService.browseFinished$.subscribe((data) =>
        this.cdsBrowseFinished()
      );
    }
  }

  private cdsBrowseFinished() {
    this.fillGenres();
    this.checkAllTracksSameAlbum();
    this.backgroundImageService.setDisplayContainerHeaderImage(this.currentContainer.albumartUri);
  }

  get albums(): ContainerDto[] {
    return this.contentDirectoryService.albumList_;
  }

  private fillGenres(): void {
    this.genresList = new Set();
    this.musicTracks?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          this.genresList.add(gen.trim());
        });
      }
    });
    this.albums?.forEach((value) => {
      if (value?.genre) {
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          this.genresList.add(gen.trim());
        });
      }
    });

    this.genresListSorted = [...this.genresList].sort();
  }

  likePossible(): boolean {
    return this.allTracksSameMusicBrainzReleaseId_;
  }

  isContainerAlbum(): boolean {
    // TODO can/should also be identified by other means
    return this.allTracksSameMusicBrainzReleaseId_;
  }

  hasSongs(): boolean {
    return this.musicTracks?.length > 0 ? true : false;
  }

  hasGenres(): boolean {
    return this.genresList?.size > 0 ? true : false;
  }

  //
  // Search support
  // ===============================================================================================

  keyUp(event: KeyboardEvent): void {
    if (event.key === 'Escape') {
      this.clearSearch();
    }
  }

  public clearSearch(): void {
    this.quickSearchString = '';
  }


  //
  // Like section
  // ==============================================================================

  getFavoriteCssBtnClass(): string {
    if (this.isLiked()) {
      return 'liked';
    } else {
      return 'disliked';
    }
  }

  getFavoriteCssIconClass(): string {
    if (this.isLiked()) {
      return 'filled';
    } else {
      return '';
    }
  }

  isLiked(): boolean {
    return this.currentAlbumLiked;
  }

  toggleListView(): void {
    this.toggleListViewClicked.emit();
  }

  toggleLikeAlbum(): void {
    this.toggleLikeAlbumClicked.emit();
  }

  //
  // Accessor
  //
  get musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_;
  }

  // Like support 
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


  //
  // Statistics
  // ===============================================================================================

  public get musicItemsCount(): number {
    if (this.musicTracks?.length) {
      return this.musicTracks.length;
    } else {
      return 0;
    }
  }

  get containerType(): string {
    if (
      this.currentContainer.objectClass === 'object.container.playlistContainer'
    ) {
      return 'Playlist';
    } else if (
      this.currentContainer.objectClass === 'object.container.album.musicAlbum'
    ) {
      return 'Album';
    } else {
      return 'Folder';
    }
  }

  get totalPlaytime(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.musicTracks.length > 0) {
      this.musicTracks?.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0))
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return '';
  }

  get totalPlaytimeShort(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.musicTracks.length > 0) {
      this.musicTracks?.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0))
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateStringShort(completeTime);
    }
    return '';
  }

  //
  // Button actions
  // ===============================================================================================
  public playContainer(): void {
    this.playClicked.emit(this.currentContainer);
  }

  public shuffleContainer(): void {
    this.shuffleClicked.emit(this.currentContainer);
  }

  public addToPlaylist(): void {
    this.addToPlaylistClicked.emit(this.currentContainer);
  }
  

  // Other
  public get currentContainer(): ContainerDto {
    if (this.contentDirectoryService.currentContainerList?.currentContainer) {
      return this.contentDirectoryService.currentContainerList.currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }
}
