import { UuidService } from './../util/uuid.service';
import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { UiClientConfig, RendererConfigDto, Config, RendererDeviceConfiguration, DeviceDriverCapability, ServerDeviceConfiguration, ServerConfigDto, ApplicationConfig } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { isAssigned } from '../global';

@Injectable({
  providedIn: 'root'
})

export class ConfigurationService {

  // Observer
  // ===============================================================================
  clientConfigChanged$: Subject<UiClientConfig> = new Subject();
  rendererConfigChanged$: Subject<RendererDeviceConfiguration[]> = new Subject();

  // Configs
  // ===============================================================================

  serverConfig: Config;                       // global serverside configuration file (server state) (read only)
  private rendererConfig: RendererConfigDto;  // renderer configurations
  serverConfigDto: ServerConfigDto;           // List of server devices
  applicationConfig: ApplicationConfig =  {   // This is a DTO copy and can be used to update server configuration
    databaseFilename:'',
    embeddedServerPort: 0,
    generateUpnpCode:false,
    generateUpnpCodePath:'',
    globalSearchDelay:0,
    libraryPath:'',
    log4jConfigFile:'',
    loggingDateTimeFormat: '',
    sseEmitterTimeout: 0
  }      

  private clientUUID = this.getStoredClientId();

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  public deviceDriverList: DeviceDriverCapability[];

  // configuration for this client
  public clientConfig: UiClientConfig = {
    clientName: "",
    uuid: "",
    defaultMediaRenderer: {
      friendlyName: "",
      udn: "",
      services: []
    },
    defaultMediaServer: {
      friendlyName: "",
      udn: "",
      extendedApi: false
    }
  };

  baseUri = '/ConfigurationService';


  constructor(private http: HttpClient, private genericResultService: GenericResultService, sseService: SseService, private httpService: HttpService, private uuidService: UuidService) {
    this.getClientConfigFromServer();
    this.getDeviceDriverFromServer();
    this.getMediaRendererConfig();
    this.getMediaServerConfig();
    sseService.configChanged$.subscribe(serverConfig => this.applyServerConfigurationFile(serverConfig));
    sseService.rendererConfigChanged$.subscribe(data => this.applyRendererServerRendererList(data));
    sseService.serverDevicesConfigChanged$.subscribe(data => this.applyMediaServerList(data));
  }

  public createNewUiClientConfig(newUUID: string): UiClientConfig {
    return { clientName: 'NewProfile', uuid: newUUID, defaultMediaRenderer: { friendlyName: '', udn: '', services: [] }, defaultMediaServer: { friendlyName: '', udn: '', extendedApi: false } };
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

  public getServerConfig() {
    return this.serverConfigDto;
  }

  public saveApplicationConfig(): void {
    const uri = '/saveApplicationConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, this.applicationConfig, "Save application config", "success").subscribe();
  }

  public saveMediaRendererConfig(mediaRendererConfig: RendererDeviceConfiguration): void {
    const uri = '/saveMediaRendererConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaRendererConfig, "Save mediarenderer config", "success").subscribe();
  }

  public deleteMediaRendererConfig(mediaRendererConfig: RendererDeviceConfiguration): void {
    const uri = '/deleteMediaRendererConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaRendererConfig, "Delete mediarenderer config", "success").subscribe();
  }

  public saveMediaServerConfig(mediaServerConfig: ServerDeviceConfiguration): void {
    const uri = '/saveMediaServerConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaServerConfig, "Save mediaserver config", "success").subscribe();
  }

  public deleteMediaServerConfig(mediaServerConfig: ServerDeviceConfiguration): void {
    const uri = '/deleteMediaServerConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaServerConfig, "Delete mediaserver config", "success").subscribe();
  }

  public getMediaRendererConfig(): void {
    const uri = '/getMediaRendererConfig';
    this.httpService.get<RendererConfigDto>(this.baseUri, uri).subscribe(data => {
      this.rendererConfig = data;
    });
  }

  public getMediaServerConfig(): void {
    const uri = '/getMediaServerConfig';
    this.httpService.get<ServerConfigDto>(this.baseUri, uri).subscribe(data =>
      this.serverConfigDto = data);
  }

  private getClientConfigFromServer() {
    const uri = '/configuration';
    this.http.get<Config>(this.baseUri + uri).subscribe(data => {
      this.applyServerConfigurationFile(data);
    }, err => {
      this.genericResultService.displayHttpError(err, "cannot read configuration");
    });
  }

  private applyMediaServerList(data: ServerConfigDto) {
    this.serverConfigDto = data;

    this.serverConfigDto.serverDevices = this.serverConfigDto?.serverDevices.sort((n1, n2) => {
      return n1.displayString.localeCompare(n2.displayString);
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
    this.applicationConfig = Object.assign({}, serverConfig.applicationConfig);
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
      console.log("looked up from local storage an UUID of : " + cid);
      return cid;
    } else {
      cid = this.uuidService.getUnique5();
      console.log("created new client UUID of : " + cid);
      return cid;
    }
  }

  public getRendererDevicesConfig(): RendererDeviceConfiguration[] {
    if (this.rendererConfig) {
      return this.rendererConfig?.rendererDevices;
    }
    return [];
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
    this.clientUUID = uuid;
    console.log("selecting client config : " + this.clientConfig.clientName + " with uuid of : " + this.clientConfig.uuid + " in local storage.");
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
    const newUUID = this.uuidService.getUnique5();
    const newProfile = this.createNewUiClientConfig(newUUID);
    this.serverConfig.clientConfig.push(newProfile);
    this.selectClientConfig(newUUID);
  }

  public saveClientProfile(): void {
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
