import { ContainerDto } from './../../../service/dto.d';
import {
  Component,
  OnInit,
  input,
  model,
  output,
  signal,
  computed,
  ChangeDetectionStrategy,
  ElementRef,
  ViewChild,
} from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatOption, MatSelect, MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { MusicItemDto } from 'src/app/service/dto';
import { MyMusicService } from 'src/app/service/my-music.service';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { DisplayHeaderOptionsComponent } from '../../popup/display-header-options/display-header-options.component';
import { MatDivider } from '@angular/material/divider';

@Component({
  selector: 'display-container-header',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatDivider,
    FormsModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatOption,
    MatDialogModule
  ],
  templateUrl: './display-container-header.component.html',
  styleUrl: './display-container-header.component.scss',
})

export class DisplayContainerHeaderComponent implements OnInit {

  @ViewChild('genresSelect') genresSelectbox : MatSelect;

  //
  // signals
  /////////////////////////////////////

  contentDirectoryService = input.required<ContentDirectoryService>();

  listView = model<boolean>();
  quickSearchString = model<string>();
  selectedGenres = model<Array<string>>([]);

  playClicked = output<ContainerDto>();
  shuffleClicked = output<ContainerDto>();
  addToPlaylistClicked = output<ContainerDto>();

  genresList = signal<Array<String>>([]);
  currentAlbumLiked = signal<boolean>(false);
  totalPlaytimeShort = computed(() => this.calcTotalPlaytimeShort(this.contentDirectoryService().musicTracks_()));
  totalPlaytime = computed(() => this.calcTotalPlaytimeLong(this.contentDirectoryService().musicTracks_()));

  likePossible = computed(() => this.allTracksSameMusicBrainzReleaseId_());
  allTracksSameMusicBrainzReleaseId_ = signal<boolean>(false);
  allTracksSameAlbum_ = signal<boolean>(false);
  currentAlbumReleaseID = signal<string>('');
  mediaServerExists = signal<boolean>(false);

  genresForm = new FormControl('');


  containerType = computed(() => {
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
  });

  constructor(
    private dialog: MatDialog,
    private myMusicService: MyMusicService,
    private dtoGeneratorService: DtoGeneratorService,
    private backgroundImageService: BackgroundImageService,
    private timeDisplayService: TimeDisplayService,
  ) {
  }

  ngOnInit(): void {
    if (this.contentDirectoryService) {
      this.contentDirectoryService().browseFinished$.subscribe((data) =>
        this.cdsBrowseFinished()
      );
    }
  }

  private cdsBrowseFinished() {
    console.log("cdsBrowseFinished ... ");
    this.clearSearch();    
    this.fillGenres();
    this.checkAllTracksSameAlbum();
    this.checkLikeStatus();
    this.backgroundImageService.setDisplayContainerHeaderImage(this.currentContainer.albumartUri);
    console.log("like possible : " + this.likePossible());
  }

  get albums(): ContainerDto[] {
    return this.contentDirectoryService().albumList_();
  }

  private fillGenres(): void {
    console.log("filling genres. Items size is " + this.musicTracks?.length);
    const mySet = new Set<String>();
    this.musicTracks?.forEach((value) => {
      if (value?.genre) {
        console.log("reading genre music tracks : " + value.genre);
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          mySet.add(gen.trim());
        });
      }
    });
    this.albums?.forEach((value) => {
      if (value?.genre) {
        console.log("reading genre albums : " + value.genre);
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          mySet.add(gen.trim());
        });
      }
    });

    console.log('sorting genres ... ');
    this.genresList.set(Array.from(mySet.values()).sort());
  }

  private checkLikeStatus() {
    if (this.allTracksSameMusicBrainzReleaseId_()) {
      if (this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId) {
        console.log("set current album release id to : " + this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId);
        this.currentAlbumReleaseID.set(this.musicTracks[0].musicBrainzId.ReleaseTrackId);
        this.myMusicService
          .isAlbumLiked(this.currentAlbumReleaseID())
          .subscribe(
            (res) => {
              console.log("current album liked : " + res);
              (this.currentAlbumLiked.set(res));
            }
          );
      }
    } else {
      this.currentAlbumLiked.set(false);
      this.currentAlbumReleaseID.set('');
    }
  }

  get isContainerAlbum(): boolean {
    // TODO can/should also be identified by other means
    return this.allTracksSameMusicBrainzReleaseId_();
  }

  hasSongs(): boolean {
    return this.musicTracks?.length > 0 ? true : false;
  }

  hasGenres(): boolean {
    return this.genresList()?.length > 0 ? true : false;
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
    this.quickSearchString.set('');
  }

  public clearGenres(event): void {
    this.genresSelectbox.options.forEach((item: MatOption) => item.deselect());
    event.stopPropagation();
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
    return this.currentAlbumLiked();
  }

  dislikeAlbum(): void {
    this.myMusicService
      .deleteAlbumLike(this.currentAlbumReleaseID())
      .subscribe((d) => this.checkLikeStatus());
  }

  likeAlbum(): void {
    this.myMusicService
      .likeAlbum(this.currentAlbumReleaseID())
      .subscribe((d) => this.checkLikeStatus());
  }

  toggleLikeAlbum(): void {
    if (this.isLiked()) {
      this.dislikeAlbum();
    } else {
      this.likeAlbum();
    }
  }

  toggleListView(): void {
    if (this.listView) {
      this.listView.update((lv) => (lv = !lv));
    }
  }

  //
  // Accessor
  //
  get musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService().musicTracks_();
  }

  // Like support
  private checkAllTracksSameAlbum(): void {
    const numtrack = this.musicTracks?.length;
    const numMbid = this.musicTracks?.filter(
      (item) => item.musicBrainzId?.ReleaseTrackId?.length > 0
    ).length;

    console.log('number of tracs : ' + numtrack);
    console.log('number of tracs with mbid: ' + numMbid);

    this.allTracksSameMusicBrainzReleaseId_.set(false);

    if (numMbid > 0 && numtrack == numMbid) {
      const firstTrackMbid = this.musicTracks[0]?.musicBrainzId?.ReleaseTrackId;
      const numSameMbid = this.musicTracks.filter(
        (item) => item.musicBrainzId?.ReleaseTrackId === firstTrackMbid
      ).length;
      this.allTracksSameAlbum_.set(numSameMbid == numMbid);
      this.allTracksSameMusicBrainzReleaseId_.set(this.allTracksSameAlbum_());
      console.log(
        'number of tracs with same mbid like first track : ' + numSameMbid
      );
    } else {
      this.allTracksSameMusicBrainzReleaseId_.set(false);
      if (this.musicTracks?.length > 0) {
        const firstTrackAlbum = this.musicTracks[0].album;
        if (firstTrackAlbum != null) {
          const albumsWithOtherNames = this.musicTracks.filter(
            (item) => item.album !== firstTrackAlbum
          ).length;
          this.allTracksSameAlbum_.set(albumsWithOtherNames == 0);
          console.log('number of tracks with other album title : ' + albumsWithOtherNames);  
        } else {
          this.allTracksSameAlbum_.set(false);
          console.log('music track have no album information');  
        }
      }
    }
    console.log('checkAllTracksSameAlbum : ' + this.allTracksSameAlbum_());
    console.log(
      'checkAllTracksSameMusicbrainzReleaseId : ' +
      this.allTracksSameMusicBrainzReleaseId_()
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

  private calcTotalPlaytimeLong(tracks: MusicItemDto[]): string {
    const completeTime = this.getTotalTimeString(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return '';
  }

  private calcTotalPlaytimeShort(tracks: MusicItemDto[]): string {
    const completeTime = this.getTotalTimeString(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateStringShort(completeTime);
    } else {
      return '';
    }
  }

  private getTotalTimeString(tracks: MusicItemDto[]): number {
    let completeTime: number;
    if (tracks.length > 0) {
      completeTime = 0;
      tracks.forEach(
        (el) =>
        (completeTime =
          completeTime +
          (el.audioFormat?.durationInSeconds
            ? el.audioFormat.durationInSeconds
            : 0))
      );
    }
    console.log("total playtime seconds : " + completeTime);
    return completeTime;
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


  public openOptionsDialog(event: MouseEvent): Observable<any> {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(DisplayHeaderOptionsComponent, {
      hasBackdrop: true,
      data: {
        trigger: target,
        addToPlaylistOutput: this.addToPlaylistClicked,
        event: event,
        currentContainer: this.currentContainer,
      },
    });
    return dialogRef.afterClosed();
  }

  // Other
  public get currentContainer(): ContainerDto {
    if (this.contentDirectoryService().currentContainerList().currentContainer) {
      return this.contentDirectoryService().currentContainerList().currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }
}
