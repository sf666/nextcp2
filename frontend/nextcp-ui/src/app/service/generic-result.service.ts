import { SseService } from './sse/sse.service';
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

  constructor(
    private toastr: ToastrService,
    private sseService: SseService,
  ) {
    sseService.toasterMessageReceived$.subscribe(message => {
      if (message.type.toLowerCase() === "info") {
        this.toastr.info(message.body, message.header);
      }
      else if (message.type.toLowerCase() === "error") {
        this.toastr.error(message.body, message.header);
      }
      else if (message.type.toLowerCase() === "success") {
        this.toastr.success(message.body, message.header);
      } else {
        // if no type is given, default to info
        this.toastr.info(message.body, message.header);
      }
    });
  }

  public displayToastr(result: GenericResult) : void {
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

  public displayGenericMessage(header: string, body: string) : void {
    this.displaySuccessMessage(header, body);
  }

  public displayHttpError(err, toastrMessage) : void {
    if (err.status == 504) {
      this.displayErrorMessage("Server unavailable. Please check if your computer is connected to your LAN and nextcp/2 server process is online.", "gateway error");
    }
    else if (err.status == 417) {
      this.toastr.error(err.error.message, "invalid request");
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
  public displayErrorMessage(message: string, head: string) : void {
    if (!this.lastError.includes(message)) {
      this.toastr.error(message, head);
      this.lastError.push(message);
      void delay(this.debounceDelayMs).then(() => this.lastError.filter(item => item !== message));
    }
  }

  public displaySuccessMessage(head: string, message: string) : void {
    if (!this.lastError.includes(message)) {
      this.toastr.info(message, head);
      this.lastError.push(message);
      void delay(this.debounceDelayMs).then(() => this.lastError.filter(item => item !== message));
    }
  }
}
