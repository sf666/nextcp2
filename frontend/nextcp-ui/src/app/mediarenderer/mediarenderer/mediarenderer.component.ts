import { MusicItemDto } from './../../service/dto.d';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { MatSliderChange } from '@angular/material/slider';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-mediarenderer',
  templateUrl: './mediarenderer.component.html',
  styleUrls: ['./mediarenderer.component.scss']
})

export class MediarendererComponent {

  private _mediaServerUdn: string;
  private _mediaRendererUdn: string;

  constructor(
    sseService: SseService,
    private deviceService: DeviceService,
    private backgroundImageService: BackgroundImageService,
    public rendererService: RendererService) {

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
}
