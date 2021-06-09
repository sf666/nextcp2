import { GenericResultService } from './generic-result.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

/**
 * Default HTTP communication implementation. Don't forget to subscribe, so the call goes out.
 */
export class HttpService {

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(
    private http: HttpClient,
    private genericResultService: GenericResultService
  ) { }

  /**
   * 
   * @param base default get behaviour
   * @param path 
   */
  public get<T>(base: string, path: string, errorHeader?: string): Subject<T> {
    const ret = new Subject<T>();
    this.http.get<T>(base + path).subscribe(data => {
      return ret.next(data);
    }, err => {
      this.genericResultService.displayHttpError(err, errorHeader == null ? "communication error" : errorHeader);
      console.log(err);
    });
    return ret;
  }

  /**
   * 
   * @param base default post behaviour
   * @param path 
   * @param payload 
   */
  public post<T>(base: string, path: string, payload: any, errorHeader?: string): Subject<T> {
    const ret = new Subject<T>();
    this.http.post<T>(base + path, payload).subscribe(data => {
      return ret.next(data);
    }, err => {
      this.genericResultService.displayHttpError(err, errorHeader == null ? "communication error" : errorHeader);
      console.log(err);
    });
    return ret;
  }

  public postWithSuccessMessage<T>(base: string, path: string, payload: any, successHeader: string, successBody: string, errorHeader?: string): Subject<T> {
    const ret = new Subject<T>();
    this.http.post<T>(base + path, payload).subscribe(data => {
      this.genericResultService.displaySuccessMessage(successHeader, successBody);
      return ret.next(data);
    }, err => {
      this.genericResultService.displayHttpError(err, errorHeader == null ? "communication error" : errorHeader);
      console.log(err);
    });
    return ret;
  }
}
