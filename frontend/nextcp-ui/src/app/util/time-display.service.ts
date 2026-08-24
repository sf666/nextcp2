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

  /**
   * Formats a number of seconds as mm:ss, or hh:mm:ss from one hour on. Matches the backend's
   * DisplayUtils.convertToDigitString, so a client-side interpolated value and a value pushed by the
   * backend never differ in format.
   */
  public convertLongToDateStringShort(seconds : number) {
    if (!Number.isFinite(seconds) || seconds < 0) {
      return '00:00';
    }
    const total = Math.floor(seconds)
    const hours = Math.floor(total / 3600)
    const minutes = Math.floor((total % 3600) / 60)
    const remainingSeconds = total % 60

    let s = (hours > 0 ?
      hours.toString().padStart(2, '0') + ':' + 
      minutes.toString().padStart(2, '0') + ':' + 
      remainingSeconds.toString().padStart(2, '0') :
      minutes.toString().padStart(2, '0') + ':' +
      remainingSeconds.toString().padStart(2, '0'));
    return s;
  }
}
