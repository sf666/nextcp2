import { DefautPlaylistsComponent } from './../../mediaserver/popup/defaut-playlists/defaut-playlists.component';
import { PlaylistComponent } from './../../view/playlist/playlist.component';
import { MatDialog } from '@angular/material/dialog';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { MatSliderChange } from '@angular/material/slider';
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

  constructor(
    sseService: SseService,
    private deviceService: DeviceService,
    private dialog: MatDialog,
    private layoutService: LayoutService,
    private backgroundImageService: BackgroundImageService,
    public rendererService: RendererService) {

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

  public get canBeAddedToPlaylist(): boolean{
    return this.rendererService.trackInfo?.currentTrack?.musicBrainzId?.TrackId.length > 0;
  }

  openAddPlaylistDialog(event: any, mbid: string) {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(DefautPlaylistsComponent, {
      data: { trigger: target, id :  mbid},
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }
}
