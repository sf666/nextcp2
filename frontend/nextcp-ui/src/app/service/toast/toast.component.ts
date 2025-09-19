import { Component, OnInit, Inject, ChangeDetectionStrategy, signal, AnimationCallbackEvent } from '@angular/core';

import { ToastData, TOAST_CONFIG_TOKEN, ToastConfig } from './toast-config';
import { ToastRef } from './toast-ref';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  imports: [],
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['toast.component.scss'],
})
export class ToastComponent {
  private intervalId: NodeJS.Timeout;

  svg_success = "M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z";
  svg_warning = "M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 11.793a1 1 0 1 1-1.414 1.414L10 11.414l-2.293 2.293a1 1 0 0 1-1.414-1.414L8.586 10 6.293 7.707a1 1 0 0 1 1.414-1.414L10 8.586l2.293-2.293a1 1 0 0 1 1.414 1.414L11.414 10l2.293 2.293Z";
  svg_info = "M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z";

  isShown = signal(true);

  constructor(
    readonly data: ToastData,
    readonly ref: ToastRef,
    @Inject(TOAST_CONFIG_TOKEN) public toastConfig: ToastConfig
  ) {
    data.type === 'success' ? 'done' : data.type;
  }

  getColor(): string {
    console.log(this.data.type);
    switch (this.data.type) {
      case 'success': return 'inline-flex items-center justify-center shrink-0 w-8 h-8 rounded-lg bg-green-800 text-green-200';
      case 'warning': return 'inline-flex items-center justify-center shrink-0 w-8 h-8 rounded-lg bg-red-800 text-red-200';
      case 'info': return 'inline-flex items-center justify-center shrink-0 w-8 h-8 rounded-lg bg-blue-800 text-blue-200';
      default: return 'inline-flex items-center justify-center shrink-0 w-8 h-8 rounded-lg bg-blue-800 text-blue-200';
    }
  }


  getPathForIcon(): string {
    switch (this.data.type) {
      case 'success':
        return this.svg_success;
      case 'warning':
        return this.svg_warning;
      case 'info':
      default:
        return this.svg_info;
    }
  }

  ngOnInit() {
    this.intervalId = setTimeout(() => {
      this.isShown.set(false);
      clearTimeout(this.intervalId);
    }, 3000);
  }

  animateLeave(event: AnimationCallbackEvent) {
    event.animationComplete();
  }
}