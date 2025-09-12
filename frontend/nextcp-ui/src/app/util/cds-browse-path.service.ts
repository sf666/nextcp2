import { Injectable, signal } from '@angular/core';
import { PersistenceService } from '../service/persistence/persistence.service';
import Stack from './stack';

const baseId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';

@Injectable()
export class CdsBrowsePathService {
  stack = new Stack<string>();

  scrollId = signal<string>(baseId);

  constructor(
    private persistenceService: PersistenceService
  ) {
    console.log("[CdsBrowsePathService] constructor call");
    var lastFocusId = this.persistenceService.getLastFocusId();
    if (lastFocusId) {
      this.scrollId.set(lastFocusId);
    }
  }

  setScrollId(id: string) : void {
    this.persistenceService.setLastFocusID(id);
    this.scrollId.set(id);
  }

  public stepIn(objectId: string): void {
    this.setScrollId(baseId);
    this.stack.push(objectId);
  }

  public stepOut(): void {
    if (this.stack.isEmpty) {
      this.setScrollId(baseId);
    }
    var previous = this.stack.pop();
    if (previous?.length > 0) {
      this.setScrollId(previous);
    } else {
      console.log("[ERROR] cannot step out");
      this.setScrollId(baseId);
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
    if (!elementID) {
      elementID = this.scrollId();
    }
    console.log("[scroll] to ID : " + elementID);
    const targetElement = document.getElementById(elementID); // querySelector('#someElementId');
    if (targetElement) {
      targetElement.focus();
    } else {
      console.log("[scroll] id not found : " + elementID);
    }
  }
}
