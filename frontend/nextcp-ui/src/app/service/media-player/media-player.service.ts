import { ConfigurationService } from './../configuration.service';
import { DeviceService } from './../device.service';
import { distinctUntilChanged, map, merge, startWith, Subject, switchMap } from 'rxjs';
import { HttpService } from './../http.service';
import { Injectable, signal, inject } from '@angular/core';
import { SseService } from './../sse/sse.service';

@Injectable({
  providedIn: 'root',
})
export class MediaPlayerService {
  private deviceService = inject(DeviceService);
  private httpService = inject(HttpService);

  baseUri = '/MediaRendererService';

  public mediaPlayerExists = signal<boolean>(false);

  constructor() {
    const configService = inject(ConfigurationService);
    const sseService = inject(SseService);
    const mediaPlayerSettingChanged$ = sseService.configChanged$.pipe(
      map((config) => config.applicationConfig?.mediaPlayerEnabled ?? true),
      distinctUntilChanged(),
    );

    merge(
      configService.applicationConfigChanged$,
      mediaPlayerSettingChanged$,
      sseService.connectionOpened$,
    )
      .pipe(
        startWith(undefined),
        switchMap(() => this.mediaPlayerExistsRequest()),
      )
      .subscribe((status) => this.mediaPlayerExists.set(status));
  }

  public mediaPlayerExistsRequest(): Subject<boolean> {
    const uri = '/mediaPlayerExists';
    return this.httpService.get<boolean>(this.baseUri, uri);
  }

  public isPlayScreening(): Subject<boolean> {
    const uri = '/isPlayScreening';
    return this.httpService.get<boolean>(this.baseUri, uri);
  }

  public stopPlayScreening(): void {
    const uri = '/stopPlayScreening';
    this.httpService.get<boolean>(this.baseUri, uri);
  }

  public startPlayScreening(): void {
    const uri = '/startPlayScreening';
    this.httpService.get<boolean>(this.baseUri, uri);
  }

  public createFolder(): void {
    const uri = '/createFolder';
    this.httpService.post<boolean>(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaServerDevice().udn,
    );
  }

  public upload(): void {
    const uri = '/upload';
    console.log(
      'upload : ' + this.deviceService.selectedMediaServerDevice().udn,
    );
    this.httpService.post<boolean>(
      this.baseUri,
      uri,
      this.deviceService.selectedMediaServerDevice().udn,
    );
  }
}
