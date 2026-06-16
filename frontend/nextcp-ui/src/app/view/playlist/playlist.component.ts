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
      '/assets/images/playlist_bg.png',
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

  delete(): void {
    this.playlistService.deleteAll();
  }

  get hasPlaylistItems(): boolean {
    return this.playlistService.playlistItems().length > 0;
  }
}
