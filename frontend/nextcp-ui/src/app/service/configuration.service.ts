import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { UuidService } from './../util/uuid.service';
import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { RendererConfigDto, Config, RendererDeviceConfiguration, DeviceDriverCapability, ServerDeviceConfiguration, ServerConfigDto, ApplicationConfig, MusicbrainzSupport, MediaPlayerConfigDto, AudioAddictConfig } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { isAssigned } from '../global';

@Injectable({
  providedIn: 'root'
})

export class ConfigurationService {

  // Observer
  // ===============================================================================
  rendererConfigChanged$: Subject<RendererDeviceConfiguration[]> = new Subject();

  // Configs
  // ===============================================================================

  serverConfig: Config;                       // global serverside configuration file (server state) (read only)
  private rendererConfig: RendererConfigDto;  // renderer configurations
  serverConfigDto: ServerConfigDto;           // List of server devices

  public mediaPlayerConfigDto = signal<MediaPlayerConfigDto>({
    overwrite: false,
    playType: 'Album',
    script: '',
    workdir: '',
    addToFolderId: { id: '', title: '' },
    addToPlaylist: false,
    mediaServerUdn: '',
    addToPlaylistId: { id: '', title: '', },
  });

  public audioAddictConfig = signal<AudioAddictConfig>(this.dtoGeneratorService.emptyAudioAddictDto());

  applicationConfig: ApplicationConfig = {    // This is a DTO copy and can be used to update server configuration
    databaseFilename: '',
    embeddedServerPort: 0,
    embeddedServerSslPort: 0,
    embeddedServerSslP12Keystore: '',
    embeddedServerSslP12KeystorePassword: '',
    generateUpnpCode: false,
    generateUpnpCodePath: '',
    globalSearchDelay: 0,
    libraryPath: '',
    log4jConfigFile: '',
    loggingDateTimeFormat: '',
    sseEmitterTimeout: 0,
    itemsPerPage: 100,
    nextPageAfter: 60,
    pathToRestartScript: '',
    myPlaylistFolderName: '',
    upnpBindInterface: '',
  }

  musicBrainzConfig: MusicbrainzSupport = {   // MusicBrainz username/password
    password: '',
    username: ''
  }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  public deviceDriverList: DeviceDriverCapability[];

  baseUri = '/ConfigurationService';

  constructor(
    private http: HttpClient, 
    private genericResultService: GenericResultService, 
    sseService: SseService,
    public dtoGeneratorService: DtoGeneratorService,
    private httpService: HttpService, 
    private uuidService: UuidService) {
      this.getClientConfigFromServer();
      this.getDeviceDriverFromServer();
      this.getMediaRendererConfig();
      this.getMediaServerConfig();
      this.getMediaPlayerConfig();
      this.getCurrentMediaPlayerConfig();
      sseService.configChanged$.subscribe(serverConfig => this.applyServerConfigurationFile(serverConfig));
      sseService.rendererConfigChanged$.subscribe(data => this.applyRendererServerRendererList(data));
      sseService.serverDevicesConfigChanged$.subscribe(data => this.applyMediaServerList(data));
      console.log("ConfigurationService constructor call");
  }

  public restart(): void {
    const uri = '/restart';

    this.httpService.get(this.baseUri, uri).subscribe();
  }

  private getCurrentMediaPlayerConfig(): void {
    this.getMediaPlayerConfig().subscribe(data => {
      this.mediaPlayerConfigDto.set(data);
    });
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

  private getMediaPlayerConfig(): Subject<MediaPlayerConfigDto> {
    const uri = '/getMediaPlayerConfig';
    return this.httpService.get<MediaPlayerConfigDto>(this.baseUri, uri);
  }

  public saveMediaPlayerConfig(mediaPlayerConfig: MediaPlayerConfigDto): void {
    const uri = '/saveMediaPlayerConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaPlayerConfig, "Save media player config", "success").subscribe(() => {
      this.getCurrentMediaPlayerConfig();
    });
  }

  public saveAudioAddictConfig(): void {
    const uri = '/saveAudioAddictConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, this.audioAddictConfig(), "Save audio addict config", "success").subscribe();
  }

  public saveMusicBrainzConfig(): void {
    const uri = '/saveMusicBrainzConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, this.musicBrainzConfig, "Save MusicBrainz config", "success").subscribe();
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
  }

  private applyServerConfigurationFile(serverConfig: Config) {
    this.serverConfig = serverConfig;
    this.applicationConfig = Object.assign({}, serverConfig.applicationConfig);
    this.musicBrainzConfig = Object.assign({}, serverConfig.musicbrainzSupport);
    this.audioAddictConfig.set(serverConfig.audioAddictConfig);
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

  public spotifyAccountConnected(): boolean {
    return this.serverConfig?.spotifyConfig?.accountConnected ? true : false;
  }
}
