import { MusicItemDto } from './../../../service/dto.d';
import { TrackQualityService } from './../../track-quality.service';
import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  input,
  inject,
} from '@angular/core';

@Component({
  selector: 'quality-badge',
  templateUrl: './quality-badge.component.html',
  styleUrls: ['./quality-badge.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class QualityBadgeComponent implements OnInit {
  trackQualityService = inject(TrackQualityService);

  readonly item = input.required<MusicItemDto>();

  ngOnInit(): void {}

  get isHifi(): boolean {
    return this.trackQualityService.isHifi(this.item());
  }

  get hifiString(): string {
    return this.trackQualityService.getHifiString(this.item()) ?? '';
  }
}
