import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { MediaRendererDto, PlaylistState } from '../dto';
import { MediaServerDto, UpnpAvTransportState, Config, DeviceDriverState, TrackTimeDto, TrackInfoDto, RendererConfigDto, RendererPlaylist, ToastrMessage, ServerConfigDto, ServerPlaylistDto, ServerPlaylists, InputSourceDto, InputSourceChangeDto, TransportServiceStateDto } from './../dto.d';

@Injectable({
  providedIn: 'root'
})

/**
 * Global SSE event multiplexer. Main purpose is to bridge from SSE to Angular-Observable.
 */
export class SseService {

  configChanged$: Subject<Config> = new Subject();

  // Device Discovery Events [ Added and deleted UPnP devices ]
  mediaRendererListChanged$: Subject<MediaRendererDto[]> = new Subject();
  mediaServerListChanged$: Subject<MediaServerDto[]> = new Subject();

  // Device States [Power, Volume, Input & Output Sources]
  mediaRendererDeviceDriverStateChanged$: Subject<DeviceDriverState> = new Subject();

  // Renderer States Events [track position and player states]
  mediaRendererAvTransportStateChanged$: Subject<UpnpAvTransportState> = new Subject();
  mediaRendererTransportStateChanged$: Subject<TransportServiceStateDto> = new Subject();
  mediaRendererTrackInfoChanged$: Subject<TrackInfoDto> = new Subject();
  mediaRendererPositionChanged$: Subject<TrackTimeDto> = new Subject();
  mediaRendererInputSourceChanged$: Subject<InputSourceChangeDto> = new Subject();

  // MediaServer state changes
  mediaServerPlaylistChanged$: Subject<ServerPlaylists> = new Subject();

  // Playlist Events [ playlist items removed or added. repeat / shuffle states ]
  mediaRendererPlaylistStateChanged$: Subject<PlaylistState> = new Subject();
  mediaRendererPlaylistItemsChanged$: Subject<RendererPlaylist> = new Subject();
  rendererConfigChanged$: Subject<RendererConfigDto> = new Subject();
  serverDevicesConfigChanged$: Subject<ServerConfigDto> = new Subject();

  // Toastr info messages
  toasterMessageReceived$: Subject<ToastrMessage> = new Subject();

  constructor() {
    const eventSource = new EventSource('/SSE');
    eventSource.onopen = e => console.log('opened SSE connection.');
    eventSource.onerror = e => this.processError(e);

    eventSource.onmessage = event => {
      const msg = JSON.parse(event.data);
      console.log("received unknown global message : " + msg);
    };

    eventSource.addEventListener('AVTRANSPORT_STATE', m => { this.sendNotification(this.mediaRendererAvTransportStateChanged$, m) }, false);
    eventSource.addEventListener('TRANSPORT_STATE', m => { this.sendNotification(this.mediaRendererTransportStateChanged$, m) }, false);
    eventSource.addEventListener('CONFIG_CHANGED', m => { this.sendNotification(this.configChanged$, m) }, false);

    eventSource.addEventListener('DEVICE_MEDIARENDERER_LIST_CHANGED', m => { this.sendNotification(this.mediaRendererListChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_DEVICE_DRIVER_STATE_CHANGED', m => { this.sendNotification(this.mediaRendererDeviceDriverStateChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_TRACK_INFO', m => { this.sendNotification(this.mediaRendererTrackInfoChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_TRACK_TIME', m => { this.sendNotification(this.mediaRendererPositionChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_PLAYLIST_STATE', m => { this.sendNotification(this.mediaRendererPlaylistStateChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_PLAYLIST_ITEMS', m => { this.sendNotification(this.mediaRendererPlaylistItemsChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIARENDERER_INPUT_SOURCE', m => { this.sendNotification(this.mediaRendererInputSourceChanged$, m) }, false);

    eventSource.addEventListener('DEVICE_MEDIASERVER_CHANGED', m => { this.sendNotification(this.mediaServerListChanged$, m) }, false);
    eventSource.addEventListener('DEVICE_MEDIASERVER_PLAYLIST_STATE', m => { this.sendNotification(this.mediaServerPlaylistChanged$, m) }, false);

    eventSource.addEventListener('TOASTR_INFO', m => { this.sendNotification(this.toasterMessageReceived$, m) }, false);
    eventSource.addEventListener('RENDERER_CONFIG_CHANGED', m => { this.sendNotification(this.rendererConfigChanged$, m) }, false);
    eventSource.addEventListener('SERVER_DEVICES_CONFIG_CHANGED', m => { this.sendNotification(this.serverDevicesConfigChanged$, m) }, false);

  }

  processError(e: any) {
    const event: MessageEvent = e;
    console.log("Event : " + e);
    console.log('error : source [' + event.source + "] / origin [" + event.origin + "] / data [" + event.data + "]");
  }

  sendNotification(f: Subject<any>, e: MessageEvent) {
    f.next(JSON.parse(e.data));
  }
}
