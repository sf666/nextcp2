import { ChangeDetectionStrategy, Component, input, signal } from '@angular/core';

@Component({
  selector: 'my-alert',
  imports: [],
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AlertComponent {
  prefix = input('info');
  screenReader = input<string>('info');
  text = input<string>('');
  svgPath = input<string>('M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z');
}
