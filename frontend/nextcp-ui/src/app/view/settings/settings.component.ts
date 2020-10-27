import { UiClientConfig, MediaServerDto, RendererDeviceConfiguration } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component } from '@angular/core';

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

  constructor(public configService: ConfigurationService) { }

  mediaRendererChanged(event) {
    this.configService.clientConfig.defaultMediaRenderer = event;
  }

  mediaServerChanged(event) {
    this.configService.clientConfig.defaultMediaServer = event;
  }

  save() {
    this.configService.saveClientConfig();
  }

  selectConfig(config: UiClientConfig) {
    this.configService.selectClientConfig(config.uuid);
  }

  saveRendererConfig(rendererConfig: RendererDeviceConfiguration) {
    this.configService.saveMediaRendererConfig(rendererConfig);
  }

  deleteRendererConfig(rendererConfig: RendererDeviceConfiguration) {
    this.configService.deleteMediaRendererConfig(rendererConfig);
  }

  restart() {
    this.configService.restart();
  }
}
