import { delay } from './../global';
import { GenericResult } from './generic-result.service.d';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GenericResultService {

  private lastError: Array<string> = new Array();;
  private debounceDelayMs: number = 1000;

  constructor(private toastr: ToastrService) { }

  public displayToastr(result: GenericResult) {
    if (typeof result !== 'undefined') {
      if (result.success) {
        this.toastr.info(result.message, result.headerMessage);
      }
      else {
        this.toastr.error(result.message, result.headerMessage, {
          disableTimeOut: false,
        });
      }
    }
    else {
      this.toastr.error("undefined result", "server response");
    }
  }

  public displayGenericMessage(header: string, body: string) {
      this.displaySuccessMessage(header, body);
  }

  public displayHttpError(err, toastrMessage) {
    if (err.status == 504) {
      this.displayErrorMessage("Server unavailable. Please check if your computer is connected to your LAN and nextcp/2 server process is online.", "gateway error");
    }
    else {
      this.toastr.error(err.error.message, toastrMessage);
    }
    return console.error(err);
  }

  /**
   * Displays error message
   * 
   * @param message Shows error messages. Debouncing message text.
   * @param head 
   */
  public displayErrorMessage(message: string, head: string) {
    if (!this.lastError.includes(message)) {
      this.toastr.error(message, head);
      this.lastError.push(message);
      delay(this.debounceDelayMs).then(() => this.lastError.filter(item => item !== message));
    }
  }

  public displaySuccessMessage(head: string, message: string) {
    if (!this.lastError.includes(message)) {
      this.toastr.info(message, head);
      this.lastError.push(message);
      delay(this.debounceDelayMs).then(() => this.lastError.filter(item => item !== message));
    }
  }
}
