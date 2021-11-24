import { Subject } from 'rxjs';
import { MusicBrainzId } from './dto.d';
import { HttpService } from './http.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RatingServiceService {

  private baseUri = '/RatingService';

  constructor(private httpService: HttpService) { }

  /*
    public getStarRatingByMusicBrainzID(musicBrainzID: string): Subject<number> {
      const uri = "/getStarRatingByMusicBrainzID";
  
      return this.httpService.post<number>(this.baseUri, uri, musicBrainzID);
    }
  */
  public setStarRatingByMusicBrainzID(musicBrainzID: string, stars: number): Subject<number> {
    const uri = `/setStarRatingByMusicBrainzID/${stars}`;

    return this.httpService.post<number>(this.baseUri, uri, musicBrainzID);
  }

  public syncRatingFromAudioFile(): Subject<string> {
    const uri = "/syncRatingFromAudioFile";
    return this.httpService.get(this.baseUri, uri);
  }

  public syncRatingFromMusicBrainz(): Subject<string> {
    const uri = "/syncRatingsFromMusicBrainz";
    return this.httpService.get(this.baseUri, uri);
  }

  public syncRatingsFromMusicBrainzToFiles(): Subject<string> {
    const uri = "/syncRatingsFromMusicBrainzToFiles";
    return this.httpService.get(this.baseUri, uri);
  }

  public indexerRescanMusicDirectory(): Subject<string> {
    const uri = "/indexerRescanMusicDirectory";
    return this.httpService.get(this.baseUri, uri);
  }
}
