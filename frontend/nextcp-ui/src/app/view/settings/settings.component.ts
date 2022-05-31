import { MyMusicService } from './../../service/my-music.service';
import { SystemService } from './../../service/system.service';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { UiClientConfig, MediaServerDto, RendererDeviceConfiguration, MediaRendererDto, ServerDeviceConfiguration } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SettingsComponent {

  code: string;

  none_serverdevice: MediaServerDto = { 'udn': '', 'friendlyName': '', extendedApi: false };
  none_renderdevice: MediaRendererDto = { 'udn': '', 'friendlyName': '', services: [] };
  newProfileName: string;

  constructor(
    public ratingServiceService: RatingServiceService,
    public contentDirectoryService: ContentDirectoryService,
    public deviceService: DeviceService,
    public systemService: SystemService,
    public myMusicService: MyMusicService,
    public configService: ConfigurationService) {

    var currentLocation = window.location;
    if (this.protocolHandlerAvailable()) {
      const url = `http://${currentLocation.hostname}:${currentLocation.port}/SystemService/spotifyCallbackOAuth/%s`;
      console.log("register protocolHandler : " + url);
      navigator.registerProtocolHandler("web+nextcp", url);
    } else {
      console.log("browser doesn't support registration of protocolHandler");
    }
  }

  protocolHandlerAvailable() : boolean {
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
    const bn = this.systemService.buildVersion;
    return bn;
  }

  activateLastFM(): void {
    this.systemService.registerNextcp2AtLastFM();
  }

  generateSession(): void {
    this.systemService.getLastFmSession();
  }

  sendSpotifyCode(): void {
    this.systemService.setSpotifyCode(this.code);
    this.code = "";
  }

  registerAppAtSpotify(): void {
    this.systemService.registerNextcp2AtSpotify();
  }

  saveClientConfig(): void {
    this.configService.saveClientProfile();
  }

  saveGeneralConfig(): void {
    this.configService.saveApplicationConfig();
  }

  saveMusicBrainzConfig(): void {
    this.configService.saveMusicBrainzConfig();
  }

  selectConfig(config: UiClientConfig): void {
    this.configService.selectClientConfig(config.uuid);
  }

  saveRendererConfig(rendererConfig: RendererDeviceConfiguration): void {
    this.configService.saveMediaRendererConfig(rendererConfig);
  }

  deleteRendererConfig(rendererConfig: RendererDeviceConfiguration): void {
    this.configService.deleteMediaRendererConfig(rendererConfig);
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
    this.contentDirectoryService.rescanContent(this.deviceService.selectedMediaServerDevice.udn);
  }

  extendedApiNotAvailable(): boolean {
    if (this.deviceService?.selectedMediaServerDevice.udn) {
      return !this.deviceService?.selectedMediaServerDevice?.extendedApi;
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
      this.configService.getActiveClientConfig().clientName = this.newProfileName;
      this.newProfileName = "";
    }
  }

  noServerSelected(): void {
    this.configService.clientConfig.defaultMediaServer = this.none_serverdevice;
  }

  noRendererSelected(): void {
    this.configService.clientConfig.defaultMediaRenderer = this.none_renderdevice;
  }


  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice;
  }
}
