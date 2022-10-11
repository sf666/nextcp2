import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { ConfigurationService } from './configuration.service';
import { SseService } from './sse/sse.service';
import { MediaServerDto, MediaRendererDto, UiClientConfig, RendererDeviceConfiguration, InputSourceChangeDto } from './dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  baseUri = '/DeviceRegistry';

  public mediaServerList: MediaServerDto[] = [];
  public mediaRenderList: MediaRendererDto[] = [];

  private defaultMediaRendererAlreadySelected = false;
  private defaultMediaServerAlreadySelected = false;

  private _selectedMediaServerDevice: MediaServerDto = { udn: '', friendlyName: 'please select Media-Server', extendedApi: false };
  private _selectedMediaRendererDevice: MediaRendererDto = { udn: '', friendlyName: 'please select Media-Renderer', services: [], allSources: [], currentSource: null };

  mediaRendererChanged$: Subject<MediaRendererDto> = new Subject();
  mediaServerChanged$: Subject<MediaServerDto> = new Subject();
  mediaRendererInitiated$: Subject<MediaRendererDto[]> = new Subject();
  mediaServerInitiated$: Subject<MediaServerDto[]> = new Subject();

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

    sse.mediaRendererInputSourceChanged$.subscribe(data => this.updateRenderDeviceSource(data));
  }

  private updateRenderDeviceSource(source: InputSourceChangeDto) {
    if (source.udn == this._selectedMediaRendererDevice.udn) {
      this._selectedMediaRendererDevice.currentSource = source.inputSource;
    }    
  }

  public isRenderOnline(device: MediaRendererDto): boolean {
    if (this.mediaRenderList.some(renderer => renderer.udn == device.udn)) {
      return true;
    }
    return false;
  }

  public isServerOnline(device: MediaServerDto): boolean {
    if (this.mediaServerList.some(server => server.udn == device.udn)) {
      return true;
    }
    return false;
  }


  public isMediaRendererSelected(udn: string): boolean {
    return udn === this._selectedMediaRendererDevice.udn;
  }

  public isMediaServerSelected(udn: string): boolean {
    return udn === this._selectedMediaServerDevice.udn;
  }

  public isMediaServerAvailable(udn: string): boolean {
    if (this.mediaServerList.some(e => e.udn === udn)) {
      return true;
    }
    return false;
  }

  public isAnyMediaRendererSelected(): boolean {
    return this._selectedMediaRendererDevice.udn.length > 0;
  }

  private mediaserverListChanged(data: MediaServerDto[]): void {
    this.mediaServerList = data;
    this.applyDefaultServer();
  }

  private mediarendererListChanged(data: MediaRendererDto[]): void {
    this.mediaRenderList = data;
    this.applyDefaultRenderer();
    console.log("renderer list updated.");
    this.logMediaRendererServices(data);
  }

  private clientConfigChanged(data: UiClientConfig): void {
    this.applyDefaultRenderer();
    this.applyDefaultServer();
  }

  private applyDefaultServer() {
    if (!this.defaultMediaServerAlreadySelected && this.mediaServerList.some(e => e.udn === this.configService.clientConfig.defaultMediaServer.udn)) {
      console.log("selecting default media server to " + this.configService.clientConfig.defaultMediaServer.friendlyName);
      this.defaultMediaServerAlreadySelected = true;
      this.selectedMediaServerDevice = this.configService.clientConfig.defaultMediaServer;
    }
  }

  private applyDefaultRenderer() {
    if (!this.defaultMediaRendererAlreadySelected && this.mediaRenderList.some(e => e.udn === this.configService.clientConfig.defaultMediaRenderer.udn)) {
      console.log("selecting default media renderer to " + this.configService.clientConfig.defaultMediaRenderer.friendlyName);
      this.defaultMediaRendererAlreadySelected = true;
      this.selectedMediaRendererDevice = this.configService.clientConfig.defaultMediaRenderer;
    }
  }

  public getMediaRendererList(): MediaRendererDto[] {
    return this.mediaRenderList;
  }

  public getEnabledMediaRendererList(): MediaRendererDto[] {
    return this.mediaRenderList.filter(renderer => {
      const enabled = this.configService.isRenderDeviceUdnActive(renderer.udn);
      return enabled;
    });
  }

  public getMediaServerList(): MediaServerDto[] {
    return this.mediaServerList;
  }

  public get selectedMediaServerDevice(): MediaServerDto {
    return this._selectedMediaServerDevice;
  }

  public get selectedMediaServerDeviceHasExtendedApi(): boolean {
    if (this._selectedMediaServerDevice) {
      return this._selectedMediaServerDevice.extendedApi;
    }
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
//    this.logMediaRendererDeviceServices(device);
  }

  public setMediaRendererByUdn(udn: string): void {
    const renderer = this.mediaRenderList.filter(e => e.udn === udn);
    if (renderer.length > 0) {
      this.selectedMediaRendererDevice = renderer[0];
    }
  }

  public setMediaServerByUdn(udn: string): void {
    const serverDevice = this.mediaServerList.filter(e => e.udn === udn);
    if (serverDevice.length > 0) {
      this.selectedMediaServerDevice = serverDevice[0];
    }
  }

  //
  // renderer and server initialization
  //

  private getAllMediaServer() {
    const uri = '/mediaServer';

    this.httpService.get<MediaServerDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaServerList = data;
      this.applyDefaultServer();
      this.mediaServerInitiated$.next(data);
    });
  }

  private getAllMediaRenderer() {
    const uri = '/mediaRenderer';

    this.httpService.get<MediaRendererDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaRenderList = data;
      this.applyDefaultRenderer();
      this.mediaRendererInitiated$.next(data);

      this.logMediaRendererServices(data);
    });
  }

  private logMediaRendererDeviceServices(renderer: MediaRendererDto): void {
    if (renderer) {
      console.log("available services for device : " + renderer.friendlyName);
      renderer.services?.forEach(service => console.log(service.namespace + " " + service.serviceName + " " + service.version))
    }
  }

  private logMediaRendererServices(rendererList: MediaRendererDto[]): void {
    if (rendererList) {
      rendererList.forEach(renderer => {
        this.logMediaRendererDeviceServices(renderer);
      });
    }
  }
}
