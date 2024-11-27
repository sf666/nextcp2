import { DeviceService } from 'src/app/service/device.service';
import { DefaultPlaylistService } from './../../mediaserver/popup/defaut-playlists/default-playlist.service';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { BackgroundImageService } from './../../util/background-image.service';
import { RendererService } from './../../service/renderer.service';
import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { MatInput } from '@angular/material/input';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatListSubheaderCssMatStyler } from '@angular/material/list';
import { MatButton } from '@angular/material/button';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';

@Component({
    selector: 'app-mediarenderer',
    templateUrl: './mediarenderer.component.html',
    styleUrls: ['./mediarenderer.component.scss'],
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [StarRatingComponent, MatButton, MatListSubheaderCssMatStyler, FormsModule, MatFormField, MatLabel, MatInput]
})

export class MediarendererComponent implements OnInit {

  showDetail = signal<boolean>(false);

  constructor(
    private defaultPlaylistService: DefaultPlaylistService,
    public deviceService: DeviceService,
    private layoutService: LayoutService,
    private backgroundImageService: BackgroundImageService,
    public rendererService: RendererService) {
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
  }

  public hasCurrentSongTitle(): boolean {
    if (this.rendererService.trackInfoAvailable) {
      return true;
    }
    else {
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
    return "lg";
  }

  public get canBeAddedToPlaylist(): boolean {
    return this.rendererService.canCurrentTrackBeAddedToPlaylist();
  }

  openAddPlaylistDialog(event: any, fileId: string): void {
    this.defaultPlaylistService.openAddPlaylistDialog(event, fileId);
  }

  detailsClicked(): void {
    this.showDetail.set(!this.showDetail());
  }
}
