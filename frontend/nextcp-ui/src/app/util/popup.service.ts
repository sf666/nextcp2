/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { MatDialogRef } from '@angular/material/dialog';
import { Injectable, ElementRef } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PopupService {

  /**
   * 
   * @param _matDialogRef calculates a visible popup position
   * @param triggerElementRef element triggering popup event
   * @param popupWidth popup width
   * @param popupHeight popup hight
   */
  public configurePopupPosition(_matDialogRef: MatDialogRef<any>, triggerElementRef: ElementRef<any>, popupWidth: number, popupHeight: number): void {
    let rect: DOMRect;
    rect = triggerElementRef.nativeElement.getBoundingClientRect();

    let leftPos, topPos: number;

    if (window.innerWidth - rect.right - popupWidth - 2 > 0) {
      leftPos = rect.right + 2;
    } else {
      leftPos = rect.left - popupWidth - 2;  // MatDialogConfig width
    }

    if (window.innerHeight - rect.bottom - popupHeight - 20 > 0) {
      topPos = rect.bottom - 20;
    } else {
      topPos = window.innerHeight - popupHeight - 2;  // MatDialogConfig height
    }

    _matDialogRef.updateSize(`${popupWidth}px`, `${popupHeight}px`);
    _matDialogRef.updatePosition({ left: `${leftPos}px`, top: `${topPos}px` });
    _matDialogRef.addPanelClass('popup');
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
