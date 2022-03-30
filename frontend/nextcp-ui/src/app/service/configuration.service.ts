import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { UiClientConfig, RendererConfigDto, Config, RendererDeviceConfiguration, DeviceDriverCapability } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { isAssigned } from '../global';

@Injectable({
  providedIn: 'root'
})

export class ConfigurationService {

  clientConfigChanged$: Subject<UiClientConfig> = new Subject();
  rendererConfigChanged$: Subject<RendererDeviceConfiguration[]> = new Subject();

  private clientUUID = this.getStoredClientId();

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  public deviceDriverList: DeviceDriverCapability[];

  // renderer configurations
  rendererConfig: RendererConfigDto;

  // configuration for this client
  public clientConfig: UiClientConfig = {
    clientName: "",
    uuid: "",
    defaultMediaRenderer: {
      friendlyName: "",
      udn: ""
    },
    defaultMediaServer: {
      friendlyName: "",
      udn: "",
      extendedApi: false
    }
  };

  // global serverside configuration file (server state)
  serverConfig: Config;

  baseUri = '/ConfigurationService';


  constructor(private http: HttpClient, private genericResultService: GenericResultService, sseService: SseService, private httpService: HttpService) {
    this.getClientConfigFromServer();
    this.getDeviceDriverFromServer();
    this.getMediaRendererConfig();
    sseService.configChanged$.subscribe(serverConfig => this.applyServerConfigurationFile(serverConfig));
    sseService.rendererConfigChanged$.subscribe(data => this.applyRendererServerRendererList(data));
  }

  public createNewUiClientConfig(newuuid: string): UiClientConfig {
    return { clientName: 'NewProfile', uuid: newuuid, defaultMediaRenderer: { friendlyName: '', udn: '' }, defaultMediaServer: { friendlyName: '', udn: '', extendedApi: false } };
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
      this.applyServerConfigurationFile(data);
    }, err => {
      this.genericResultService.displayHttpError(err, "cannot read configuration");
    });
  }

  private applyRendererServerRendererList(data: RendererConfigDto) {
    this.rendererConfig = data;

    this.rendererConfig.rendererDevices = this.rendererConfig?.rendererDevices.sort((n1, n2) => {
      return n1.displayString.localeCompare(n2.displayString);
    });
    this.updateUiClientConfig();
  }

  private applyServerConfigurationFile(serverConfig: Config) {
    this.serverConfig = serverConfig;
    this.updateUiClientConfig();
  }

  private updateUiClientConfig() {
    if (isAssigned(this.getClientConfigFromServerFile(this.clientUUID))) {
      this.clientConfig = this.getClientConfigFromServerFile(this.clientUUID);
      this.clientConfigChanged$.next(this.clientConfig);
    }
  }

  // Use this function to acquire a client global userID
  public getStoredClientId(): string {
    let cid: string = localStorage.getItem("clientID");
    if (cid) {
      return cid;
    }
    return;
  }

  public getRendererDevicesConfig(): RendererDeviceConfiguration[] {
    return this.rendererConfig?.rendererDevices;
  }

  public isRenderDeviceUdnActive(deviceUdn: string): boolean {
    const configEntry = this.rendererConfig.rendererDevices.filter(conf => conf.mediaRenderer.udn == deviceUdn);
    if (!configEntry || configEntry.length == 0) {
      return false;
    }
    return configEntry[0].active;
  }

  public selectClientConfig(uuid: string): void {
    this.clientConfig = this.getClientConfigFromServerFile(uuid);
    localStorage.setItem("clientID", uuid);
    this.clientConfigChanged$.next(this.clientConfig);
  }

  /**
   * @param uuid find config entry with supplies uuid.
   */
  public getClientConfigFromServerFile(uuid: string): UiClientConfig {
    return this.serverConfig.clientConfig.find(conf => conf.uuid === uuid);
  }

  public getActiveClientConfig(): UiClientConfig {
    return this.clientConfig;
  }

  public addNewClientConfig(): void {
    const newProfile = this.createNewUiClientConfig(this.getStoredClientId());
    this.serverConfig.clientConfig.push(newProfile);
    this.clientConfig = newProfile;
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
    const element: UiClientConfig = this.getClientConfigFromServerFile(uuid);
    return isAssigned(element);
  }

  public spotifyAccountConnected(): boolean {
    return this.serverConfig?.spotifyConfig?.accountConnected ? true : false;
  }

}
