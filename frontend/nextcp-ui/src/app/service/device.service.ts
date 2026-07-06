import { HttpService } from './http.service';
import { Subject } from 'rxjs';
import { ConfigurationService } from './configuration.service';
import { SseService } from './sse/sse.service';
import {
  MediaServerDto,
  MediaRendererDto,
  InputSourceChangeDto,
  InputSourceDto,
} from './dto.d';
import { Injectable, computed, signal, inject } from '@angular/core';
import { PersistenceService } from './persistence/persistence.service';

@Injectable({
  providedIn: 'root',
})
export class DeviceService {
  private httpService = inject(HttpService);
  private persistenceService = inject(PersistenceService);
  private configService = inject(ConfigurationService);

  baseUri = '/DeviceRegistry';

  public mediaServerList = signal<MediaServerDto[]>([]);
  public mediaRendererList = signal<MediaRendererDto[]>([]);
  public selectedMediaServerDevice = signal<MediaServerDto>({
    udn: '',
    img: '',
    friendlyName: 'select Media-Server',
    extendedApi: false,
  });
  public selectedMediaRendererDevice = signal<MediaRendererDto>({
    udn: '',
    img: '',
    friendlyName: 'select Media-Renderer',
    services: [],
    allSources: [],
    currentSource: { id: 0, Name: '', Type: '', Visible: false },
  });

  public mediaRendererSelected = computed(() => {
    return this.selectedMediaRendererDevice().udn.length > 0;
  });

  public mediaServerSelected = computed(() => {
    return this.selectedMediaServerDevice().udn.length > 0;
  });

  /**
   * Synthetic renderer that plays audio in this browser (HTML5 audio) instead of on a real UPnP
   * device. It is not part of the discovered renderer list; it is offered as an extra choice in the
   * renderer selector and detected by its sentinel udn.
   */
  public readonly LOCAL_BROWSER_UDN = 'nextcp-local-browser';
  public readonly localBrowserRenderer: MediaRendererDto = {
    udn: this.LOCAL_BROWSER_UDN,
    img: '',
    friendlyName: 'This Device',
    services: [],
    allSources: [],
    currentSource: { id: 0, Name: '', Type: '', Visible: false },
  };
  public isLocalBrowserSelected = computed(
    () => this.selectedMediaRendererDevice().udn === this.LOCAL_BROWSER_UDN,
  );

  mediaRendererInitiated$: Subject<MediaRendererDto[]> = new Subject();
  mediaServerInitiated$: Subject<MediaServerDto[]> = new Subject();

  public enabledMediaRendererList = computed(() => {
    return this.mediaRendererList().filter((renderer) => {
      const enabled = this.configService.isRenderDeviceUdnActive(renderer.udn);
      return enabled;
    });
  });

  constructor() {
    const sse = inject(SseService);

    // Initialize MediaRenderer & MediaServer ...
    this.initAllMediaServer();
    this.initAllMediaRenderer();

    // push notification: We are interested in added and removed devices
    sse.mediaRendererListChanged$.subscribe((data) =>
      this.mediarendererListChanged(data),
    );
    sse.mediaServerListChanged$.subscribe((data) =>
      this.mediaserverListChanged(data),
    );

    sse.mediaRendererInputSourceChanged$.subscribe((data) =>
      this.updateRenderDeviceSource(data),
    );
  }

  // TODO check update signaling
  private updateRenderDeviceSource(source: InputSourceChangeDto) {
    console.log('new input source : ' + source);
    if (source.udn == this.selectedMediaRendererDevice().udn) {
      console.log('new input source applied');
      this.selectedMediaRendererDevice().currentSource = source.inputSource;
    }
  }

  public getCurrentInputSource(): InputSourceDto {
    return this.selectedMediaRendererDevice().currentSource;
  }

  public isRenderOnline(device: MediaRendererDto): boolean {
    if (
      this.mediaRendererList().some((renderer) => renderer.udn == device.udn)
    ) {
      return true;
    }
    return false;
  }

  public isServerOnline(device: MediaServerDto): boolean {
    if (this.mediaServerList().some((server) => server.udn == device.udn)) {
      return true;
    }
    return false;
  }

  public isMediaRendererSelected(udn: string): boolean {
    return udn === this.selectedMediaRendererDevice().udn;
  }

  public isMediaServerSelected(udn: string): boolean {
    return udn === this.selectedMediaServerDevice().udn;
  }

  public isMediaServerAvailable(udn: string): boolean {
    if (this.mediaServerList().some((e) => e.udn === udn)) {
      return true;
    }
    return false;
  }

  private mediaserverListChanged(data: MediaServerDto[]): void {
    this.mediaServerList.set(data);
    this.selectLastPersistentServerDevice();
  }

  private mediarendererListChanged(data: MediaRendererDto[]): void {
    this.mediaRendererList.set(data);
    this.selectLastPersistentRendererDevice();
    console.log('renderer list updated.');
    // this.logMediaRendererServices(data);
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

  public setMediaRendererByUdn(udn: string): boolean {
    this.persistenceService.setNewMediaRendererDevice(udn);
    if (udn === this.LOCAL_BROWSER_UDN) {
      // The local browser renderer is synthetic and not part of the discovered list.
      this.selectedMediaRendererDevice.set(this.localBrowserRenderer);
      return true;
    }
    const renderer = this.mediaRendererList().filter((e) => e.udn === udn);
    if (renderer?.length > 0) {
      // Skip re-setting the signal when the same renderer is already selected.
      // Periodic UPnP re-announcements re-fire the renderer list event; without
      // this guard every re-announcement would reset the signal to a fresh object
      // reference and needlessly re-trigger all subscribing views.
      if (this.selectedMediaRendererDevice().udn !== udn) {
        this.selectedMediaRendererDevice.set(renderer[0]);
      }
      return true;
    }
    return false;
  }

  public setMediaServerByUdn(udn: string): boolean {
    this.persistenceService.setNewMediaServerDevice(udn);
    const serverDevice = this.mediaServerList().filter((e) => e.udn === udn);
    if (serverDevice?.length > 0) {
      // Skip re-setting the signal when the same server is already selected.
      // Periodic UPnP re-announcements re-fire the media-server list event; without
      // this guard every re-announcement would reset the signal to a fresh object
      // reference and trigger a full content reload (browseChildren, getServerPlaylists,
      // getRecentServerPlaylists, ...) in all subscribing views.
      if (this.selectedMediaServerDevice().udn !== udn) {
        console.log(
          'media server was found. Setting to ' + serverDevice[0].friendlyName,
        );
        this.selectedMediaServerDevice.set(serverDevice[0]);
      }
      return true;
    } else {
      console.log('udn not yet available : ' + udn);
      return false;
    }
  }

  //
  // renderer and server initialization
  //

  private initAllMediaServer() {
    const uri = '/mediaServer';
    this.httpService
      .get<MediaServerDto[]>(this.baseUri, uri)
      .subscribe((data) => {
        this.mediaServerList.set(data);
        this.selectLastPersistentServerDevice();
        this.mediaServerInitiated$.next(data);
      });
  }

  private selectLastPersistentRendererDevice(): boolean {
    let udn = this.persistenceService.getCurrentMediaRendererDevice();
    if (!udn) {
      return false;
    }
    let b = this.setMediaRendererByUdn(udn);
    if (!b) {
      console.log('last persistence media renderer device not available yet.');
    }
    return b;
  }

  private selectLastPersistentServerDevice(): boolean {
    let udn = this.persistenceService.getCurrentMediaServerDevice();
    if (!udn) {
      return false;
    }
    let b = this.setMediaServerByUdn(udn);
    if (!b) {
      console.log('last persistence media server device not available yet.');
    }
    return b;
  }

  private initAllMediaRenderer() {
    const uri = '/mediaRenderer';

    this.httpService
      .get<MediaRendererDto[]>(this.baseUri, uri)
      .subscribe((data) => {
        this.mediaRendererList.set(data);
        this.selectLastPersistentRendererDevice();
        this.mediaRendererInitiated$.next(data);
      });
  }
}
