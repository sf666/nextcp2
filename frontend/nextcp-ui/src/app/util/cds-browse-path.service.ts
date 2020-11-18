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
    this.scrollId = '';
  }

  public stepIn(path: string): void {
    this.visitedPathFromRoot.push(path);
    this.scrollId = '';
  }

  public stepOut() {
    this.scrollId = this.visitedPathFromRoot.pop();
  }

  public peekCurrentPathID(): string {
    if (this.visitedPathFromRoot.isEmpty()) {
      return '';
    }
    
    return this.visitedPathFromRoot.peek();
  }

  get scrollToID(): string {
    return this.scrollId;
  }
}
