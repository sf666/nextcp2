import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { ConfigurationService } from './configuration.service';
import { SseService } from './sse/sse.service';
import { MediaServerDto, MediaRendererDto, UiClientConfig } from './dto.d';
import { Injectable } from '@angular/core';
import { enableDebugTools } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  baseUri = '/DeviceRegistry';

  public mediaServerList: MediaServerDto[] = [];
  public mediaRenderList: MediaRendererDto[] = [];

  private defaultMediaRendererAlreadySelected = false;
  private defaultMediaServerAlreadySelected = false;

  private _selectedMediaServerDevice: MediaServerDto = { udn: '', friendlyName: 'please select Media-Server' };
  private _selectedMediaRendererDevice: MediaRendererDto = { udn: '', friendlyName: 'please select Media-Renderer' };

  mediaRendererChanged$: Subject<MediaRendererDto> = new Subject();
  mediaServerChanged$: Subject<MediaServerDto> = new Subject();

  constructor(
    // class services
    sse: SseService,

    // instance services
    private httpService: HttpService,
    private configService: ConfigurationService) {

    // Initialize MediaRenderer & MediaServer ...
    this.getAllMediaServer();
    this.getAllMediaRenderer();

    // push notification: We are interested in added and removed devices
    configService.clientConfigChanged$.subscribe(data => this.clientConfigChanged(data));
    sse.mediaRendererListChanged$.subscribe(data => this.mediarendererListChanged(data));
    sse.mediaServerListChanged$.subscribe(data => this.mediaserverListChanged(data));
  }

  public isMediaRendererSelected(udn: string): boolean {
    return udn === this._selectedMediaRendererDevice.udn;
  }

  private mediaserverListChanged(data: MediaServerDto[]): void {
    this.mediaServerList = data;
    this.applyDefaultServer();
  }

  private mediarendererListChanged(data: MediaRendererDto[]): void {
    this.mediaRenderList = data;
    this.applyDefaultRenderer();
  }

  private clientConfigChanged(data: UiClientConfig): void {
    this.applyDefaultRenderer();
    this.applyDefaultServer();
  }

  private applyDefaultServer() {
    if (!this.defaultMediaServerAlreadySelected && this.mediaServerList.some(e => e.udn === this.configService.clientConfig.defaultMediaServer.udn)) {
      console.log("selecting default media server ... ");
      this.defaultMediaServerAlreadySelected = true;
      this.selectedMediaServerDevice = this.configService.clientConfig.defaultMediaServer;
    }
  }

  private applyDefaultRenderer() {
    if (!this.defaultMediaRendererAlreadySelected && this.mediaRenderList.some(e => e.udn === this.configService.clientConfig.defaultMediaRenderer.udn)) {
      console.log("selecting default media renderer ... ");
      this.defaultMediaRendererAlreadySelected = true;
      this.selectedMediaRendererDevice = this.configService.clientConfig.defaultMediaRenderer;
    }
  }

  public getMediaRendererList(): MediaRendererDto[] {
    return this.mediaRenderList;
  }

  public getEnabledMediaRendererList(): MediaRendererDto[] {
    return this.mediaRenderList.filter(renderer => { 
      var enabled : boolean;
      enabled = this.configService.isRenderDeviceActive(renderer.udn) 
      return enabled;
    });
  }

  public getMediaServerList(): MediaServerDto[] {
    return this.mediaServerList;
  }

  public get selectedMediaServerDevice(): MediaServerDto {
    return this._selectedMediaServerDevice;
  }

  public set selectedMediaServerDevice(device: MediaServerDto) {
    this._selectedMediaServerDevice = device;
    this.mediaServerChanged$.next(device);
  }

  public get selectedMediaRendererDevice(): MediaRendererDto {
    return this._selectedMediaRendererDevice;
  }

  public set selectedMediaRendererDevice(device: MediaRendererDto) {
    this._selectedMediaRendererDevice = device;
    this.mediaRendererChanged$.next(device);
  }

  public setMediaRendererByUdn(udn: string) {
    let renderer = this.mediaRenderList.filter(e => e.udn === udn);
    if (renderer.length > 0) {
      this.selectedMediaRendererDevice = renderer[0];
    }
  }

  public setMediaServerByUdn(udn: string) {
    let serverDevice = this.mediaServerList.filter(e => e.udn === udn);
    if (serverDevice.length > 0) {
      this.selectedMediaServerDevice = serverDevice[0];
    }
  }

  private getAllMediaServer() {
    const uri = '/mediaServer';

    this.httpService.get<MediaServerDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaServerList = data;
      this.applyDefaultServer();
    });
  }

  private getAllMediaRenderer() {
    const uri = '/mediaRenderer';

    this.httpService.get<MediaRendererDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaRenderList = data;
      this.applyDefaultRenderer();
    });
  }
}
