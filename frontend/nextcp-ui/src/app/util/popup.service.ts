/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { MatDialogRef } from '@angular/material/dialog';
import { Injectable, ElementRef } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PopupService {

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

    // Material puts the dialog ref's id on the container element, and the pane
    // around it is what carries the size we set above.
    const container = document.getElementById(_matDialogRef.id);
    const pane = container?.closest<HTMLElement>('.cdk-overlay-pane') ?? null;

    const maxHeight = window.innerHeight - 2 * PopupService.VIEWPORT_MARGIN;
    if (pane) {
      pane.style.maxHeight = `${maxHeight}px`;
    }

    const place = (): void => {
      const height = Math.min(pane?.offsetHeight ?? 0, maxHeight);
      this.positionNextToTrigger(_matDialogRef, triggerElementRef, popupWidth, height);
    };

    // The menu's rows render after this runs, so the first placement is a guess
    // that the observer below corrects as soon as there is something to measure
    // - and again whenever the menu changes size while it is open.
    place();
    if (!pane) {
      return;
    }
    const observer = new ResizeObserver(() => place());
    observer.observe(pane);
    _matDialogRef.afterClosed().subscribe(() => observer.disconnect());
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
