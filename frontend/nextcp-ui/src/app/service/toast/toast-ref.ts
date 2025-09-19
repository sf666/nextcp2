import { OverlayRef } from '@angular/cdk/overlay';

export class ToastRef {
  constructor(private readonly overlay: OverlayRef) { }

  close() {
    this.overlay.dispose();
  }

  isVisible() {
    return this.overlay?.overlayElement?.clientWidth > 0 ;
  }

  getPosition() {
    return this.overlay.overlayElement.getBoundingClientRect()
  }
}