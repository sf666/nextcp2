import { DeviceService } from './device.service';
import { Subject } from 'rxjs';
import { MusicItemIdDto, UpdateStarRatingRequest } from './dto.d';
import { HttpService } from './http.service';
import { Injectable, inject } from '@angular/core';

/**
 * A like is stored as 5 stars, a dislike as 0. Removing a rating clears it, which
 * is a third state and must stay distinguishable from a dislike.
 */
export const RATING_LIKED = 5;
export const RATING_DISLIKED = 0;

@Injectable({
  providedIn: 'root',
})
export class RatingServiceService {
  private httpService = inject(HttpService);
  private deviceSerice = inject(DeviceService);

  private baseUri = '/RatingService';

  public setStarRating(
    ids: MusicItemIdDto,
    previousStars: number,
    stars: number,
  ): Subject<void> {
    const uri = `/setStarRating`;

    const srr: UpdateStarRatingRequest = {
      previousRating: previousStars,
      newRating: stars,
      musicItemIdDto: ids,
      mediaServerDevice: this.deviceSerice.selectedMediaServerDevice().udn,
    };

    return this.httpService.post<void>(this.baseUri, uri, srr);
  }

  /**
   * Rates any resource that is not a song, for example a folder, a playlist or an
   * album container. The media server only needs the objectID.
   *
   * A rating of 5 is a like, 0 is a dislike and undefined removes the rating.
   */
  public setResourceRating(
    objectID: string,
    previousRating: number | undefined,
    newRating: number | undefined,
  ): Subject<void> {
    const uri = `/setStarRating`;

    // The generated DTO types the ratings as non-optional numbers, but the media
    // server needs a null NewTagValue to remove a rating. Cast once here instead
    // of widening the generated type.
    const srr = {
      previousRating: previousRating ?? null,
      newRating: newRating ?? null,
      musicItemIdDto: {
        objectID: objectID,
        acoustID: '',
        musicBrainzIdTrackId: '',
      },
      mediaServerDevice: this.deviceSerice.selectedMediaServerDevice().udn,
    } as unknown as UpdateStarRatingRequest;

    return this.httpService.post<void>(this.baseUri, uri, srr);
  }

  public syncRatingsFromMusicBrainzToBackend(): Subject<string> {
    const uri = '/syncRatingsFromMusicBrainzToBackend';
    return this.httpService.post(
      this.baseUri,
      uri,
      this.deviceSerice.selectedMediaServerDevice().udn,
    );
  }
}
