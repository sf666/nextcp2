import { ContentDirectoryService } from './content-directory.service';
import { Subject } from 'rxjs';
import { MusicBrainzId } from './dto.d';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RatingServiceService {

  private baseUri = '/RatingService';

  constructor(
    private httpService: HttpService,
    private contentDirectoryService: ContentDirectoryService) { }

  public setStarRating(musicBrainzID: number, stars: number): Subject<number> {
    const uri = `/setStarRating/${stars}/${this.contentDirectoryService.getCurrentMediaServerDto().udn}`;    
    return this.httpService.post<number>(this.baseUri, uri, musicBrainzID);
  }

  public syncRatingsFromMusicBrainzToBackend(): Subject<string> {
    const uri = "/syncRatingsFromMusicBrainzToBackend";
    return this.httpService.get(this.baseUri, uri);
  }
}
