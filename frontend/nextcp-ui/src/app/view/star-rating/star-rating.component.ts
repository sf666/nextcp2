import { RatingServiceService } from './../../service/rating-service.service';
import { MusicItemDto } from './../../service/dto.d';
import { ChangeDetectionStrategy, Component, Input, OnInit, ViewEncapsulation, computed, input, model, signal } from '@angular/core';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';

@Component({
  selector: 'star-rating',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss'],
  standalone: true
})

export class StarRatingComponent implements OnInit{

  currentSong = input<MusicItemDto>(this.dtoGeneratorService.emptyMusicItemDto());
  isVisible = computed(() => this.currentSong().objectID?.length > 0 != null);
  rating = signal<number>(this.currentSong().rating);
  size = input<string>("sm");

  starsAvail = model<number[]>(Array(5).fill(1).map((x, i) => i + 1));

  constructor(
    private dtoGeneratorService: DtoGeneratorService,
    private ratingServiceService: RatingServiceService) {
  }

  ngOnInit(): void {
    this.rating.set(this.currentSong().rating);
  }

  starSelected(num: number): void {
    this.ratingServiceService.setStarRating(this.currentSong().songId, this.rating(), num).subscribe({
      next: () => this.rating.set(num),
      error: (err) => console.log("error star selected : " + err),
    })
  }

  isActive(num: number): string {
    if (this.rating() >= num) {
      return "active";
    } else {
      return "inactive";
    }
  }

  getIconFor(num: number): string {
    if (this.rating() >= num) {
      return "star";
    }
    return "grade";
  }
}
