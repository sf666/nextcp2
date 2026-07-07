import { HttpClient } from '@angular/common/http';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { SystemInformationDto } from './dto.d';
import { HttpService } from './http.service';
import { Injectable, signal, inject } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SystemService {
  private httpService = inject(HttpService);
  private http = inject(HttpClient);
  private dtoGeneratorService = inject(DtoGeneratorService);

  private baseUri = '/SystemService';
  private systemInformationDto = signal<SystemInformationDto>(
    this.dtoGeneratorService.generateSystemInformationDto(),
  );

  // True when the backend runs as the native desktop app; gates the shutdown button.
  public readonly desktopMode = signal<boolean>(false);

  constructor() {
    this.httpService
      .get<SystemInformationDto>(this.baseUri, '/getSystemInformation')
      .subscribe((version) => this.updateBuildVersion(version));
    this.httpService
      .get<boolean>(this.baseUri, '/isDesktopMode')
      .subscribe((desktop) => this.desktopMode.set(desktop));
  }

  private updateBuildVersion(data: SystemInformationDto) {
    this.systemInformationDto.set(data);
  }

  public registerNextcp2AtLastFM(): void {
    const options = {
      responseType: 'text',
    };
    this.http
      .get('/SystemService/getLastFmAppRegistration', { responseType: 'text' })
      .subscribe((url) => this.openUrl(url));
  }

  public restart(): void {
    this.http.get('/SystemService/restartNextcp2').subscribe();
  }

  public shutdown(): void {
    this.http.get('/SystemService/shutdownNextcp2').subscribe();
  }

  public registerNextcp2AtSpotify(): void {
    const options = {
      responseType: 'text',
    };

    const protocolHandler =
      window.isSecureContext &&
      typeof (navigator as Navigator & { registerProtocolHandler?: unknown })
        .registerProtocolHandler === 'function';

    this.http
      .post('/SystemService/getSpotifyAppRegistration', protocolHandler, {
        responseType: 'text',
      })
      .subscribe((url) => window.open(url, '_blanc'));
  }

  public setSpotifyCode(code: string): void {
    const options = {
      responseType: 'text',
    };
    this.httpService
      .get(this.baseUri, '/spotifyCallback?code=' + code)
      .subscribe();
  }

  public getLastFmSession(): void {
    const options = {
      responseType: 'text',
    };
    this.httpService.get(this.baseUri, '/createLastFmSession').subscribe();
  }

  private openUrl(url: string) {
    console.log('opening registration link : ' + url);
    const frame = document.getElementById('spotifyIframe');
    if (frame) {
      frame.setAttribute('src', url);
    }
  }

  public get build(): SystemInformationDto {
    return this.systemInformationDto();
  }
}
