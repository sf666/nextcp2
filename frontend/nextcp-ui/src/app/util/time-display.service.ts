import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TimeDisplayService {

  constructor() { }

  public convertLongToDateString(seconds : number) {
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)
    const remainingSeconds = seconds % 60

    let s = (hours > 0 ?
      hours.toString().padStart(2, '0') + 'h ' + 
      minutes.toString().padStart(2, '0') + 'm ' + 
      remainingSeconds.toString().padStart(2, '0') + 's ' :
      minutes.toString().padStart(2, '0') + 'm ' +
      remainingSeconds.toString().padStart(2, '0') + 's ');
    return s;
  }

  public convertLongToDateStringShort(seconds : number) {
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)
    const remainingSeconds = seconds % 60

    let s = (hours > 0 ?
      hours.toString().padStart(2, '0') + ':' + 
      minutes.toString().padStart(2, '0') + ':' + 
      remainingSeconds.toString().padStart(2, '0') :
      minutes.toString().padStart(2, '0') + ':' +
      remainingSeconds.toString().padStart(2, '0'));
    return s;
  }
}
