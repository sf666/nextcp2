import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { DeviceService } from './../../service/device.service';
import { ItemDto } from './../../service/dto.d';
import { PlaylistService } from '../../service/playlist.service';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { MatDialog } from '@angular/material/dialog';
import {
  ConfirmPopupComponent,
  ConfirmPopupData,
} from 'src/app/util/comp/confirm-popup/confirm-popup.component';

@Component({
  selector: 'playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    CdsBrowsePathService,
    { provide: 'uniqueId', useValue: 'cds_playlist' },
  ],
  standalone: true,
  imports: [QualityBadgeComponent],
})
export class PlaylistComponent implements OnInit {
  deviceService = inject(DeviceService);
  private sseService = inject(SseService);
  private layoutService = inject(LayoutService);
  private backgroundImageService = inject(BackgroundImageService);
  private confirmDialog = inject(MatDialog);
  playlistService = inject(PlaylistService);

  constructor() {
    const deviceService = this.deviceService;
    const sseService = this.sseService;
    const scrollViewService = inject(CdsBrowsePathService);

    sseService.mediaRendererPlaylistStateChanged$.subscribe((data) => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        scrollViewService.scrollIntoViewID(`PL-${data.Id}`);
      }
    });
  }

  ngOnInit(): void {
    this.layoutService.setFramedViewWithoutNavbar();
    this.playlistService.updatePlaylistItems();
    this.backgroundImageService.setBackgroundImageMainScreen(
      '/assets/images/playlist_bg.webp',
    );
  }

  isActive(item: ItemDto, index: number): boolean {
    return this.playlistService.isActiveEntry(item, index);
  }

  play(): void {
    this.playlistService.play();
  }

  /**
   * Takes one track out of the queue.
   *
   * Stops the click here: the row itself starts playback, and removing a track must not also start
   * the one that slid into its place.
   */
  remove(event: Event, item: ItemDto, index: number): void {
    event.stopPropagation();
    this.playlistService.removeEntry(item, index);
  }

  /**
   * How long one track runs, as a listener reads it.
   *
   * UPnP reports a full clock with milliseconds ("0:03:32.000"), which is precision nobody queues
   * music by - and six glyphs of it are always the same three characters.
   */
  trackLength(item: ItemDto): string {
    const seconds = item.audioFormat?.durationInSeconds ?? 0;
    if (seconds <= 0) {
      return '';
    }
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const rest = Math.floor(seconds % 60);
    const shownMinutes = hours > 0 ? String(minutes).padStart(2, '0') : String(minutes);
    return `${hours > 0 ? hours + ':' : ''}${shownMinutes}:${String(rest).padStart(2, '0')}`;
  }

  /** What the queue adds up to, for the line under the title. Empty while no track reports a length. */
  totalPlaytime = computed<string>(() => {
    const seconds = this.playlistService
      .playlistItems()
      .reduce((sum, item) => sum + (item.audioFormat?.durationInSeconds ?? 0), 0);
    if (seconds <= 0) {
      return '';
    }
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.round((seconds % 3600) / 60);
    if (hours > 0) {
      return minutes > 0 ? `${hours} h ${minutes} min` : `${hours} h`;
    }
    return `${Math.max(1, minutes)} min`;
  });

  /** "12 tracks, 48 min on Living Room" — what is queued, how long it runs and where it plays. */
  summary = computed<string>(() => {
    const count = this.playlistService.playlistItems().length;
    const tracks = count === 1 ? '1 track' : `${count} tracks`;
    const playtime = this.totalPlaytime();
    const renderer = this.deviceService.selectedMediaRendererDevice().friendlyName;
    return `${tracks}${playtime ? ', ' + playtime : ''} on ${renderer}`;
  });

  /** Clearing the whole queue in one click is worth a question. */
  delete(): void {
    const count = this.playlistService.playlistItems().length;
    const confirmData: ConfirmPopupData = {
      title: 'Clear queue',
      message: 'This takes every track out of the player queue.',
      detail: count === 1 ? '1 track' : `${count} tracks`,
      confirmText: 'clear queue',
      cancelText: 'cancel',
      danger: true,
    };
    const dialogRef = this.confirmDialog.open(ConfirmPopupComponent, {
      width: '420px',
      maxWidth: '90vw',
      panelClass: ['popup-glass'],
      data: confirmData,
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed === true) {
        this.playlistService.deleteAll();
      }
    });
  }

  get hasPlaylistItems(): boolean {
    return this.playlistService.playlistItems().length > 0;
  }
}
