import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ContainerDto } from 'src/app/service/dto.d';
import {
  RATING_DISLIKED,
  RATING_LIKED,
  RatingServiceService,
} from 'src/app/service/rating-service.service';

/**
 * Rates a container without navigating into it. Opened by a long press on a
 * container tile, which is the only spare gesture there : a tap navigates.
 */
@Component({
  selector: 'container-rating',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './container-rating.component.html',
  styleUrl: './container-rating.component.scss',
})
export class ContainerRatingComponent {
  private ratingService = inject(RatingServiceService);
  private dialogRef = inject(MatDialogRef<ContainerRatingComponent>);
  private data: { container: ContainerDto } = inject(MAT_DIALOG_DATA);

  readonly container = this.data.container;
  rating = signal<number | undefined>(this.data.container.rating ?? undefined);

  isLiked(): boolean {
    return this.rating() === RATING_LIKED;
  }

  isDisliked(): boolean {
    return this.rating() === RATING_DISLIKED;
  }

  like(): void {
    this.apply(this.isLiked() ? undefined : RATING_LIKED);
  }

  dislike(): void {
    this.apply(this.isDisliked() ? undefined : RATING_DISLIKED);
  }

  clear(): void {
    this.apply(undefined);
  }

  private apply(newRating: number | undefined): void {
    const previousRating = this.rating();
    this.ratingService
      .setResourceRating(this.container.id, previousRating, newRating)
      .subscribe({
        next: () => {
          this.rating.set(newRating);
          // Hand the new value back so the tile can update its overlay without
          // re-browsing the whole container.
          this.dialogRef.close(newRating ?? null);
        },
        error: (err) => console.log('cannot rate container : ' + err),
      });
  }
}
