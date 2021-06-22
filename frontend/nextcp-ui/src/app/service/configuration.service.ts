import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { UiClientConfig, RendererConfigDto, Config, MediaServerDto, MediaRendererDto, DeviceDriverState, RendererDeviceConfiguration, DeviceDriverCapability } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { v4 as uuid } from 'uuid';
import { isAssigned } from '../global';

@Injectable({
  providedIn: 'root'
})

export class ConfigurationService {

  clientConfigChanged$: Subject<UiClientConfig> = new Subject();
  rendererConfigChanged$: Subject<RendererDeviceConfiguration[]> = new Subject();

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  public deviceDriverList: DeviceDriverCapability[];

  // renderer configurations
  rendererConfig: RendererConfigDto;

  // configuration for this client
  clientConfig: UiClientConfig = {
    clientName: "",
    uuid: "",
    defaultMediaRenderer: {
      friendlyName: "",
      udn: ""
    },
    defaultMediaServer: {
      friendlyName: "",
      udn: ""
    }
  };

  // global serverside configuration file
  serverConfig: Config;

  baseUri = '/ConfigurationService';


  constructor(private http: HttpClient, private genericResultService: GenericResultService, sseService: SseService, private httpService: HttpService) {
    this.clientConfig.uuid = this.getStoredClientId();
    this.getClientConfigFromServer();
    this.getDeviceDriverFromServer();
    this.getMediaRendererConfig();
    sseService.configChanged$.subscribe(data => this.applyConfig(data));
    sseService.rendererConfigChanged$.subscribe(data => this.applyRendererConfig(data));
  }

  public restart(): void {
    const uri = '/restart';

    this.httpService.get(this.baseUri, uri).subscribe();
  }

  private getDeviceDriverFromServer() {
    const uri = '/availableDeviceDriver';

    this.httpService.get<DeviceDriverCapability[]>(this.baseUri, uri).subscribe(data => {
      this.deviceDriverList = data;
    });
  }

  public saveMediaRendererConfig(mediaRendererConfig: RendererDeviceConfiguration): void {
    const uri = '/saveMediaRendererConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaRendererConfig, "Save mediarenderer config", "success").subscribe();
  }

  public deleteMediaRendererConfig(mediaRendererConfig: RendererDeviceConfiguration): void {
    const uri = '/deleteMediaRendererConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaRendererConfig, "Delete mediarenderer config", "success").subscribe();
  }

  public getMediaRendererConfig(): void {
    const uri = '/getMediaRendererConfig';
    this.httpService.get<RendererConfigDto>(this.baseUri, uri).subscribe(data =>
      this.rendererConfig = data);
  }

  private getClientConfigFromServer() {
    const uri = '/configuration';
    this.http.get<Config>(this.baseUri + uri).subscribe(data => {
      this.applyConfig(data);
    }, err => {
      this.genericResultService.displayHttpError(err, "cannot read configuration");
    });
  }

  private applyRendererConfig(data: RendererConfigDto) {
    this.rendererConfig = data;

    this.rendererConfig.rendererDevices = this.rendererConfig?.rendererDevices.sort((n1, n2) => {
      return n1.displayString.localeCompare(n2.displayString);
    });

  }

  private applyConfig(data: Config) {
    this.serverConfig = data;

    if (isAssigned(this.getClientConfig(this.clientConfig.uuid))) {
      this.clientConfig = this.getClientConfig(this.clientConfig.uuid);
      this.clientConfigChanged$.next(this.clientConfig);
    }
  }

  // Use this function to aquire a vlient global userID
  public getStoredClientId(): string {
    let cid: string = localStorage.getItem("clientID");
    if (cid) {
      return cid;
    }
    cid = uuid();
    localStorage.setItem("clientID", cid);
    return cid;
  }

  public getRendererDevicesConfig(): RendererDeviceConfiguration[] {
    return this.rendererConfig?.rendererDevices;
  }

  public isRenderDeviceActive(deviceUdn: string): boolean {
    const configEntry = this.rendererConfig.rendererDevices.filter(conf => conf.mediaRenderer.udn == deviceUdn);
    if (!configEntry || configEntry.length == 0) {
      return false;
    }
    return configEntry[0].active;
  }

  public selectClientConfig(uuid): void {
    this.clientConfig = this.getClientConfig(uuid);
    localStorage.setItem("clientID", uuid);
    this.clientConfigChanged$.next(this.clientConfig);
  }

  /**
   * @param uuid find config entry with supplies uuid.
   */
  public getClientConfig(uuid: string): UiClientConfig {
    return this.serverConfig.clientConfig.find(conf => conf.uuid === uuid);
  }

  public saveClientConfig(): void {
    const uri = '/saveClientProfile';
    this.http.post(this.baseUri + uri, this.clientConfig).subscribe(data => {
      this.genericResultService.displayGenericMessage("configuration", "client configuration saved.");
    }, err => {
      this.genericResultService.displayHttpError(err, "cannot save client configuration");
    });

  }

  public existClientConfig(uuid: string): boolean {
    const element: UiClientConfig = this.getClientConfig(uuid);
    return isAssigned(element);
  }

  public spotifyAccountConnected() : boolean {
    return this.serverConfig?.spotifyConfig?.accountConnected ? true : false;
  }

}
