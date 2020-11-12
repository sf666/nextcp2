import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ScrollViewService {

  constructor() { }

  /**
   *  
   * @param elementID ATTENTION: elementID needs to have tabindex set: <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID: string): void {
    return;

    // buttons don't work any more ...
    let targetElement = document.getElementById(elementID); // querySelector('#someElementId');

    if (targetElement) {
      targetElement.focus();
    }
  }
}
