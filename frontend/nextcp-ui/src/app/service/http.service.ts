import { GenericResult } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Subject, timeout } from 'rxjs';

@Injectable({
  providedIn: 'root',
})

/**
 * Default HTTP communication implementation. Don't forget to subscribe, so the call goes out.
 */
export class HttpService {
  /**
   * Upper bound for a single request.
   *
   * Without one a request that the server never answers keeps its connection open forever, and a
   * browser allows only a handful of them per origin - a few stuck calls and the whole app looks
   * dead. The value is deliberately generous: a media server browsing a large playlist for the first
   * time has been measured at about a minute, and cutting that short would break a legitimate
   * operation. Pass timeoutMs to override it where a call is known to be quick or slow.
   */
  public static readonly DEFAULT_TIMEOUT_MS = 90_000;

  private http = inject(HttpClient);
  private genericResultService = inject(GenericResultService);

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      Application: 'nextcp2',
    }),
  };

  /**
   *
   * @param base default get behaviour
   * @param path
   */
  public get<T>(
    base: string,
    path: string,
    errorHeader?: string,
    timeoutMs: number = HttpService.DEFAULT_TIMEOUT_MS,
  ): Subject<T> {
    const ret = new Subject<T>();
    this.http
      .get<T>(base + path)
      .pipe(timeout(timeoutMs))
      .subscribe(
        (data) => {
          // A one shot request has exactly one answer. Without completing here, every
          // consumer that waits for the end of the stream - concatMap, forkJoin, a
          // throttle counting its free slots - waits forever.
          ret.next(data);
          ret.complete();
        },
        (err) => {
          this.genericResultService.displayHttpError(
            err,
            errorHeader == null ? 'communication error' : errorHeader,
          );
          console.log(err);
        },
      );
    return ret;
  }

  public getWithGenericResult(
    base: string,
    path: string,
    errorHeader?: string,
  ): void {
    this.get<GenericResult>(base, path, errorHeader).subscribe((data) => {
      this.genericResultService.displayGenericResult(data);
    });
  }

  public postWithGenericResult(
    base: string,
    path: string,
    payload: any,
    errorHeader?: string,
  ): void {
    this.post<GenericResult>(base, path, payload).subscribe((data) => {
      this.genericResultService.displayGenericResult(data);
    });
  }

  /**
   *
   * @param base default post behaviour
   * @param path
   * @param payload
   */
  public post<T>(
    base: string,
    path: string,
    payload: any,
    errorHeader?: string,
    timeoutMs: number = HttpService.DEFAULT_TIMEOUT_MS,
  ): Subject<T> {
    const ret = new Subject<T>();
    this.http
      .post<T>(base + path, payload)
      .pipe(timeout(timeoutMs))
      .subscribe({
        next: (data) => {
          ret.next(data);
          ret.complete();
        },
        error: (err) => {
          this.genericResultService.displayHttpError(
            err,
            errorHeader == null ? 'communication error' : errorHeader,
          );
          console.log(err);
          return ret.error(err);
        },
      });
    return ret;
  }

  public postWithSuccessMessage<T>(
    base: string,
    path: string,
    payload: any,
    successHeader: string,
    successBody: string,
    errorHeader?: string,
    timeoutMs: number = HttpService.DEFAULT_TIMEOUT_MS,
  ): Subject<T> {
    const ret = new Subject<T>();
    this.http
      .post<T>(base + path, payload)
      .pipe(timeout(timeoutMs))
      .subscribe({
        next: (data) => {
          this.genericResultService.displaySuccessMessage(
            successHeader,
            successBody,
          );
          ret.next(data);
          ret.complete();
        },
        error: (err) => {
          this.genericResultService.displayHttpError(
            err,
            errorHeader == null ? 'communication error' : errorHeader,
          );
          console.log(err);
        },
      });
    return ret;
  }
}
