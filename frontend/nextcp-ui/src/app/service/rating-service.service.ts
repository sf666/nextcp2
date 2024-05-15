import { DeviceService } from './device.service';
import { ContentDirectoryService } from './content-directory.service';
import { Subject } from 'rxjs';
import { MusicItemIdDto, UpdateStarRatingRequest } from './dto.d';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';
import { error } from 'console';

@Injectable({
  providedIn: 'root'
})
export class RatingServiceService {

  private baseUri = '/RatingService';

  constructor(
    private httpService: HttpService,
    private deviceSerice: DeviceService) { }

  public setStarRating(ids: MusicItemIdDto, previousStars: number, stars: number) {
    const uri = `/setStarRating`;
    
    const srr: UpdateStarRatingRequest = {      
      previousRating : previousStars,
      newRating : stars,
      musicItemIdDto : ids,
      mediaServerDevice : this.deviceSerice.selectedMediaServerDevice.udn,
    }
    
    return this.httpService.post<void>(this.baseUri, uri, srr);
  }

  public syncRatingsFromMusicBrainzToBackend(): Subject<string> {
    const uri = "/syncRatingsFromMusicBrainzToBackend";
    return this.httpService.post(this.baseUri, uri, this.deviceSerice.selectedMediaServerDevice.udn);
  }
}
