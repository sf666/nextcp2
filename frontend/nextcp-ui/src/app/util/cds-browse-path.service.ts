import { Injectable, signal, inject } from '@angular/core';
import { PersistenceService } from '../service/persistence/persistence.service';
import Stack from './stack';

const baseId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';

@Injectable()
export class CdsBrowsePathService {
  private persistenceService = inject(PersistenceService);

  stack = new Stack<string>();

  scrollId = signal<string>(baseId);

  constructor() {
    console.log('[CdsBrowsePathService] constructor call');
    var lastFocusId = this.persistenceService.getLastFocusId();
    if (lastFocusId) {
      this.scrollId.set(lastFocusId);
    }
  }

  setScrollId(id: string): void {
    this.persistenceService.setLastFocusID(id);
    this.scrollId.set(id);
  }

  public stepIn(objectId: string): void {
    this.setScrollId(baseId);
    this.stack.push(objectId);
  }

  /**
   * Leaves the current container and scrolls back to the entry we came from.
   *
   * An empty stack is a normal state, not an error: jumping home, following a
   * search hit or restoring a deep link all land somewhere without a recorded
   * way back. Scroll to the top in that case — popping would throw.
   */
  public stepOut(): void {
    if (this.stack.isEmpty()) {
      this.setScrollId(baseId);
      return;
    }
    const previous = this.stack.pop();
    this.setScrollId(previous?.length > 0 ? previous : baseId);
  }

  public peekCurrentPathID(): string {
    return this.stack.isEmpty() ? '' : this.stack.peek();
  }

  public clear(): void {
    while (!this.stack.isEmpty()) {
      this.stack.pop();
    }
    this.setScrollId(baseId);
  }

  get scrollToID(): string {
    return this.scrollId();
  }

  public persistPathToRoot(): void {}

  /**
   * Brings the element with this id to the top of the browse view.
   *
   * It used to get there by focusing the element, because focus() scrolls its target into view. That
   * also left the focus parked on the element - and every dialog opened afterwards handed the focus
   * back to it when it closed (MatDialog restores focus in ngOnDestroy), which scrolled the listing
   * to the top again. In a long list that is a jump from wherever the user was to the very first row,
   * on closing the song options, the rating sheet or the playlist picker. Scrolling directly moves
   * the listing and nothing else, so there is no focus left behind to come back to.
   */
  public scrollIntoViewID(elementID?: string): void {
    if (!elementID) {
      elementID = this.scrollId();
    }
    console.log('[scroll] to ID : ' + elementID);
    const targetElement = document.getElementById(elementID);
    if (targetElement) {
      targetElement.scrollIntoView({ block: 'start' });
    } else {
      console.log('[scroll] id not found : ' + elementID);
    }
  }
}
