import { GenericResultService } from './../../service/generic-result.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { MusicItemDto } from './../../service/dto.d';
import { Component, Input, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'star-rating',
    encapsulation: ViewEncapsulation.None,
    templateUrl: './star-rating.component.html',
    styleUrls: ['./star-rating.component.scss'],
    standalone: true
})

export class StarRatingComponent {

  @Input() currentSong: MusicItemDto;
  @Input() size = "sm";

  private lastUpdateId: number;
  starsAvail: number[];



  constructor(
    private genericResultService: GenericResultService,
    private ratingServiceService: RatingServiceService) {

    this.starsAvail = Array(5).fill(1).map((x, i) => i + 1);
  }

  isVisible(): boolean {
    if (this.currentSong?.objectID != null) {
      return true;
    }
    return false;
  }

  starSelected(num: number): void {
    this.ratingServiceService.setStarRating(this.currentSong.songId, this.currentSong.rating, num).subscribe({
      next: () => this.currentSong.rating = num,
      error: () => console.log("error"),
    })
  }

  isActive(num: number): string  {
    if (this.currentSong) {
      if (this.currentSong.rating && this.currentSong.rating >= num) {
        return "active";
      }
    }
    return "inactive";
  }

  getIconFor(num: number): string  {
    if (this.currentSong) {
      if (this.currentSong.rating && this.currentSong.rating >= num) {
        return "star";
      }
    }
    return "grade";
  }
}
