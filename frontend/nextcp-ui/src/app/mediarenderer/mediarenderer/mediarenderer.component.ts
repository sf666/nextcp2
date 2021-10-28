import { DefaultPlaylistService } from './../../mediaserver/popup/defaut-playlists/default-playlist.service';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { BackgroundImageService } from './../../util/background-image.service';
import { RendererService } from './../../service/renderer.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-mediarenderer',
  templateUrl: './mediarenderer.component.html',
  styleUrls: ['./mediarenderer.component.scss']
})

export class MediarendererComponent implements OnInit {

  public showDetail: boolean;

  constructor(
    private defaultPlaylistService: DefaultPlaylistService,
    private layoutService: LayoutService,
    private backgroundImageService: BackgroundImageService,
    public rendererService: RendererService) {
    this.showDetail = false;
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
  }

  public getCurrentSongTitle(): string {
    if (this.rendererService.trackInfoAvailable) {
      return this.rendererService.trackInfo?.currentTrack?.title;
    }
    else {
      return "no track info available";
    }
  }

  public getCurrentTrack(): MusicItemDto {
    if (this.rendererService.trackInfoAvailable) {
      return this.rendererService.trackInfo?.currentTrack;
    }
  }

  streaming(): boolean {
    const streaming = this.rendererService?.trackTime?.streaming;
    return streaming;
  }

  public getImgSrc(): string {
    if (this.rendererService.trackInfo?.currentTrack?.albumArtUrl) {
      this.backgroundImageService.setBackgroundImageMainScreen(this.rendererService.trackInfo?.currentTrack?.albumArtUrl);
      return this.rendererService.trackInfo?.currentTrack?.albumArtUrl;
    }
    else {
      return "";
    }
  }

  getStarSize(): string {
    return "lg";
  }

  public get canBeAddedToPlaylist(): boolean {
    return this.rendererService.trackInfo?.currentTrack?.musicBrainzId?.TrackId?.length > 0;
  }

  openAddPlaylistDialog(event: any, mbid: string): void {
    this.defaultPlaylistService.openAddPlaylistDialog(event, mbid);
  }

  detailsClicked(): void {
    this.showDetail = !this.showDetail;
  }
}
