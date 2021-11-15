import { Injectable } from '@angular/core';
import Stack from 'ts-data.stack';

@Injectable({
  providedIn: 'root'
})

export class CdsBrowsePathService {

  private visitedPathFromRoot: Stack<string>;
  private scrollId: string;

  constructor() {
    this.visitedPathFromRoot = new Stack();
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
  }

  public stepIn(path: string): void {
    this.visitedPathFromRoot.push(path);
    this.scrollId = 'ID_SCROLL_TO_ELEMENT_STEP_IN';
  }

  public stepOut(): void {
    this.scrollId = this.visitedPathFromRoot.pop();
  }

  public peekCurrentPathID(): string {
    if (this.visitedPathFromRoot.isEmpty()) {
      return '';
    }
    
    return this.visitedPathFromRoot.peek();
  }

  public clearPath() : void {
    while (!this.visitedPathFromRoot.isEmpty) {
      this.visitedPathFromRoot.pop();
    }
  }

  get scrollToID(): string {
    return this.scrollId;
  }
}
