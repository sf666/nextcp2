import { Inject, Injectable, Injector } from '@angular/core';
import { TOAST_CONFIG_TOKEN, ToastConfig, ToastData } from './toast-config';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ToastRef } from './toast-ref';
import { ComponentPortal } from "@angular/cdk/portal";
import { ToastComponent } from './toast.component';


@Injectable({ providedIn: 'any' })
export class ToastService {

  private lastToast: ToastRef;

  constructor(
    private overlay: Overlay,
    private parentInjector: Injector,
    @Inject(TOAST_CONFIG_TOKEN) private toastConfig: ToastConfig
  ) { }

  info(message, header: string) {
    this.show({
      text: message,
      header: header,
      type: 'info',
    });
  }

  error(message, header: string) {
    this.show({
      text: message,
      header: header,
      type: 'warning',
    });
  }

  success(message, header: string) {
    this.show({
      text: message,
      header: header,
      type: 'success',
    });
  }

  show(data: ToastData) {
    const positionStrategy = this.getPositionStrategy();
    const overlayRef = this.overlay.create({ positionStrategy });

    const toastRef = new ToastRef(overlayRef);
    this.lastToast = toastRef;

    const injector = this.getInjector(data, toastRef, this.parentInjector);
    const toastPortal = new ComponentPortal(ToastComponent, null, injector);

    overlayRef.attach(toastPortal);

    return toastRef;
  }

  getPositionStrategy() {
    return this.overlay.position()
      .global()
      .top(this.getPosition())
      .right(this.toastConfig.position.right + 'px');
  }

  getPosition() {
    const lastToastIsVisible = this.lastToast && this.lastToast.isVisible();
    const position = lastToastIsVisible
      ? this.lastToast.getPosition().bottom + 8
      : this.toastConfig.position.top;

    return position + 'px';
  }

  getInjector(data: ToastData, toastRef: ToastRef, parentInjector: Injector) {
    return Injector.create({
      parent: parentInjector,
      providers: [
        { provide: ToastData, useValue: data },
        { provide: ToastRef, useValue: toastRef },
        { provide: TOAST_CONFIG_TOKEN, useValue: this.toastConfig },
      ],
    });
  }
}