import { MusicItemDto } from './../../service/dto.d';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})
export class StarRatingComponent implements OnInit {

  @Input() currentSong: MusicItemDto;


  starsAvail: number[];

  constructor() {
    this.starsAvail = Array(5).fill(1).map((x, i) => i + 1);

  }

  ngOnInit(): void {
  }

  starSelected(num: number) {
    if (this.currentSong)
    {
      this.currentSong.rating = num;
    }
  }

  getClass(num: number) {
    if (this.currentSong) {
      if (this.currentSong.rating && this.currentSong.rating >= num) {
        return "active";
      }
    }
    return "inactive";
  }

}
