import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScrollViewService {

  constructor() { }

  public scrollIntoViewID(elementID: string): void {
    let elmnt = document.getElementById(elementID);
    if (elmnt) {
      elmnt.scrollIntoView({
        behavior: "auto",
        block: "center",
        inline: "center",
      });
    }
  }
}