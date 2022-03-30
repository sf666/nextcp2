import { MyMusicService } from './../../service/my-music.service';
import { SystemService } from './../../service/system.service';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { UiClientConfig, MediaServerDto, RendererDeviceConfiguration, MediaRendererDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component } from '@angular/core';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

  code: string;

  none_device: MediaServerDto = { 'udn': '', 'friendlyName': '', extendedApi: false };
  newProfileName: string;

  constructor(
    public ratingServiceService: RatingServiceService,
    public contentDirectoryService: ContentDirectoryService,
    public deviceService: DeviceService,
    public systemService: SystemService,
    public myMusicService: MyMusicService,
    public configService: ConfigurationService) {
  }

  isRendererConfigActive(deviceConfig: RendererDeviceConfiguration): boolean {
    return this.deviceService.isRenderOnline(deviceConfig.mediaRenderer);
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

  save(): void {
    this.configService.saveClientConfig();
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
    this.configService.clientConfig.defaultMediaServer = this.none_device;
  }

  noRendererSelected(): void {
    this.configService.clientConfig.defaultMediaRenderer = this.none_device;
  }


  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice;
  }
}
