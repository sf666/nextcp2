import { RatingServiceService } from './../../service/rating-service.service';
import { ItemDto } from './../../service/dto.d';
import { ChangeDetectionStrategy, Component, ViewEncapsulation, computed, effect, input, model, signal, inject } from '@angular/core';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';

@Component({
  selector: 'star-rating',
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss'],
  standalone: true,
})
export class StarRatingComponent {
  private dtoGeneratorService = inject(DtoGeneratorService);
  private ratingServiceService = inject(RatingServiceService);

  currentSong = input<ItemDto>(
    this.dtoGeneratorService.emptyItemDto(),
  );
  isVisible = computed(() => !!(this.currentSong().objectID || this.currentSong().songId?.objectID));
  rating = signal<number>(this.currentSong().rating);
  size = input<string>('sm');

  starsAvail = model<number[]>(
    Array(5)
      .fill(1)
      .map((x, i) => i + 1),
  );

  constructor() {
    effect(() => this.rating.set(this.currentSong().rating));
  }

  starSelected(num: number): void {
    const item = this.currentSong();
    const request = item.songId?.objectID
      ? this.ratingServiceService.setStarRating(item.songId, this.rating(), num, item.parentId)
      : this.ratingServiceService.setResourceRating(item.objectID, this.rating(), num, item.parentId);
    request.subscribe({
      next: () => this.rating.set(num),
      error: (err) => console.log('error star selected : ' + err),
    });
  }

  isActive(num: number): string {
    if (this.rating() >= num) {
      return 'active';
    } else {
      return 'inactive';
    }
  }

  getIconFor(num: number): string {
    if (this.rating() >= num) {
      return 'star';
    }
    return 'grade';
  }
}
