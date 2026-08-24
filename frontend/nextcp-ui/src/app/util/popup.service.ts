/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { MatDialogRef } from '@angular/material/dialog';
import { Injectable, ElementRef, Injector, afterNextRender, inject } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  private injector = inject(Injector);

  /**
   * Distance a popup keeps from the edges of the window.
   */
  private static readonly VIEWPORT_MARGIN = 8;

  /**
   * Places a menu popup next to its trigger and lets the menu's own content
   * decide how tall it is.
   *
   * Every popup used to pass a pixel height instead. For a menu whose rows come
   * and go that number is guesswork, and it goes stale as soon as a row's padding
   * changes — the popup is then cut off at the bottom. Measuring the rendered
   * panel cannot go stale. The height is capped at the window, so an over-long
   * popup scrolls inside itself instead of hanging off the screen.
   *
   * @param _matDialogRef dialog to size and position
   * @param triggerElementRef element the popup belongs to
   * @param popupWidth popup width; the component's own panel should match it
   */
  public configurePopupAtTrigger(_matDialogRef: MatDialogRef<any>, triggerElementRef: ElementRef<any>, popupWidth: number): void {
    _matDialogRef.updateSize(`${popupWidth}px`, '');
    _matDialogRef.addPanelClass('popup');

    const pane = this.findOverlayPane(_matDialogRef);
    if (!pane) {
      return;
    }
    const maxHeight = window.innerHeight - 2 * PopupService.VIEWPORT_MARGIN;
    pane.style.maxHeight = `${maxHeight}px`;

    const place = (): void => {
      this.positionNextToTrigger(_matDialogRef, triggerElementRef, popupWidth, Math.min(pane.offsetHeight, maxHeight));
    };

    // Nothing to measure yet: callers run this from their constructor or their
    // ngOnInit, and the popup's rows do not exist until the render after that.
    // Placing it now would use an empty panel's height, and a popup triggered
    // from the footer would end up hanging off the bottom of the window. So it
    // stays invisible for that one frame rather than being placed twice, which
    // would show as a jump from the middle of the screen.
    pane.style.visibility = 'hidden';
    afterNextRender(() => {
      place();
      pane.style.visibility = '';
      // Rows can still come and go while the popup is open, so keep following it.
      const observer = new ResizeObserver(() => place());
      observer.observe(pane);
      _matDialogRef.afterClosed().subscribe(() => observer.disconnect());
    }, { injector: this.injector });
  }

  /**
   * The overlay pane the dialog lives in - the element that carries the size and
   * position we set, and the one to measure.
   *
   * Taken from the CDK ref behind the dialog, which knows its own overlay from
   * the moment the dialog is created. The lookup by the container's id is the
   * fallback: that id is a host binding, so it is only on the element once the
   * container has been change-detected, which is too late for a caller that runs
   * in its constructor.
   */
  private findOverlayPane(_matDialogRef: MatDialogRef<any>): HTMLElement | null {
    const internals = _matDialogRef as unknown as {
      _ref?: { overlayRef?: { overlayElement?: HTMLElement } };
    };
    const overlayElement = internals._ref?.overlayRef?.overlayElement;
    if (overlayElement) {
      return overlayElement;
    }
    return document.getElementById(_matDialogRef.id)?.closest<HTMLElement>('.cdk-overlay-pane') ?? null;
  }

  /**
   * Puts a popup of the given size beside the trigger, preferring its right side,
   * and keeps it fully inside the window on both axes.
   */
  private positionNextToTrigger(_matDialogRef: MatDialogRef<any>, triggerElementRef: ElementRef<any>, popupWidth: number, popupHeight: number): void {
    const rect: DOMRect = triggerElementRef.nativeElement.getBoundingClientRect();
    const margin = PopupService.VIEWPORT_MARGIN;

    let leftPos = window.innerWidth - rect.right - popupWidth - 2 > 0 ? rect.right + 2 : rect.left - popupWidth - 2;
    leftPos = Math.max(margin, Math.min(leftPos, window.innerWidth - popupWidth - margin));

    // Slightly overlapping the trigger's bottom edge is what ties the menu to the
    // button it came out of; the clamp wins whenever the menu would not fit.
    let topPos = rect.bottom - 20;
    topPos = Math.max(margin, Math.min(topPos, window.innerHeight - popupHeight - margin));

    _matDialogRef.updatePosition({ left: `${leftPos}px`, top: `${topPos}px` });
  }

  /**
   * Places a popup centred over its trigger and growing upwards — for controls
   * that belong to the button they pop out of (volume column, small pickers).
   * The popup's bottom edge ends level with the trigger's bottom edge, so it
   * reads as an extension of the button rather than as a detached panel. Falls
   * back to below the trigger when there is no room above, and is kept inside
   * the viewport on both axes.
   *
   * @param _matDialogRef dialog to position
   * @param triggerElementRef element triggering the popup event
   * @param popupWidth popup width
   * @param popupHeight popup height
   * @param bottomOffset lifts the popup off the trigger's bottom edge
   */
  public configurePopupAboveTrigger(_matDialogRef: MatDialogRef<any>, triggerElementRef: ElementRef<any>, popupWidth: number, popupHeight: number, bottomOffset = 0): void {
    const rect: DOMRect = triggerElementRef.nativeElement.getBoundingClientRect();
    const margin = 8;

    const centred = rect.left + rect.width / 2 - popupWidth / 2;
    const leftPos = Math.min(Math.max(centred, margin), window.innerWidth - popupWidth - margin);

    let topPos = rect.bottom - bottomOffset - popupHeight;
    if (topPos < margin) {
      // Taller than the space above: hang it below the trigger instead.
      topPos = Math.min(rect.top + bottomOffset, window.innerHeight - popupHeight - margin);
    }

    _matDialogRef.updateSize(`${popupWidth}px`, `${popupHeight}px`);
    _matDialogRef.updatePosition({ left: `${leftPos}px`, top: `${topPos}px` });
    _matDialogRef.addPanelClass('popup');
  }
}
