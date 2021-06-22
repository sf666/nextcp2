/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */

import { MatDialogRef } from '@angular/material/dialog';
import { MatDialogConfig } from '@angular/material/dialog';
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
      topPos = window.innerHeight - popupHeight - 2;  // MatDialogConfig width
    }

    _matDialogRef.updateSize(`${popupWidth}px`, `${popupHeight}px`);
    _matDialogRef.updatePosition({ left: `${leftPos}px`, top: `${topPos}px` });
    _matDialogRef.addPanelClass('popup');
  }
}
