import { RendererService } from './../../service/renderer.service';
import { ToastService } from './../../service/toast/toast.service';
import { MyMusicService } from './../../service/my-music.service';
import { SystemService } from './../../service/system.service';
import { DeviceService } from './../../service/device.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { RatingServiceService } from './../../service/rating-service.service';
import {
  MediaServerDto,
  RendererDeviceConfiguration,
  MediaRendererDto,
  ServerDeviceConfiguration,
} from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { form, Field } from '@angular/forms/signals';
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
} from '@angular/material/form-field';
import { AlertComponent } from "src/app/comp/alert/alert.component";

interface SettingsFormModel {
  spotifyCode: string;
  showOnlyActiveServer: boolean;
  showOnlyActiveRenderer: boolean;
}

@Component({
  selector: 'settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{ provide: ContentDirectoryService, useClass: ContentDirectoryService },], // non singleton injections
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    MatOptionModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    AlertComponent,
    Field
  ],
})
export class SettingsComponent implements OnInit {

  // Signal Forms Setup
  settingsModel = signal<SettingsFormModel>({
    spotifyCode: '',
    showOnlyActiveServer: true,
    showOnlyActiveRenderer: true,
  });

  settingsForm = form(this.settingsModel);

  none_serverdevice: MediaServerDto = {
    img: '',
    udn: '',
    friendlyName: '',
    extendedApi: false,
  };
  none_renderdevice: MediaRendererDto = {
    img: '',
    udn: '',
    friendlyName: '',
    services: [],
    allSources: [],
    currentSource: null,
  };

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

  showAdvancedRendererSettings(rendererConfig: RendererDeviceConfiguration): boolean {
    if (rendererConfig.deviceDriverType) {
      return true;
    } else {
      return false;
    }
  }

  public getMediaRenderer(): RendererDeviceConfiguration[] {
    if (this.settingsForm.showOnlyActiveRenderer().value()) {
      return this.configService
        .getRendererDevicesConfig()
        .filter((renderer) => this.isRendererConfigActive(renderer));
    } else {
      return this.configService.getRendererDevicesConfig();
    }
  }

  public getMediaServerConfig(): ServerDeviceConfiguration[] {
    if (this.configService?.getServerConfig()?.serverDevices) {
      if (this.settingsForm.showOnlyActiveServer().value()) {
        return this.configService.getServerConfig()
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
    this.systemService.setSpotifyCode(this.settingsForm.spotifyCode().value());
  }

  registerAppAtSpotify(): void {
    this.systemService.registerNextcp2AtSpotify();
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

  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice();
  }
}
