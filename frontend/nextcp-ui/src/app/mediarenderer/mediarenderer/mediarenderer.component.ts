import { DeviceService } from 'src/app/service/device.service';
import { DefaultPlaylistService } from './../../mediaserver/popup/defaut-playlists/default-playlist.service';
import { LayoutService } from './../../service/layout.service';
import { MusicItemDto } from './../../service/dto.d';
import { RendererService } from './../../service/renderer.service';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  effect,
  signal,
  inject,
} from '@angular/core';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';
import { MusicLibraryService } from 'src/app/service/music-library/music-library.service';
import { BackgroundImageService } from '../../util/background-image.service';

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
  private backgroundImageService = inject(BackgroundImageService);

  showDetail = signal<boolean>(false);

  constructor() {
    // Drive the living-canvas wash from the current track while this screen is
    // open, so navigating straight to "Now Listening" isn't a black void. The
    // effect is torn down with the component when the user leaves the screen.
    effect(() => {
      const url = this.rendererService.imgSrc();
      if (url) {
        this.backgroundImageService.setBackgroundImageMainScreen(url);
        this.backgroundImageService.applyAmbientTint(url);
      }
    });
  }

  // Roon-style "signal path" chips: codec · sample rate · bit depth, built from
  // whatever the renderer/track actually reports (empty entries are dropped).
  qualityChips = computed<string[]>(() => {
    const ti = this.rendererService.trackInfo();
    const fmt = ti.currentTrack?.audioFormat;
    const chips: string[] = [];
    let codec: string | undefined = ti.codecName;
    if (!codec && fmt?.contentFormat) {
      codec = fmt.contentFormat.split('/').pop();
    }
    if (codec) {
      // "x-flac" / "vnd.wave" -> "FLAC" / "WAVE" (drop MIME experimental prefix).
      chips.push(codec.replace(/^(x-|vnd\.)/i, '').toUpperCase());
    }
    const sampleRate = ti.sampleRate || fmt?.sampleFrequency;
    if (sampleRate) {
      chips.push(this.formatSampleRate(sampleRate));
    }
    const bitDepth = ti.bitDepth || fmt?.bitsPerSample;
    if (bitDepth) {
      chips.push(bitDepth + ' bit');
    }
    return chips;
  });

  // The renderer's `lossless` flag is unreliable (often false for FLAC), so
  // fall back to the codec/container name — FLAC, ALAC, WAV, DSD, … are lossless.
  isLossless = computed<boolean>(() => {
    const ti = this.rendererService.trackInfo();
    if (ti.lossless) {
      return true;
    }
    const codec = (
      ti.codecName ||
      ti.currentTrack?.audioFormat?.contentFormat ||
      ''
    ).toLowerCase();
    return /flac|alac|wav|aif|pcm|dsd|dsf|dff|ape|wavpack|\bwv\b|tak|tta|lossless/.test(
      codec,
    );
  });

  ngOnInit(): void {
    this.layoutService.setFramedView();
  }

  // Hz -> "44.1 kHz" / "48 kHz" (no trailing ".0").
  private formatSampleRate(hz: number): string {
    const kHz = hz / 1000;
    const disp = Number.isInteger(kHz) ? String(kHz) : kHz.toFixed(1);
    return disp + ' kHz';
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
