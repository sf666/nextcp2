import { ToastMessage } from './toast.service.d';
import { Injectable, TemplateRef } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts: ToastMessage[] = [];

  show(textOrTpl: ToastMessage) {
    this.toasts.push(textOrTpl);
  }

  remove(toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }

  info(message, header : string) {
    let m = this.generateDefaultToastMessage(message, header, 'bg-info');
    this.show(m);
  }
  
  error(message, header : string) {
    let m = this.generateDefaultToastMessage(message, header, 'bg-danger');
    this.show(m);
  }
  
  success(message, header : string) {
    let m = this.generateDefaultToastMessage(message, header, 'bg-success');
    this.show(m);
  }

  public generateDefaultToastMessage(message, header, classname : string): ToastMessage {
    return {
      classname: classname,
      autohide: 'true',
      delay: 5000,
      header: header,
      textToDisplay: message
    }
  }
}