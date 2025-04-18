import { RendererService } from './../../service/renderer.service';
import { ToastService } from './../../service/toast/toast.service';
import { MyMusicService } from './../../service/my-music.service';
import { SystemService } from './../../service/system.service';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RatingServiceService } from './../../service/rating-service.service';
import {
  UiClientConfig,
  MediaServerDto,
  RendererDeviceConfiguration,
  MediaRendererDto,
  ServerDeviceConfiguration,
} from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { ChangeDetectionStrategy, Component, OnInit, model } from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import {
  MatFormFieldModule,
  MatLabel,
  MatSuffix,
} from '@angular/material/form-field';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    MatFormFieldModule,
    MatLabel,
    MatSelectModule,
    FormsModule,
    MatOptionModule,
    MatIconModule,
    MatInputModule,
    MatSuffix,
    MatButtonModule,
    MatSlideToggleModule,
    ReactiveFormsModule
  ],
})
export class SettingsComponent implements OnInit {

  spotifyCode = model<string>('');
  showOnlyActiveServer = model<boolean>(true);
  showOnlyActiveRenderer = model<boolean>(true);

  none_serverdevice: MediaServerDto = {
    udn: '',
    friendlyName: '',
    extendedApi: false,
  };
  none_renderdevice: MediaRendererDto = {
    udn: '',
    friendlyName: '',
    services: [],
    allSources: [],
    currentSource: null,
  };
  newProfileName: string;

  constructor(
    public ratingServiceService: RatingServiceService,
    public contentDirectoryService: ContentDirectoryService,
    private rendererService: RendererService,
    public deviceService: DeviceService,
    public toastService: ToastService,
    public systemService: SystemService,
    public myMusicService: MyMusicService,
    private layoutService: LayoutService,
    public configService: ConfigurationService
  ) {
    var currentLocation = window.location;
    if (this.protocolHandlerAvailable()) {
      const url = `http://${currentLocation.hostname}:${currentLocation.port}/SystemService/spotifyCallbackOAuth/%s`;
      console.log('register protocolHandler : ' + url);
      navigator.registerProtocolHandler('web+nextcp', url);
    } else {
      console.log("browser doesn't support registration of protocolHandler");
    }
  }

  ngOnInit(): void {
    this.layoutService.setFramedViewWithoutNavbar();
  }

  showAdvancedRendererSettings(
    rendererConfig: RendererDeviceConfiguration
  ): boolean {
    if (rendererConfig.deviceDriverType) {
      return true;
    } else {
      return false;
    }
  }

  public getMediaRenderer(): RendererDeviceConfiguration[] {
    if (this.showOnlyActiveRenderer) {
      return this.configService
        .getRendererDevicesConfig()
        .filter((renderer) => this.isRendererConfigActive(renderer));
    } else {
      return this.configService.getRendererDevicesConfig();
    }
  }

  public getMediaServer(): ServerDeviceConfiguration[] {
    if (this.configService?.getServerConfig()?.serverDevices) {
      if (this.showOnlyActiveServer) {
        return this.configService
          .getServerConfig()
          .serverDevices.filter((server) => this.isServerConfigActive(server));
      } else {
        return this.configService.getServerConfig().serverDevices;
      }
    } else {
      return [];
    }
  }

  protocolHandlerAvailable(): boolean {
    if (navigator.registerProtocolHandler) {
      return true;
    } else {
      return false;
    }
  }

  isRendererConfigActive(deviceConfig: RendererDeviceConfiguration): boolean {
    return this.deviceService.isRenderOnline(deviceConfig.mediaRenderer);
  }

  isServerConfigActive(deviceConfig: ServerDeviceConfiguration): boolean {
    return this.deviceService.isServerOnline(deviceConfig.mediaServer);
  }

  get buildNumber(): string {
    return this.systemService.build.buildNumber;
  }

  get buildName(): string {
    return this.systemService.build.name;
  }
  get buildTime(): string {
    return this.systemService.build.time;
  }


  activateLastFM(): void {
    this.systemService.registerNextcp2AtLastFM();
  }

  generateSession(): void {
    this.systemService.getLastFmSession();
  }

  sendSpotifyCode(): void {
    this.systemService.setSpotifyCode(this.spotifyCode());
    this.spotifyCode.set('');
  }

  registerAppAtSpotify(): void {
    this.systemService.registerNextcp2AtSpotify();
  }

  saveClientConfig(): void {
    this.configService.saveClientProfile();
  }

  deleteClientConfig(): void {
    this.configService.deleteClientProfile();
  }

  saveGeneralConfig(): void {
    this.configService.saveApplicationConfig();
  }

  saveMusicBrainzConfig(): void {
    this.configService.saveMusicBrainzConfig();
  }

  saveAudioAddict() {
    this.configService.saveAudioAddictConfig();
  }

  selectConfig(config: UiClientConfig): void {
    this.configService.selectClientConfig(config.uuid);
    if (
      !this.deviceService
        .mediaRendererList()
        .some((renderer) => renderer.udn === config.defaultMediaRenderer.udn)
    ) {
      this.toastService.info('media renderer is not available', 'ATTENTION');
    }
    if (
      !this.deviceService
        .mediaServerList()
        .some((device) => device.udn === config.defaultMediaServer.udn)
    ) {
      this.toastService.info('media server is not available', 'ATTENTION');
    }
  }

  saveRendererConfig(rendererConfig: RendererDeviceConfiguration): void {
    this.configService.saveMediaRendererConfig(rendererConfig);
  }

  deleteRendererConfig(rendererConfig: RendererDeviceConfiguration): void {
    this.configService.deleteMediaRendererConfig(rendererConfig);
  }

  initServices(rendererConfig: RendererDeviceConfiguration): void {
    this.rendererService.initServices(rendererConfig.mediaRenderer.udn);
  }

  saveServerConfig(rendererConfig: ServerDeviceConfiguration): void {
    this.configService.saveMediaServerConfig(rendererConfig);
  }

  deleteServerConfig(rendererConfig: ServerDeviceConfiguration): void {
    this.configService.deleteMediaServerConfig(rendererConfig);
  }

  restart(): void {
    this.configService.restart();
  }

  rescan(): void {
    this.contentDirectoryService.rescanContent(
      this.deviceService.selectedMediaServerDevice().udn
    );
  }

  extendedApiNotAvailable(): boolean {
    if (this.deviceService?.selectedMediaServerDevice().udn) {
      return !this.deviceService?.selectedMediaServerDevice().extendedApi;
    }
    return true;
  }
  // Client Profile configuration

  compareUdn(o1: any, o2: any): boolean {
    return o1.udn === o2.udn;
  }

  compareProfile(o1: any, o2: any): boolean {
    return o1.uuid === o2.uuid;
  }

  renameProfile(): void {
    if (this.newProfileName) {
      this.configService.getActiveClientConfig().clientName =
        this.newProfileName;
      this.newProfileName = '';
    }
  }

  noServerSelected(): void {
    this.configService.clientConfig.defaultMediaServer = this.none_serverdevice;
  }

  noRendererSelected(): void {
    this.configService.clientConfig.defaultMediaRenderer =
      this.none_renderdevice;
  }

  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice();
  }
}
