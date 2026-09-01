import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { take, timeout } from 'rxjs';
import { ContainerDto } from 'src/app/service/dto.d';
import {
  RATING_DISLIKED,
  RATING_LIKED,
  RatingServiceService,
} from 'src/app/service/rating-service.service';

/**
 * How long to wait for the media server before giving up. A rating is a tiny
 * round trip; if it has not answered by now something is wrong, and leaving the
 * sheet frozen tells the user nothing.
 */
const RATING_TIMEOUT_MS = 12000;

/**
 * Rates a container without navigating into it. Opened by a long press on a
 * container tile or by its options button — a tap navigates, so neither can
 * carry the action.
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
  private data: { container: ContainerDto; rating?: number } =
    inject(MAT_DIALOG_DATA);

  readonly container = this.data.container;

  /**
   * The caller passes what it currently believes the rating to be, which may be
   * newer than the browse DTO if the user already rated this container in this
   * view. Getting it wrong makes the media server reject the update.
   */
  rating = signal<number | undefined>(
    this.data.rating !== undefined
      ? this.data.rating
      : (this.data.container.rating ?? undefined),
  );

  /** A request is in flight; the buttons are inert until it settles. */
  busy = signal(false);
  error = signal<string | null>(null);

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
    if (this.busy()) {
      return;
    }
    this.busy.set(true);
    this.error.set(null);

    const previousRating = this.rating();
    this.ratingService
      .setResourceRating(
        this.container.id,
        previousRating,
        newRating,
        this.container.parentID,
        this.container.objectClass,
      )
      .pipe(take(1), timeout(RATING_TIMEOUT_MS))
      .subscribe({
        next: () => {
          this.rating.set(newRating);
          // Hand the new value back so the tile can update its overlay without
          // re-browsing the whole container.
          this.dialogRef.close(newRating ?? null);
        },
        error: (err) => {
          // Stay open and say so. Closing would look like it worked, and doing
          // nothing at all is what made this sheet appear to hang.
          console.error('cannot rate container', err);
          this.busy.set(false);
          this.error.set(
            err?.name === 'TimeoutError'
              ? 'The media server did not answer. The rating was not saved.'
              : 'Could not save the rating.',
          );
        },
      });
  }
}
