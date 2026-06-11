import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { UuidService } from './../util/uuid.service';
import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { RendererConfigDto, Config, RendererDeviceConfiguration, DeviceDriverCapability, ServerDeviceConfiguration, ServerConfigDto, ApplicationConfig, MusicbrainzSupport, MediaPlayerConfigDto, AudioAddictConfig, AiConfig, AiProvidersDto, AiModelsDto, AiProviderProfile, AiToolDto, AiToolsDto } from './dto.d';
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
  public rendererConfigChanged$: Subject<RendererDeviceConfiguration[]> = new Subject();
  public applicationConfigChanged$: Subject<ApplicationConfig> = new Subject();
  
  public serverConfigurationChanged$: Subject<ServerDeviceConfiguration> = new Subject();

  // Configs
  // ===============================================================================

  // global server side configuration file (server state) (read only)
  public serverConfig!: Config;
  public rendererConfig = signal<RendererConfigDto>(this.dtoGeneratorService.emptyRendererConfigDto());  // renderer configurations
  public serverConfigDto = signal<ServerConfigDto>(this.dtoGeneratorService.emptyServerConfigDto());  // List of server devices
  public availableBindInterfaces = signal<string[]>([]); // List of available network interfaces for UPnP bind interface configuration

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
    chatHistorySize: 50,
    databaseFilename: '',
    embeddedServerPort: 0,
    embeddedServerSslPort: 0,
    embeddedServerSslP12Keystore: '',
    embeddedServerSslP12KeystorePassword: '',
    generateUpnpCode: false,
    generateUpnpCodePath: '',
    globalSearchDelay: 0,
    libraryPath: '',
    loggingConfigFile: '',
    sseEmitterTimeout: 0,
    itemsPerPage: 100,
    nextPageAfter: 60,
    pathToRestartScript: '',
    upnpBindInterface: '',
  }

  aiConfig: AiConfig = {    // This is a DTO copy and can be used to update the AI configuration
    aiEnabled: true,
    aiSendTools: true,
    aiConversationMemory: false,
    aiProvider: '',
    aiApiKey: '',
    aiModel: '',
    aiBaseUrl: '',
    aiToolIds: '',
    aiProviderProfiles: [],
    selectedRendererUdn: '',
    selectedServerUdn: '',
  }

  // Reactive flag so OnPush components (e.g. sidebar) update immediately when AI is toggled.
  public aiEnabled = signal<boolean>(true);

  // AI providers/models offered by the backend, used to populate the settings dropdowns.
  public aiProviders = signal<string[]>([]);
  public aiModels = signal<string[]>([]);
  // Server-side tools offered by the configured OpenAI-compatible endpoint (e.g. OpenWebUI).
  public aiTools = signal<AiToolDto[]>([]);

  musicBrainzConfig: MusicbrainzSupport = {   // MusicBrainz username/password
    password: '',
    username: ''
  }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  public deviceDriverList: DeviceDriverCapability[] = [];

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
      this.getAvailableBindInterfaces();
      this.getAiProviders();
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

   private getAvailableBindInterfaces() {
    const uri = '/availableBindInterfaces';

    this.httpService.get<string[]>(this.baseUri, uri).subscribe(data => {
      if (data != undefined && data.length > 0) {
          this.availableBindInterfaces.set(['', ...data]);
      }
    });
  }
 

  private getDeviceDriverFromServer() {
    const uri = '/availableDeviceDriver';

    this.httpService.get<DeviceDriverCapability[]>(this.baseUri, uri).subscribe(data => {
      this.deviceDriverList = data;
    });
  }

  public findServerConfig(udn: string): ServerDeviceConfiguration | undefined{
    return this.serverConfigDto().serverDevices.find(d => d.mediaServer.udn === udn);
  }

  public findRendererConfig(udn: string): RendererDeviceConfiguration | undefined{
    return this.rendererConfig().rendererDevices.find(d => d.mediaRenderer.udn === udn);
  }

  public getSelectedServerConfig(udn: string): ServerDeviceConfiguration | undefined {
    return this.serverConfigDto().serverDevices.find(d => d.mediaServer.udn === udn);
  }

  public updateServerPlaylistId(udn: string, playlistId: string) {
    if (!udn) {
      return "";
    }
    var sc = this.findServerConfig(udn);
    if (sc) {
      sc.playistObjectId = playlistId;
      this.saveMediaServerConfig(sc);
    } else {
      console.log("[updateServerPlaylistId] : no server udn given.");
    }
  }

  public getServerConfig() {
    return this.serverConfigDto();
  }

  private getMediaPlayerConfig(): Subject<MediaPlayerConfigDto> {
    const uri = '/getMediaPlayerConfig';
    return this.httpService.get<MediaPlayerConfigDto>(this.baseUri, uri);
  }

  public setAlbumArtistFolder(udn: string, objectId: string) {
    const base = '/ContentDirectoryService';
    const uri = `/updateUmsAlbumArtistDirectory/${udn}`;
    this.httpService.post(base, uri, objectId).subscribe();
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
    this.httpService.postWithSuccessMessage(this.baseUri, uri, this.applicationConfig, "Save application config", "success")
      .subscribe(() => {this.applicationConfigChanged$.next(this.applicationConfig)});
  }

  public saveAiConfig(): void {
    // Snapshot the current form values into the active provider's profile so the
    // persisted profiles always reflect the latest edits.
    this.storeAiProviderProfile();
    const uri = '/saveAiConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, this.aiConfig, "Save AI config", "success")
      .subscribe(() => {
        this.aiEnabled.set(this.aiConfig.aiEnabled ?? true);
        // Refresh the available models/tools now that the (base URL / provider) is saved.
        this.listAiModels();
        this.listAiTools();
      });
  }

  // Loads the AI providers supported by the backend (for the provider dropdown).
  public getAiProviders(): void {
    const uri = '/getAiProviders';
    this.httpService.get<AiProvidersDto>(this.baseUri, uri).subscribe(data => {
      this.aiProviders.set(data?.providers ?? []);
    });
  }

  // Discards unsaved AI form edits by restoring the values from the last
  // received (persisted) server configuration and reloading the dependent lists.
  public resetAiConfig(): void {
    this.aiConfig = this.deepCopyAiConfig(this.serverConfig?.aiConfig);
    this.aiEnabled.set(this.aiConfig.aiEnabled ?? true);
    this.listAiModels();
    this.listAiTools();
  }

  // Switches the active AI provider: stores the current form values into the old
  // provider's profile, restores the new provider's profile (or sensible defaults)
  // and reloads the model/tool lists.
  public switchAiProvider(newProvider: string): void {
    this.storeAiProviderProfile();
    this.aiConfig.aiProvider = newProvider;
    this.applyAiProviderProfile(newProvider);
    this.listAiModels();
    this.listAiTools();
  }

  // Saves the current provider-specific form values into the profile list.
  private storeAiProviderProfile(): void {
    const provider = this.aiConfig.aiProvider;
    if (!provider) {
      return;
    }
    const profile: AiProviderProfile = {
      aiProvider: provider,
      aiApiKey: this.aiConfig.aiApiKey,
      aiBaseUrl: this.aiConfig.aiBaseUrl,
      aiModel: this.aiConfig.aiModel,
      aiToolIds: this.aiConfig.aiToolIds,
      aiSendTools: this.aiConfig.aiSendTools,
    };
    const profiles = this.aiConfig.aiProviderProfiles ?? [];
    const index = profiles.findIndex(p => p.aiProvider === provider);
    if (index >= 0) {
      profiles[index] = profile;
    } else {
      profiles.push(profile);
    }
    this.aiConfig.aiProviderProfiles = profiles;
  }

  // Restores the provider-specific form values from the profile list (or defaults).
  private applyAiProviderProfile(provider: string): void {
    const profile = (this.aiConfig.aiProviderProfiles ?? []).find(p => p.aiProvider === provider);
    this.aiConfig.aiApiKey = profile?.aiApiKey ?? '';
    this.aiConfig.aiBaseUrl = profile?.aiBaseUrl ?? '';
    this.aiConfig.aiModel = profile?.aiModel ?? '';
    this.aiConfig.aiToolIds = profile?.aiToolIds ?? '';
    this.aiConfig.aiSendTools = profile?.aiSendTools ?? true;
    if (provider.toLowerCase() === 'google') {
      // Google is reached via SDK/API key only - a base URL does not apply.
      this.aiConfig.aiBaseUrl = '';
    }
  }

  // Deep copy so form edits (incl. the profile list) never mutate serverConfig.
  private deepCopyAiConfig(aiConfig: AiConfig | undefined): AiConfig {
    return aiConfig ? JSON.parse(JSON.stringify(aiConfig)) : ({} as AiConfig);
  }

  // Loads the models available for the provider of the CURRENT (possibly unsaved)
  // AI form values, so the model dropdown follows provider/base URL edits.
  public listAiModels(): void {
    const uri = '/listAiModels';
    this.httpService.post<AiModelsDto>(this.baseUri, uri, this.aiConfig).subscribe(data => {
      this.aiModels.set(data?.models ?? []);
    });
  }

  // Loads the server-side tools offered by the endpoint of the CURRENT (possibly
  // unsaved) AI form values, so the checkbox list follows provider/base URL edits.
  public listAiTools(): void {
    const uri = '/listAiTools';
    this.httpService.post<AiToolsDto>(this.baseUri, uri, this.aiConfig).subscribe(data => {
      this.aiTools.set(data?.tools ?? []);
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

  public saveMediaServerConfig(mediaServerConfig: ServerDeviceConfiguration): void {
    const uri = '/saveMediaServerConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaServerConfig, "Save mediaserver config", "success").subscribe(
      () => this.serverConfigurationChanged$.next(mediaServerConfig)
    );
  }

  public deleteMediaServerConfig(mediaServerConfig: ServerDeviceConfiguration): void {
    const uri = '/deleteMediaServerConfig';
    this.httpService.postWithSuccessMessage(this.baseUri, uri, mediaServerConfig, "Delete mediaserver config", "success").subscribe();
  }

  public getMediaRendererConfig(): void {
    const uri = '/getMediaRendererConfig';
    this.httpService.get<RendererConfigDto>(this.baseUri, uri).subscribe(data => {
      this.rendererConfig.set(data);
    });
  }

  public getMediaServerConfig(): void {
    const uri = '/getMediaServerConfig';
    this.httpService.get<ServerConfigDto>(this.baseUri, uri).subscribe(data => {
      console.log("Received server config: " + JSON.stringify(data));
      if (data != undefined) { 
        this.serverConfigDto.set(data);
      }
    });
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
    console.log("Applying server config: " + JSON.stringify(data));
    if (isAssigned(data)) {
      this.serverConfigDto.set(data);
    }

    this.serverConfigDto().serverDevices = this.serverConfigDto().serverDevices.sort((n1, n2) => {
      return n1.displayString.localeCompare(n2.displayString);
    });
  }

  private applyRendererServerRendererList(data: RendererConfigDto) {
    console.log("Applying renderer config: " + JSON.stringify(data));
    if (isAssigned(data)) {
      this.rendererConfig.set(data);
    }

    this.rendererConfig().rendererDevices = this.rendererConfig().rendererDevices.sort((n1, n2) => {
      return n1.displayString.localeCompare(n2.displayString);
    });
  }

  private applyServerConfigurationFile(serverConfig: Config) {
    this.serverConfig = serverConfig;
    this.applicationConfig = Object.assign({}, serverConfig.applicationConfig);
    this.aiConfig = this.deepCopyAiConfig(serverConfig.aiConfig);
    this.aiEnabled.set(serverConfig.aiConfig?.aiEnabled ?? true);
    // Populate the model dropdown and tool list for the now-known provider/base URL.
    this.listAiModels();
    this.listAiTools();
    this.musicBrainzConfig = Object.assign({}, serverConfig.musicbrainzSupport);
    this.audioAddictConfig.set(serverConfig.audioAddictConfig);
  }

  public getRendererDevicesConfig(): RendererDeviceConfiguration[] {
    return this.rendererConfig().rendererDevices;
  }

  public isRenderDeviceUdnActive(deviceUdn: string): boolean {
    const configEntry = this.rendererConfig().rendererDevices.filter(conf => conf.mediaRenderer.udn == deviceUdn);
    if (!configEntry || configEntry.length == 0) {
      return false;
    }
    return configEntry[0].active;
  }

  public spotifyAccountConnected(): boolean {
    return this.serverConfig?.spotifyConfig?.accountConnected ? true : false;
  }
}
