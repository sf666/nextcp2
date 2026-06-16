import { DeviceService } from 'src/app/service/device.service';
import { DefaultPlaylistService } from './../../mediaserver/popup/defaut-playlists/default-playlist.service';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { RendererService } from './../../service/renderer.service';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  signal,
  inject,
} from '@angular/core';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';
import { MusicLibraryService } from 'src/app/service/music-library/music-library.service';

@Component({
  selector: 'app-mediarenderer',
  templateUrl: './mediarenderer.component.html',
  styleUrls: ['./mediarenderer.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [StarRatingComponent],
})
export class MediarendererComponent implements OnInit {
  private defaultPlaylistService = inject(DefaultPlaylistService);
  deviceService = inject(DeviceService);
  private layoutService = inject(LayoutService);
  musicLibraryService = inject(MusicLibraryService);
  rendererService = inject(RendererService);

  showDetail = signal<boolean>(false);

  ngOnInit(): void {
    this.layoutService.setFramedView();
  }

  public hasCurrentSongTitle(): boolean {
    if (this.rendererService.trackInfoAvailable()) {
      return true;
    } else {
      return false;
    }
  }

  public getCurrentSongTitle(): string {
    return this.rendererService.currentSongTitle();
  }

  public getCurrentTrack(): MusicItemDto {
    return this.rendererService.currentTrack();
  }

  streaming(): boolean {
    return this.rendererService.isStreaming();
  }

  public getImgSrc(): string {
    return this.rendererService.imgSrc();
  }

  getStarSize(): string {
    return 'lg';
  }

  public get canBeAddedToPlaylist(): boolean {
    return this.rendererService.canCurrentTrackBeAddedToPlaylist();
  }

  openAddPlaylistDialog(): void {
    if (this.getCurrentTrack().objectID != null) {
      const dialogRef =
        this.defaultPlaylistService.openAddGlobalPlaylistDialogWithBackdrop(
          this.getCurrentTrack(),
          this.musicLibraryService.currentContainer(),
        );
    }
  }

  detailsClicked(): void {
    this.showDetail.set(!this.showDetail());
  }
}
