import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
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
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  signal,
  inject,
} from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { LayoutService } from 'src/app/service/layout.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AlertComponent } from 'src/app/comp/alert/alert.component';

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
  providers: [
    { provide: ContentDirectoryService, useClass: ContentDirectoryService },
  ], // non singleton injections
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
    FormField,
  ],
})
export class SettingsComponent implements OnInit {
  ratingServiceService = inject(RatingServiceService);
  contentDirectoryService = inject(ContentDirectoryService);
  private rendererService = inject(RendererService);
  deviceService = inject(DeviceService);
  toastService = inject(ToastService);
  systemService = inject(SystemService);
  myMusicService = inject(MyMusicService);
  private layoutService = inject(LayoutService);
  configService = inject(ConfigurationService);
  dtoGeneratorService = inject(DtoGeneratorService);

  amplifierInfoRendererUdn: string | null = null;

  // Open state of the AI model combobox (free text + full clickable list).
  modelDropdownOpen = signal(false);

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
    currentSource: this.dtoGeneratorService.emptyInputSourceDto(),
  };

  constructor() {
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
    // Populate the model dropdown and server-side tool list for the current AI configuration.
    this.configService.listAiModels();
    this.configService.listAiTools();
  }

  showAdvancedRendererSettings(
    rendererConfig: RendererDeviceConfiguration,
  ): boolean {
    if (rendererConfig.deviceDriverType) {
      return true;
    } else {
      return false;
    }
  }

  toggleAmplifierInfo(rendererUdn: string): void {
    this.amplifierInfoRendererUdn =
      this.amplifierInfoRendererUdn === rendererUdn ? null : rendererUdn;
  }

  isAmplifierInfoOpen(rendererUdn: string): boolean {
    return this.amplifierInfoRendererUdn === rendererUdn;
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
    const protocolHandler =
      window.isSecureContext &&
      typeof (navigator as Navigator & { registerProtocolHandler?: unknown })
        .registerProtocolHandler === 'function';
    return protocolHandler;
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

  saveAiConfig(): void {
    this.configService.saveAiConfig();
  }

  // Discards unsaved AI form edits and restores the persisted configuration.
  resetAiConfig(): void {
    this.configService.resetAiConfig();
  }

  // Provider options from the backend, including the currently configured value
  // (so a legacy/custom provider is never silently dropped from the dropdown).
  aiProviderOptions(): string[] {
    return this.mergeCurrent(
      this.configService.aiProviders(),
      this.configService.aiConfig.aiProvider,
    );
  }

  // Model options for the current provider, including the currently configured model.
  aiModelOptions(): string[] {
    return this.mergeCurrent(
      this.configService.aiModels(),
      this.configService.aiConfig.aiModel,
    );
  }

  // Options shown in the open combobox panel. Shows the full list when the field
  // is empty or holds an exact selection; filters by substring while typing a new
  // term; falls back to the full list when nothing matches (free text).
  filteredModelOptions(): string[] {
    const all = this.aiModelOptions();
    const current = (this.configService.aiConfig.aiModel ?? '')
      .trim()
      .toLowerCase();
    if (!current || all.some((o) => o.toLowerCase() === current)) {
      return all;
    }
    const filtered = all.filter((o) => o.toLowerCase().includes(current));
    return filtered.length > 0 ? filtered : all;
  }

  openModelDropdown(): void {
    this.modelDropdownOpen.set(true);
  }

  closeModelDropdown(): void {
    this.modelDropdownOpen.set(false);
  }

  toggleModelDropdown(): void {
    this.modelDropdownOpen.update((open) => !open);
  }

  // Picks a model from the list (free text in the input stays possible).
  selectModel(model: string): void {
    this.configService.aiConfig.aiModel = model;
    this.modelDropdownOpen.set(false);
  }

  // Applies an AI provider change: the old provider's form values are kept in its
  // profile, the new provider's profile is restored and the lists are reloaded.
  onAiProviderChange(provider: string): void {
    this.configService.switchAiProvider(provider);
  }

  // Whether the Google provider is selected (base URL does not apply then).
  isGoogleProvider(): boolean {
    return (
      (this.configService.aiConfig.aiProvider ?? '').toLowerCase() === 'google'
    );
  }

  // Reloads the model/tool lists, e.g. after the base URL or API key was edited.
  reloadAiTools(): void {
    this.configService.listAiModels();
    this.configService.listAiTools();
  }

  // Whether the given server-side tool is part of the configured aiToolIds.
  // The wildcard '*' selects every tool offered by the server.
  isAiToolSelected(toolId: string): boolean {
    const value = (this.configService.aiConfig.aiToolIds ?? '').trim();
    if (value === '*') {
      return true;
    }
    return this.parseAiToolIds(value).includes(toolId);
  }

  // Adds or removes a tool id from aiToolIds. A configured wildcard is
  // materialized into the explicit list of all server tools first.
  toggleAiTool(toolId: string, checked: boolean): void {
    const value = (this.configService.aiConfig.aiToolIds ?? '').trim();
    let selected =
      value === '*'
        ? this.configService.aiTools().map((t) => t.id)
        : this.parseAiToolIds(value);

    if (checked && !selected.includes(toolId)) {
      selected = [...selected, toolId];
    } else if (!checked) {
      selected = selected.filter((id) => id !== toolId);
    }
    this.configService.aiConfig.aiToolIds = selected.join(',');
  }

  private parseAiToolIds(value: string): string[] {
    return value
      .split(',')
      .map((id) => id.trim())
      .filter((id) => id.length > 0);
  }

  private mergeCurrent(
    list: string[],
    current: string | null | undefined,
  ): string[] {
    const result = [...(list ?? [])];
    if (current && !result.includes(current)) {
      result.unshift(current);
    }
    return result;
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
      this.deviceService.selectedMediaServerDevice().udn,
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
