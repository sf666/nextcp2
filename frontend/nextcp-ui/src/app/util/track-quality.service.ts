import { MusicItemDto } from './../service/dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TrackQualityService {

  constructor() { }

  public isHifi(song: MusicItemDto) : boolean {
    let bps = this.getBitsPerSample(song);
    let sFreq = this.getSampleFreq(song);
    if (bps >= 16 && sFreq>= 44100) { // CD Quality
      return true;
    }
    return false;
  }

  public getHifiString(song: MusicItemDto) : string {
    let bps = this.getBitsPerSample(song);
    let sFreq = this.getSampleFreq(song);
    if (!this.isHifi) {
      return "low";
    } else if (bps == 16 && sFreq == 44100) {
      return "CD"
    } else if (bps > 16 && sFreq == 44100) {
      return "HIFI"
    } else if (bps >= 16 && sFreq > 44100) {
      return "Hi-Res"
    }
  }

  public getBitrate(song: MusicItemDto): number {
    if (song?.audioFormat?.bitrate) {
      return song?.audioFormat?.bitrate / 125
    } else {
      return 0;
    }
  }

  public getBitsPerSample(song: MusicItemDto): number {
    if (song?.audioFormat?.bitsPerSample) {
      return song?.audioFormat?.bitsPerSample
    } else {
      return 0;
    }
  }

  public getSampleFreq(song: MusicItemDto): number {
    if (song?.audioFormat?.sampleFrequency) {
      return song?.audioFormat?.sampleFrequency
    } else {
      return 0;
    }
  }
}
