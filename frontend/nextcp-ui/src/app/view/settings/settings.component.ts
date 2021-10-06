import { SystemService } from './../../service/system.service';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RatingServiceService } from './../../service/rating-service.service';
import { UiClientConfig, MediaServerDto, RendererDeviceConfiguration, MediaRendererDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

  code: string;

  constructor(
    public ratingServiceService: RatingServiceService,
    public contentDirectoryService: ContentDirectoryService,
    public deviceService: DeviceService,
    public systemService: SystemService,
    public configService: ConfigurationService) {
  }
  /*

  ngOnInit(): void {
  
  const protocol = "web+nextcp";

  const btn = document.getElementById('spotifyLoginBtn');
  this.systemService.registerNextcp2AtSpotify(btn);

  const url = location.href;
  const arr = url.split('/');
  const result = arr[0] + '//' + arr[2]
  const target = result + '/spotifyCallback?code=%s';

  console.log("target url: " + target);
  
  navigator.registerProtocolHandler(protocol,
    target,
    "Callback");
  
}
  */

  isRendererConfigActive(deviceConfig: RendererDeviceConfiguration): boolean {
    return this.deviceService.isRenderOnline(deviceConfig.mediaRenderer);
  }

  mediaRendererChanged(event: MediaRendererDto): void {
    this.configService.clientConfig.defaultMediaRenderer = event;
  }

  mediaServerChanged(event: MediaServerDto): void {
    this.configService.clientConfig.defaultMediaServer = event;
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

  registerAppAtSpotify() : void {
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
}
