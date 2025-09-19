import { InjectionToken, TemplateRef } from '@angular/core';

export class ToastData {
  type: ToastType;
  header: string;
  text?: string;
  template?: TemplateRef<any>;
  templateContext?: {};
}

export type ToastType = 'warning' | 'info' | 'success';

export interface ToastConfig {
    position?: {
        top: number;
        right: number;
    };
}

export const defaultToastConfig: ToastConfig = {
    position: {
        top: 20,
        right: 20,
    },
};

export const TOAST_CONFIG_TOKEN = new InjectionToken('toastConfig');