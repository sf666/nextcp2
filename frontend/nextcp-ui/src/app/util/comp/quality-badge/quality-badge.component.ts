import { MusicItemDto } from './../../../service/dto.d';
import { TrackQualityService } from './../../track-quality.service';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'quality-badge',
  templateUrl: './quality-badge.component.html',
  styleUrls: ['./quality-badge.component.scss']
})
export class QualityBadgeComponent implements OnInit {

  @Input() item: MusicItemDto;

  constructor(public trackQualityService: TrackQualityService) { }

  ngOnInit(): void {
  }

  get isHifi(): boolean {
    return this.trackQualityService.isHifi(this.item);
  }

  get hifiString(): string {
    return this.trackQualityService.getHifiString(this.item);
  }
}
