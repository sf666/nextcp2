import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpResponse } from '@angular/common/http';
import { HttpRequest } from '@angular/common/http';
import { HttpHandler } from '@angular/common/http';
import { HttpEvent } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

/**
 * Purpose:
 * ==========================
 * 
 * 1. show / hide spinner on the view
 * 
 * 2. add nextcp2 user agent header
 * 
 */
@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {

     constructor(private spinnerService: SpinnerService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        this.spinnerService.show();

        const modifiedReq = req.clone({ 
            headers: req.headers.set('control-point', 'nextcp/2.0'),
          });

        return next.handle(modifiedReq)
             .pipe(tap((event: HttpEvent<any>) => {
                    if (event instanceof HttpResponse) {
                        this.spinnerService.hide();
                    }
                }, (error) => {
                    this.spinnerService.hide();
                }));
    }
}