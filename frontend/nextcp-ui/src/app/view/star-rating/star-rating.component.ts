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


  private lastUpdateId : number;
  starsAvail: number[];

  constructor(
    private ratingServiceService: RatingServiceService,
    private deviceService: DeviceService,
    private sseService: SseService) {

    this.starsAvail = Array(5).fill(1).map((x, i) => i + 1);
  }

  ngOnInit(): void {
  }

  starSelected(num: number) {
    this.ratingServiceService.setStarRatingByMusicBrainzID(this.currentSong.musicBrainzId.TrackId, num);
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
