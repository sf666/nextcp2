import { Inject, Injectable, signal } from '@angular/core';
import Stack from 'ts-data.stack';

const baseId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';

@Injectable()
export class CdsBrowsePathService {
  stack = new Stack<string>();

  scrollId = signal<string>(baseId);

  constructor() {
    console.log("[CdsBrowsePathService] constructor call");
  }

  public stepIn(objectId: string): void {
    this.scrollId.set(baseId);
    this.stack.push(objectId);
  }

  public stepOut(): void {
    if (this.stack.isEmpty) {
      this.scrollId.set(baseId);
    }
    var previous = this.stack.pop();
    if (previous?.length > 0) {
      this.scrollId.set(previous);
    } else {
      console.log("[ERROR] cannot step out");
      this.scrollId.set(baseId);
    }
  }

  public peekCurrentPathID(): string {
    return this.stack.peek();
  }

  public clear(): void {
    while(!this.stack.isEmpty) {
      this.stack.pop();
    }
  }

  get scrollToID(): string {
    return this.scrollId();
  }

  public persistPathToRoot(): void {
  }

  /**
   * @param elementID ATTENTION: elementID needs to have tabindex set to '-1': <div id="elementID" tabindex="-1">
   */
  public scrollIntoViewID(elementID?: string): void {
    console.error("do not use");
    return;

    if (!elementID) {
      elementID = this.scrollId();
    }
    console.log("scroll to ID : " + elementID);
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      this.scrollId.set(baseId);
      targetElement.focus();
    } else {
      console.log("scroll id not found : " + elementID);
    }
  }
}
