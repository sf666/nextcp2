import { HttpHeaders, HttpClient } from '@angular/common/http';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { SystemInformationDto } from './dto.d';
import { HttpService } from './http.service';
import { Injectable, OnInit, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  private baseUri = '/SystemService';
  private systemInformationDto = signal<SystemInformationDto>(this.dtoGeneratorService.generateSystemInformationDto());

  constructor(
    private httpService: HttpService,
    private http: HttpClient,
    private dtoGeneratorService: DtoGeneratorService) {
    this.httpService.get<SystemInformationDto>(this.baseUri, "/getSystemInformation").subscribe(version => this.updateBuildVersion(version));
  }

  private updateBuildVersion(data: SystemInformationDto) {
    this.systemInformationDto.set(data);
  }

  public registerNextcp2AtLastFM(): void {
    const options = {
      responseType: 'text',
    };
    this.http.get("/SystemService/getLastFmAppRegistration", { responseType: 'text' }).subscribe(url => this.openUrl(url));
  }

  public restart(): void {
    this.http.get("/SystemService/restartNextcp2").subscribe();
  } 

  public registerNextcp2AtSpotify(): void {
    const options = {
      responseType: 'text',
    };
    
    let protocolHandler: boolean;
    if (navigator.registerProtocolHandler) {
      protocolHandler = true;
    } else {
      protocolHandler = false;
    }

    this.http.post("/SystemService/getSpotifyAppRegistration", protocolHandler, { responseType: 'text' }).subscribe(url => window.open(url, "_blanc"));
  }

  public setSpotifyCode(code: string): void {
    const options = {
      responseType: 'text',
    };
    this.httpService.get(this.baseUri, "/spotifyCallback?code=" + code).subscribe();
  }

  public getLastFmSession(): void {
    const options = {
      responseType: 'text',
    };
    this.httpService.get(this.baseUri, "/createLastFmSession").subscribe();
  }

  private openUrl(url: string) {
    console.log("opening registration link : " + url);
    const frame = document.getElementById("spotifyIframe");
    frame.setAttribute("src", url);
  }

  public get build(): SystemInformationDto {
    return this.systemInformationDto();
  }
}
