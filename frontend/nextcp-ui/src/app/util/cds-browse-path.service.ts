import { Inject, Injectable } from '@angular/core';
import Stack from 'ts-data.stack';

@Injectable({
  providedIn: 'root'
})

export class CdsBrowsePathService {

  private visitedPathFromRoot: Stack<string>;
  private visitedPathFromRootAsString: string;
  private scrollId: string;
  private uniqueId: string;

  constructor(@Inject('uniqueId') private uniqueId_: string) {
    this.uniqueId = uniqueId_;
    this.visitedPathFromRoot = new Stack();
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
    this.visitedPathFromRootAsString = '';
    if (uniqueId_?.length == 0) {
      console.error("provide uniqueId");
    }
  }

  public stepIn(path: string): void {
    this.visitedPathFromRoot.push(path);
    this.addPathToString(path);
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
  }

  private addPathToString(path: string) {
    if (this.visitedPathFromRootAsString.length > 0) {
      this.visitedPathFromRootAsString = this.visitedPathFromRootAsString.concat(',');
    }
    this.visitedPathFromRootAsString = this.visitedPathFromRootAsString.concat(path);

  }

  public stepOut(): void {
    if (this.visitedPathFromRoot.peek()) {
      this.scrollId = this.visitedPathFromRoot.pop();
      this.visitedPathFromRootAsString = this.visitedPathFromRootAsString.substring(0, this.visitedPathFromRootAsString.lastIndexOf(','));
    }
  }

  public peekCurrentPathID(): string {
    if (this.visitedPathFromRoot.isEmpty()) {
      return '';
    }

    return this.visitedPathFromRoot.peek();
  }

  public clearPath(): void {
    while (!this.visitedPathFromRoot.isEmpty) {
      this.visitedPathFromRoot.pop();
    }
    this.visitedPathFromRootAsString = '';
    this.persistPathToRoot();
  }

  get scrollToID(): string {
    return this.scrollId;
  }

  public persistPathToRoot(): void {
    localStorage.setItem(this.uniqueId + 'visitedPath', this.visitedPathFromRootAsString);
  }

  public restorePathToRoot(): void {
    const pathStack = localStorage.getItem(this.uniqueId + 'visitedPath');
    this.clearPath();
    if (pathStack) {
      pathStack.split(',').forEach(p => {
        this.visitedPathFromRoot.push(p);
        this.addPathToString(p);
      });
    }
    this.persistPathToRoot();
    console.log('path restored');
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
