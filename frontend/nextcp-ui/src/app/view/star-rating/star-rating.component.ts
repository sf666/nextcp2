import { GenericResultService } from './../../service/generic-result.service';
import { DeviceService } from './../../service/device.service';
import { SseService } from './../../service/sse/sse.service';
import { RendererService } from './../../service/renderer.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { MusicItemDto } from './../../service/dto.d';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})
export class StarRatingComponent implements OnInit {

  @Input() currentSong: MusicItemDto;


  private lastUpdateId: number;
  starsAvail: number[];

  constructor(
    private genericResultService: GenericResultService,
    private ratingServiceService: RatingServiceService,
    private deviceService: DeviceService,
    private sseService: SseService) {

    this.starsAvail = Array(5).fill(1).map((x, i) => i + 1);
  }

  ngOnInit(): void {
  }

  isVisible() : boolean {
    return this.currentSong?.musicBrainzId?.TrackId?.length > 0;
  }

  starSelected(num: number) {
    if (this.currentSong.musicBrainzId.TrackId) {
      this.ratingServiceService.setStarRatingByMusicBrainzID(this.currentSong.musicBrainzId.TrackId, num);
    } else {
      this.genericResultService.displayErrorMessage("current track has no identifier.", "add star rating");
    }
  }

  getClass(num: number) {
    if (this.currentSong) {
      if (this.currentSong.rating && this.currentSong.rating >= num) {
        return "active";
      }
    }
    return "inactive";
  }

}
