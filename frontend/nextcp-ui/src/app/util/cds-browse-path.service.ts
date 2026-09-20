import { Injectable, signal, inject } from '@angular/core';
import { PersistenceService } from '../service/persistence/persistence.service';
import Stack from './stack';

/**
 * Stepping into a container scrolls to the top of the page. This used to be an element id on the
 * header's info column, scrolled into view - but that column is centred against a tall cover, so it
 * starts below the top of the page and dragged the header out of view with it.
 */
const SCROLL_TO_TOP = 'SCROLL_TO_TOP';

/** What the marker above was called while it was a real element id; may still sit in storage. */
const LEGACY_TOP_ID = 'ID_SCROLL_TO_ELEMENT_STEP_IN';

@Injectable()
export class CdsBrowsePathService {
  private persistenceService = inject(PersistenceService);

  stack = new Stack<string>();

  scrollId = signal<string>(SCROLL_TO_TOP);

  constructor() {
    console.log('[CdsBrowsePathService] constructor call');
    var lastFocusId = this.persistenceService.getLastFocusId();
    if (lastFocusId) {
      this.scrollId.set(
        lastFocusId === LEGACY_TOP_ID ? SCROLL_TO_TOP : lastFocusId,
      );
    }
  }

  setScrollId(id: string): void {
    this.persistenceService.setLastFocusID(id);
    this.scrollId.set(id);
  }

  public stepIn(objectId: string): void {
    this.setScrollId(SCROLL_TO_TOP);
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
      this.setScrollId(SCROLL_TO_TOP);
      return;
    }
    const previous = this.stack.pop();
    this.setScrollId(previous?.length > 0 ? previous : SCROLL_TO_TOP);
  }

  public peekCurrentPathID(): string {
    return this.stack.isEmpty() ? '' : this.stack.peek();
  }

  public clear(): void {
    while (!this.stack.isEmpty()) {
      this.stack.pop();
    }
    this.setScrollId(SCROLL_TO_TOP);
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
    if (this.isScrollToTop(elementID)) {
      this.scrollPageToTop();
      return;
    }
    const targetElement = document.getElementById(elementID);
    if (targetElement) {
      targetElement.scrollIntoView({ block: 'start' });
    } else {
      console.log('[scroll] id not found : ' + elementID);
    }
  }

  /** Whether this target means the top of the page rather than an element to scroll to. */
  public isScrollToTop(elementID?: string): boolean {
    return !elementID || elementID === SCROLL_TO_TOP;
  }

  /** The page's scroll container; every browse view lives inside it. */
  private scrollPageToTop(): void {
    const parent = document.getElementById('mainContent');
    if (parent) {
      parent.scrollTop = 0;
    }
  }
}
