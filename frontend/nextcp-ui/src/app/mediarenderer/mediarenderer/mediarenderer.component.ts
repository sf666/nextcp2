import { DefaultPlaylistService } from './../../mediaserver/popup/defaut-playlists/default-playlist.service';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { Component, OnInit, ElementRef } from '@angular/core';

@Component({
  selector: 'app-mediarenderer',
  templateUrl: './mediarenderer.component.html',
  styleUrls: ['./mediarenderer.component.scss']
})

export class MediarendererComponent implements OnInit {

  private _mediaServerUdn: string;
  private _mediaRendererUdn: string;

  public showDetail : boolean;

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

  streaming() {
    let streaming: boolean;
    streaming = this.rendererService?.trackTime?.streaming;
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

  getStarSize() {
    return "lg";
  }

  public get canBeAddedToPlaylist(): boolean{
    return this.rendererService.trackInfo?.currentTrack?.musicBrainzId?.TrackId?.length > 0;
  }

  openAddPlaylistDialog(event: any, mbid: string) {
    this.defaultPlaylistService.openAddPlaylistDialog(event, mbid);
  }

  detailsClicked() : void {
    this.showDetail = !this.showDetail;
  }
}
