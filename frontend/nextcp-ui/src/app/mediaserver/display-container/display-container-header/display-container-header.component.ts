import { CdsBrowsePathService } from 'src/app/util/cds-browse-path.service';
import { ContainerDto, ContainerItemDto } from './../../../service/dto.d';
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
  viewChild,
  inject,
  afterNextRender,
  DestroyRef,
} from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {
  MatOption,
  MatSelect,
  MatSelectModule,
} from '@angular/material/select';
import { Observable } from 'rxjs';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { MusicItemDto } from 'src/app/service/dto';
import { MyMusicService } from 'src/app/service/my-music.service';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { TimeDisplayService } from 'src/app/util/time-display.service';
import { DisplayHeaderOptionsComponent } from '../../popup/display-header-options/display-header-options.component';

@Component({
  selector: 'display-container-header',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    FormsModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatOption,
    MatDialogModule,
  ],
  templateUrl: './display-container-header.component.html',
  styleUrl: './display-container-header.component.scss',
})
export class DisplayContainerHeaderComponent implements OnInit {
  private dialog = inject(MatDialog);
  private myMusicService = inject(MyMusicService);
  private dtoGeneratorService = inject(DtoGeneratorService);
  private backgroundImageService = inject(BackgroundImageService);
  private cdsBrowsePathService = inject(CdsBrowsePathService);
  private timeDisplayService = inject(TimeDisplayService);
  private destroyRef = inject(DestroyRef);

  readonly genresSelectbox = viewChild.required<MatSelect>('genresSelect');

  //
  // Collapsing sticky header
  /////////////////////////////////////

  // Scroll-linked collapse progress, 0 (full hero) .. 1 (compact bar).
  // Driven continuously by scroll position so the header shrinks gradually
  // instead of snapping — a binary toggle fed back into layout height and
  // caused flicker around the threshold.
  collapse = signal<number>(0);
  // Compact mode: buttons move to the right and the page-filter tools hide.
  // Switched once, early in the scroll, so it never affects header height.
  condensed = computed(() => this.collapse() > 0.06);

  // Scroll distance (px) over which the header fully collapses.
  private readonly COLLAPSE_RANGE = 150;
  private scrollParent: HTMLElement | null = null;
  private rafPending = false;

  private readonly onScroll = (): void => {
    if (this.rafPending) {
      return;
    }
    this.rafPending = true;
    requestAnimationFrame(() => {
      this.rafPending = false;
      const y = this.scrollParent?.scrollTop ?? 0;
      const next =
        Math.round(Math.min(1, Math.max(0, y / this.COLLAPSE_RANGE)) * 100) /
        100;
      if (next !== this.collapse()) {
        this.collapse.set(next);
        // Drive the CSS var on the scroll container so both the header and the
        // compensating bottom spacer (in display-container) inherit it.
        this.scrollParent?.style.setProperty('--collapse', String(next));
      }
    });
  };

  constructor() {
    afterNextRender(() => {
      this.scrollParent = document.getElementById('mainContent');
      this.scrollParent?.addEventListener('scroll', this.onScroll, {
        passive: true,
      });
      this.destroyRef.onDestroy(() => {
        this.scrollParent?.removeEventListener('scroll', this.onScroll);
        this.scrollParent?.style.removeProperty('--collapse');
      });
    });
  }

  //
  // signals
  /////////////////////////////////////

  contentDirectoryService = input.required<ContentDirectoryService>();

  listView = model<boolean>();
  quickSearchString = model<string>();
  selectedGenres = model<Array<string>>([]);
  // Album sort/group control (only shown when enableAlbumSort is true).
  sortCriteria = model<string>('NONE');
  enableAlbumSort = input<boolean>(false);

  playClicked = output<ContainerDto>();
  shuffleClicked = output<ContainerDto>();
  addToPlaylistClicked = output<ContainerDto>();

  genresList = signal<Array<String>>([]);
  currentAlbumLiked = signal<boolean>(false);
  totalPlaytimeShort = computed(() =>
    this.calcTotalPlaytimeShort(this.contentDirectoryService().musicTracks_()),
  );
  totalPlaytime = computed(() =>
    this.calcTotalPlaytimeLong(this.contentDirectoryService().musicTracks_()),
  );

  likePossible = computed(() => this.contentDirectoryService().albumIdExists());
  allTracksSameAlbum_ = signal<boolean>(false);
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

  ngOnInit(): void {
    if (this.contentDirectoryService) {
      this.contentDirectoryService().browseFinished$.subscribe((data) =>
        this.cdsBrowseFinished(),
      );
    }
  }

  private cdsBrowseFinished() {
    console.log('cdsBrowseFinished ... ');
    // A fresh browse result starts at the top, so show the full hero header.
    this.collapse.set(0);
    this.scrollParent?.style.setProperty('--collapse', '0');
    this.clearSearch();
    this.fillGenres();
    this.checkLikeStatus();
    this.backgroundImageService.setDisplayContainerHeaderImage(
      this.currentContainer.albumartUri,
    );
    this.cdsBrowsePathService.scrollIntoViewID();
  }

  get albums(): ContainerDto[] {
    return this.contentDirectoryService().albumList_();
  }

  private fillGenres(): void {
    console.log('filling genres. Items size is ' + this.musicTracks?.length);
    const mySet = new Set<String>();
    this.musicTracks?.forEach((value) => {
      if (value?.genre) {
        console.log('reading genre music tracks : ' + value.genre);
        let aGenre = value.genre.split('/');
        aGenre?.forEach((gen) => {
          mySet.add(gen.trim());
        });
      }
    });
    this.albums?.forEach((value) => {
      if (value?.genre) {
        console.log('reading genre albums : ' + value.genre);
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
    const albumIds = this.contentDirectoryService().getCurrentAlbumIds();

    if (!albumIds) {
      this.currentAlbumLiked.set(false);
      return;
    }

    if (this.likePossible()) {
      const likeStatusRequest = this.myMusicService.isAlbumLiked(albumIds);
      likeStatusRequest?.subscribe((res) => {
        console.log('current album liked : ' + res);
        this.currentAlbumLiked.set(res);
      });
    }
  }

  get isContainerAlbum(): boolean {
    // TODO can/should also be identified by other means
    return this.likePossible();
  }

  hasSongs(): boolean {
    return this.musicTracks?.length > 0 ? true : false;
  }

  // True when the current container actually lists albums (e.g. "My Albums").
  // The album sort/group control only makes sense then — not on a single
  // album's track list or a plain folder.
  hasAlbums(): boolean {
    return this.albums?.length > 0 ? true : false;
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

  public clearGenres(event: MouseEvent): void {
    this.genresSelectbox().options.forEach((item: MatOption) =>
      item.deselect(),
    );
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
    const albumIds = this.contentDirectoryService().getCurrentAlbumIds();

    if (this.likePossible() && albumIds != undefined) {
      const deleteLikeRequest = this.myMusicService.deleteAlbumLike(albumIds);
      deleteLikeRequest?.subscribe((d) => this.checkLikeStatus());
    } else {
      console.log('cannot dislike album, because no valid album ids found');
    }
  }

  likeAlbum(): void {
    const albumIds = this.contentDirectoryService().getCurrentAlbumIds();

    if (this.likePossible() && albumIds != undefined) {
      const likeAlbumRequest = this.myMusicService.likeAlbum(albumIds);
      likeAlbumRequest?.subscribe((d) => this.checkLikeStatus());
    } else {
      console.log('cannot like album, because no valid album ids found');
    }
  }

  toggleLikeAlbum(): void {
    console.log('toggle like album ... ');
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
    const completeTime = this.getTotalTimeSeconds(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return '';
  }

  private calcTotalPlaytimeShort(tracks: MusicItemDto[]): string {
    const completeTime = this.getTotalTimeSeconds(tracks);
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateStringShort(completeTime);
    } else {
      return '';
    }
  }

  private getTotalTimeSeconds(tracks: MusicItemDto[]): number {
    let completeTime: number;
    completeTime = 0;
    if (tracks.length > 0) {
      tracks.forEach(
        (el) =>
          (completeTime =
            completeTime +
            (el.audioFormat?.durationInSeconds
              ? el.audioFormat.durationInSeconds
              : 0)),
      );
    }
    console.log('total playtime seconds : ' + completeTime);
    return completeTime;
  }

  //
  // Button actions
  // ===============================================================================================
  public playContainer(): void {
    console.log('play container clicked ... ');
    this.playClicked.emit(this.currentContainer);
  }

  public shuffleContainer(): void {
    console.log('shuffle container clicked ... ');
    this.shuffleClicked.emit(this.currentContainer);
  }

  public addToPlaylist(): void {
    console.log('add to playlist clicked ... ');
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
    if (
      this.contentDirectoryService().currentContainerList().currentContainer
    ) {
      return this.contentDirectoryService().currentContainerList()
        .currentContainer;
    }
    return this.dtoGeneratorService.generateEmptyContainerDto();
  }

  public currentContainerItem(): ContainerItemDto {
    if (this.contentDirectoryService().currentContainerList()) {
      return this.contentDirectoryService().currentContainerList();
    }

    console.log('no current container item found, returning empty one');
    return this.dtoGeneratorService.generateEmptyContainerItemDto();
  }
}
