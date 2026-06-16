import { DeviceService } from './device.service';
import { Subject } from 'rxjs';
import { MusicItemIdDto, UpdateStarRatingRequest } from './dto.d';
import { HttpService } from './http.service';
import { Injectable, inject } from '@angular/core';

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

  public syncRatingsFromMusicBrainzToBackend(): Subject<string> {
    const uri = '/syncRatingsFromMusicBrainzToBackend';
    return this.httpService.post(
      this.baseUri,
      uri,
      this.deviceSerice.selectedMediaServerDevice().udn,
    );
  }
}
