import { DeviceService } from './device.service';
import { ContentDirectoryService } from './content-directory.service';
import { Subject } from 'rxjs';
import { MusicItemIdDto } from './dto.d';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RatingServiceService {

  private baseUri = '/RatingService';

  constructor(
    private httpService: HttpService,
    private deviceSerice: DeviceService) { }

  public setStarRating(ids: MusicItemIdDto, stars: number): Subject<number> {
    const uri = `/setStarRating/${stars}/${this.deviceSerice.selectedMediaServerDevice.udn}`;    
    return this.httpService.post<number>(this.baseUri, uri, ids);
  }

  public syncRatingsFromMusicBrainzToBackend(): Subject<string> {
    const uri = "/syncRatingsFromMusicBrainzToBackend";
    return this.httpService.post(this.baseUri, uri, this.deviceSerice.selectedMediaServerDevice.udn);
  }
}
