import { UuidService } from './../../util/uuid.service';
import { GenericResultService } from './../../service/generic-result.service';
import { DeviceService } from './../../service/device.service';
import { SseService } from './../../service/sse/sse.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { MusicItemDto } from './../../service/dto.d';
import { Component, Input, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'star-rating',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})

export class StarRatingComponent {

  @Input() currentSong: MusicItemDto;
  @Input() size = "sm";

  private lastUpdateId: number;
  starsAvail: number[];



  constructor(
    private genericResultService: GenericResultService,
    private ratingServiceService: RatingServiceService,
    private uuidService: UuidService,
    private deviceService: DeviceService,
    private sseService: SseService) {

    this.starsAvail = Array(5).fill(1).map((x, i) => i + 1);
  }

  isVisible(): boolean {
    if (this.currentSong?.musicBrainzId?.TrackId?.length > 0) {
      return this.uuidService.isValidUuid(this.currentSong.musicBrainzId?.TrackId);
    }
    return false;
  }

  starSelected(num: number): void {
    this.currentSong.rating = num;
    if (this.currentSong.musicBrainzId.TrackId) {
      this.ratingServiceService.setStarRatingByMusicBrainzID(this.currentSong.musicBrainzId.TrackId, num);
    } else {
      this.genericResultService.displayErrorMessage("current track has no identifier.", "add star rating");
    }
  }

  getBtnSizeClass(): string  {
    if (this.size === 'sm') {
      return "mat-icon-button-sm";
    }
  }

  getIconSizeClass(): string  {
    if (this.size === 'sm') {
      return "mat-icon-sm";
    }
  }

  getClass(num: number): string  {
    if (this.currentSong) {
      if (this.currentSong.rating && this.currentSong.rating >= num) {
        return "active";
      }
    }
    return "inactive";
  }
}
