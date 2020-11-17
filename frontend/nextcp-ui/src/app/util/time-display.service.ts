import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TimeDisplayService {

  constructor() { }

  public convertLongToDateString(seconds : number) {
    let date = new Date(Date.UTC(0, 0, 0, 0, 0, seconds));
    let s = (date.getUTCHours() > 0 ?
      date.getUTCHours().toString() + ':' + date.getUTCMinutes().toString().padStart(2, '0') :
      date.getUTCMinutes().toString()) +
      ':' + date.getUTCSeconds().toString().padStart(2, '0');
    return s;    
  }
}
