import { ChangeDetectionStrategy, Component, input } from '@angular/core';

@Component({
  selector: 'live-badge',
  templateUrl: './live-badge.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class LiveBadgeComponent {
  readonly playing = input<boolean>(false);
}
