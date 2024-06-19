import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { ConfigurationService } from './configuration.service';
import { SseService } from './sse/sse.service';
import { MediaServerDto, MediaRendererDto, UiClientConfig, RendererDeviceConfiguration, InputSourceChangeDto, TransportServiceStateDto, InputSourceDto } from './dto.d';
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DeviceService {

  // member
  // ============================================================

  private baseUri = '/DeviceRegistry';
  private defaultMediaRendererAlreadySelected = false;
  private defaultMediaServerAlreadySelected = false;

  // Signals 
  // ============================================================
  public mediaServerList = signal<MediaServerDto[]>([]);
  public mediaRendererList = signal<MediaRendererDto[]>([]);
  public selectedMediaServerDevice = signal<MediaServerDto>({ udn: '', friendlyName: 'please select Media-Server', extendedApi: false });
  public selectedMediaRendererDevice = signal<MediaRendererDto>({ udn: '', friendlyName: 'please select Media-Renderer', services: [], allSources: [], currentSource: null });

  constructor(
    // class services
    sse: SseService,

    // instance services
    private httpService: HttpService,
    private configService: ConfigurationService) {

    // Initialize MediaRenderer & MediaServer ...
    this.initAllMediaServer();
    this.initAllMediaRenderer();

    // push notification: We are interested in added and removed devices
    configService.clientConfigChanged$.subscribe(data => this.clientConfigChanged(data));
    sse.mediaRendererListChanged$.subscribe(data => this.mediarendererListChanged(data));
    sse.mediaServerListChanged$.subscribe(data => this.mediaserverListChanged(data));

    sse.mediaRendererInputSourceChanged$.subscribe(data => this.updateRenderDeviceSource(data));
  }

  // TODO check update signaling
  private updateRenderDeviceSource(source: InputSourceChangeDto) {
    console.log("new input source : " + source)
    if (source.udn == this.selectedMediaRendererDevice().udn) {
      console.log("new input source applied");
      this.selectedMediaRendererDevice().currentSource = source.inputSource;
    }
  }

  public getCurrentInputSource(): InputSourceDto {
    return this.selectedMediaRendererDevice().currentSource;
  }

  public isRenderOnline(device: MediaRendererDto): boolean {
    if (this.mediaRendererList().some(renderer => renderer.udn == device.udn)) {
      return true;
    }
    return false;
  }

  public isServerOnline(device: MediaServerDto): boolean {
    if (this.mediaServerList().some(server => server.udn == device.udn)) {
      return true;
    }
    return false;
  }


  public isMediaRendererSelected(udn: string): boolean {
    return udn === this.selectedMediaRendererDevice().udn;
  }

  get noMediaRendererSelected() : boolean {
    return this.selectedMediaRendererDevice().udn === '';
  }

  public isMediaServerSelected(udn: string): boolean {
    return udn === this.selectedMediaServerDevice().udn;
  }

  public isMediaServerAvailable(udn: string): boolean {
    if (this.mediaServerList().some(e => e.udn === udn)) {
      return true;
    }
    return false;
  }

  public isAnyMediaRendererSelected(): boolean {
    return this.selectedMediaRendererDevice().udn?.length > 0;
  }

  private mediaserverListChanged(data: MediaServerDto[]): void {
    this.mediaServerList.set(data);
    this.applyDefaultServer();
  }

  private mediarendererListChanged(data: MediaRendererDto[]): void {
    this.mediaRendererList.set(data);
    this.applyDefaultRenderer();
    console.log("renderer list updated.");
    // this.logMediaRendererServices(data);
  }

  private clientConfigChanged(data: UiClientConfig): void {
    this.applyDefaultRenderer();
    this.applyDefaultServer();
  }

  private applyDefaultServer() {
    const defServer = this.mediaServerList().filter(e => e.udn === this.configService.clientConfig.defaultMediaServer.udn).find(e => true);
    if (!this.defaultMediaServerAlreadySelected && defServer) {
      console.log("selecting default media server to " + defServer.friendlyName);
      this.defaultMediaServerAlreadySelected = true;
      this.selectedMediaServerDevice.set(defServer);
    }
  }

  private applyDefaultRenderer() {
    const defRenderer = this.mediaRendererList().filter(e => e.udn === this.configService.clientConfig.defaultMediaRenderer.udn).find(e => true);;
    if (!this.defaultMediaRendererAlreadySelected && defRenderer) {
      console.log("selecting default media renderer : " + this.configService.clientConfig.defaultMediaRenderer.friendlyName);
      this.defaultMediaRendererAlreadySelected = true;
      this.selectedMediaRendererDevice.set(defRenderer);
    }
  }

  public getEnabledMediaRendererList(): MediaRendererDto[] {
    return this.mediaRendererList().filter(renderer => {
      const enabled = this.configService.isRenderDeviceUdnActive(renderer.udn);
      return enabled;
    });
  }


  public get selectedMediaServerDeviceHasExtendedApi(): boolean {
    return this.selectedMediaServerDevice().extendedApi;
  }

  public setActiveMediaServerDevice(device: MediaServerDto) {
    this.selectedMediaServerDevice.set(device);
  }

  public selectMediaRendererDevice(device: MediaRendererDto) {
    this.selectedMediaRendererDevice.set(device);
  }

  public setMediaRendererByUdn(udn: string): void {
    const renderer = this.mediaRendererList().filter(e => e.udn === udn);
    if (renderer?.length > 0) {
      this.selectedMediaRendererDevice.set(renderer[0]);
    }
  }

  public setMediaServerByUdn(udn: string): void {
    const serverDevice = this.mediaServerList().filter(e => e.udn === udn);
    if (serverDevice?.length > 0) {
      this.selectedMediaServerDevice.set(serverDevice[0]);
    }
  }

  //
  // renderer and server initialization
  //

  private initAllMediaServer() {
    const uri = '/mediaServer';
    this.httpService.get<MediaServerDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaServerList.set(data);
      this.applyDefaultServer();
    });
  }

  private initAllMediaRenderer() {
    const uri = '/mediaRenderer';

    this.httpService.get<MediaRendererDto[]>(this.baseUri, uri).subscribe(data => {
      this.mediaRendererList.set(data);
      this.applyDefaultRenderer();
    });
  }

  // for debugging
  private logMediaRendererDeviceServices(renderer: MediaRendererDto): void {
    if (renderer) {
      console.log("available services for device : " + renderer.friendlyName);
      renderer.services?.forEach(service => console.log(service.namespace + " " + service.serviceName + " " + service.version))
    }
  }

  private logMediaRendererServices(rendererList: MediaRendererDto[]): void {
    if (rendererList) {
      rendererList?.forEach(renderer => {
        this.logMediaRendererDeviceServices(renderer);
      });
    }
  }
}
