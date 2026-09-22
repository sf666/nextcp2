import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
} from '@angular/core';

import { AlertComponent } from 'src/app/comp/alert/alert.component';
import { DeviceService } from 'src/app/service/device.service';
import {
  FEATURE_REQUIREMENT,
  ServerFeatureName,
} from 'src/app/service/server-feature';

/**
 * Says why a function is missing, where the user looks for it.
 *
 * Hiding what a media server cannot do is right, but on its own it is indistinguishable from a bug.
 * Shows nothing while the server offers the capability, and nothing while no server is selected -
 * then the empty state of the view has the better answer.
 */
@Component({
  selector: 'feature-hint',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [AlertComponent],
  templateUrl: './feature-hint.component.html',
  styleUrl: './feature-hint.component.scss',
})
export class FeatureHintComponent {
  private readonly deviceService = inject(DeviceService);

  readonly feature = input.required<ServerFeatureName>();
  /** `line` for a hint inside a list, `alert` for one that carries a page. */
  readonly variant = input<'line' | 'alert'>('line');

  readonly visible = computed(
    () =>
      this.deviceService.mediaServerSelected() &&
      !this.deviceService.hasFeature(this.feature()),
  );

  readonly text = computed(() => FEATURE_REQUIREMENT[this.feature()] ?? '');
}
