import { HttpHeaders, HttpClient } from '@angular/common/http';
import { DtoGeneratorService } from './../util/dto-generator.service';
import { SystemInformationDto } from './dto.d';
import { HttpService } from './http.service';
import { Injectable, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SystemService {

  private baseUri = '/SystemService';
  private systemInformationDto: SystemInformationDto;

  constructor(
    private httpService: HttpService,
    private http: HttpClient,
    private dtoGeneratorService: DtoGeneratorService) {
    this.systemInformationDto = this.dtoGeneratorService.generateSystemInformationDto();
    this.httpService.get<SystemInformationDto>(this.baseUri, "/getSystemInformation").subscribe(version => this.updateBuildVersion(version));
  }

  private updateBuildVersion(data: SystemInformationDto) {
    this.systemInformationDto = data;
  }

  public registerNextcp2AtLastFM(): void {
    const options = {
      responseType: 'text',
    };
    this.http.get("/SystemService/getLastFmAppRegistration", {responseType: 'text'}).subscribe(url => this.openLastFMUrl(url));
      
  }

  private openLastFMUrl(url: string) {
    console.log("opening LastFM registration link : " + url);
    window.open(url, "_blank"); // , "_blank");
  }

  public get buildVersion(): string {
    return this.systemInformationDto.buildNumber;
  }
}