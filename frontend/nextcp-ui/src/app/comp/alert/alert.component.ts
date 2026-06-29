import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

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
  // Color variant: 'info' (blue, default), 'warning' (yellow) or 'error' (red).
  color = input<'info' | 'warning' | 'error'>('info');
  // Optional icon override; when empty a per-variant default icon is used.
  svgPath = input<string>('');

  // Layout classes shared by every variant.
  private static readonly BASE_CLASS = 'flex max-w-2xl items-center p-4 mb-4 text-sm rounded-lg dark:bg-gray-800';

  // Alert color palette per variant. Full class tokens so Tailwind keeps them.
  private static readonly COLOR_CLASS: Record<string, string> = {
    info: 'text-blue-800 bg-blue-50 dark:text-blue-400',
    warning: 'text-yellow-800 bg-yellow-50 dark:text-yellow-300',
    error: 'text-red-800 bg-red-50 dark:text-red-400',
  };

  // Default icons per variant. INFO_ICON uses a 0 0 20 20 viewBox, ERROR_ICON a 0 0 16 16 one.
  private static readonly INFO_ICON = 'M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z';
  // Filled "x in a circle" (Bootstrap Icons x-circle-fill).
  private static readonly ERROR_ICON = 'M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z';

  readonly alertClass = computed(
    () => `${AlertComponent.BASE_CLASS} ${AlertComponent.COLOR_CLASS[this.color()] ?? AlertComponent.COLOR_CLASS['info']}`
  );

  readonly iconPath = computed(() => {
    if (this.svgPath()) {
      return this.svgPath();
    }
    return this.color() === 'error' ? AlertComponent.ERROR_ICON : AlertComponent.INFO_ICON;
  });

  readonly iconViewBox = computed(
    () => (!this.svgPath() && this.color() === 'error') ? '0 0 16 16' : '0 0 20 20'
  );
}
