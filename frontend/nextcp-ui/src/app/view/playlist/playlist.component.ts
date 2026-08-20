import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { DeviceService } from './../../service/device.service';
import { MusicItemDto } from './../../service/dto.d';
import { PlaylistService } from '../../service/playlist.service';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
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
  imports: [MatButton, MatIcon, QualityBadgeComponent],
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

  getActiveClass(item: MusicItemDto): string {
    const id: number = +item.objectID;
    if (id === this.playlistService.playlistState().Id) {
      return 'active';
    }
    return '';
  }

  play(): void {
    this.playlistService.play();
  }

  /** Clearing the whole queue in one click is worth a question. */
  delete(): void {
    const count = this.playlistService.playlistItems().length;
    const confirmData: ConfirmPopupData = {
      title: 'Clear playlist',
      message: 'This removes every track from the player playlist.',
      detail: count === 1 ? '1 track' : `${count} tracks`,
      confirmText: 'clear all',
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
