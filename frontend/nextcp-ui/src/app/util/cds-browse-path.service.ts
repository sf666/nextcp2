import { Inject, Injectable } from '@angular/core';
import Stack from 'ts-data.stack';

@Injectable({
  providedIn: 'root'
})

export class CdsBrowsePathService {

  private scrollId: string;

  constructor(@Inject('uniqueId') private uniqueId_: string) {
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
  }

  public stepIn(path: string): void {
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
  }

  private addPathToString(path: string) {
  }

  public stepOut(): void {
  }

  public peekCurrentPathID(): string {
    return "DEPRECATED";
  }

  public clearPath(): void {
  }

  get scrollToID(): string {
    return this.scrollId;
  }

  public persistPathToRoot(): void {
  }

  public restorePathToRoot(): void {
  }

  /**
   * @param elementID ATTENTION: elementID needs to have tabindex set to '-1': <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID: string): void {
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      this.scrollId = '';
      targetElement.focus();
    }
  }
}
