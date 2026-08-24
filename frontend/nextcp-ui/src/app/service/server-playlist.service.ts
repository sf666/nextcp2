import { ConfigurationService } from 'src/app/service/configuration.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { Injectable, computed, signal, inject } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { CreateServerPlaylistVO, ServerDeleteObjectRequest, ServerPlaylistEntry, ServerPlaylists } from './dto';
import { HttpService } from './http.service';
import { SseService } from './sse/sse.service';
import { DeviceService } from './device.service';

/**
 * What changed on the media server, so a listening view can tell whether it is
 * showing it. Both fields are optional: on create we know the parent container,
 * on destroy only the object that is gone.
 */
export interface PlaylistStructureChange {
  /** Container a new playlist was created in. */
  containerId?: string;
  /** Object (playlist or playlist entry) that was destroyed. */
  removedObjectId?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ServerPlaylistService {
  private httpService = inject(HttpService);
  private configurationService = inject(ConfigurationService);
  private deviceService = inject(DeviceService);

  baseUri = '/MediaServerPlaylistService';

  playlistLoading = signal<boolean>(true);

  /**
   * Fires whenever a playlist was created or an object destroyed on the media
   * server. A browse result is a snapshot, so any view currently showing the
   * affected container is stale from this moment on — it has to browse again to
   * see the new playlist. Only the sidebar list is refetched here; the browse
   * views listen for this (see ContentDirectoryService).
   */
  public playlistStructureChanged$ = new Subject<PlaylistStructureChange>();

  recentServerPl = signal<ServerPlaylists>({
    mediaServerUdn: '',
    containerId: '',
    serverPlaylists: [],
  });

  // Default server based playlists
  serverPl = signal<ServerPlaylists>({
    mediaServerUdn: '',
    containerId: '',
    serverPlaylists: [],
  });

  serverPlPlaylistIds = computed(() => {
    const ids: string[] = [];
    this.serverPl().serverPlaylists?.forEach((element) => {
      ids.push(element.playlistId);
    });
    return ids;
  });

  selectedMediaServer = computed(() => {
    return this.deviceService.selectedMediaServerDevice();
  });

  constructor() {
    const configurationService = this.configurationService;
    const deviceService = this.deviceService;
    const sseService = inject(SseService);

    // reconstruct sidebar after potential playlist folder change
    configurationService.serverConfigurationChanged$.subscribe(
      (mediaServerConfig) => {
        if (
          mediaServerConfig.mediaServer.udn === this.selectedMediaServer().udn
        ) {
          this.afterMediaServerChanged();
        }
      },
    );

    toObservable(this.selectedMediaServer).subscribe((data) =>
      this.afterMediaServerChanged(),
    );

    sseService.mediaServerPlaylistChanged$.subscribe((data) => {
      if (deviceService.isMediaServerSelected(data.mediaServerUdn)) {
        this.serverPl.set(data);
      }
    });

    sseService.mediaServerRecentPlaylistChanged$.subscribe((data) => {
      console.log('updating recently used playlists ... ');
      this.recentServerPl.set(data);
    });
  }

  private afterMediaServerChanged() {
    this.updateServerAccessiblePlaylists();
    this.updateRecentServerAccessiblePlaylists();
  }

  //
  // Playlists located in the configured folder name
  //
  public updateServerAccessiblePlaylists() {
    if (this.selectedMediaServer().udn.length > 0) {
      this.playlistLoading.set(true);
      const uri = '/getServerPlaylists';
      this.httpService
        .post<ServerPlaylists>(
          this.baseUri,
          uri,
          this.selectedMediaServer().udn,
        )
        .subscribe({
          next: (data) => {
            this.serverPl.set(data);
            this.playlistLoading.set(false);
          },
          // Without this the sidebar keeps saying "loading playlist ..." forever after a single
          // failed request, and the app looks disconnected although nothing else is broken.
          error: () => this.playlistLoading.set(false),
        });
    } else {
      console.log(
        'updateServerAccessiblePlaylists : skipping, no server selected ...',
      );
    }
  }

  public updateRecentServerAccessiblePlaylists() {
    if (this.selectedMediaServer().udn) {
      const uri = '/getRecentServerPlaylists';
      this.httpService
        .post<ServerPlaylists>(
          this.baseUri,
          uri,
          this.selectedMediaServer().udn,
        )
        .subscribe({
          next: (data) => this.recentServerPl.set(data),
          // The toast from HttpService is the user facing part, this only keeps the failure from
          // bubbling out as an unhandled rejection.
          error: () => {},
        });
    } else {
      console.log(
        'updateRecentServerAccessiblePlaylists : skipping, no server selected ...',
      );
    }
  }

  public playlistIdExistsInServerPlaylists(id: string): boolean {
    return this.serverPlPlaylistIds().indexOf(id) > -1;
  }

  //
  // Filesystem Playlist actions (MediaServer actions)
  // ========================================================================

  public createPlaylist(
    playlistName: string,
    containerId: string,
  ): Observable<string> {
    const createPL: CreateServerPlaylistVO = {
      containerId: containerId,
      mediaServerUdn: this.selectedMediaServer().udn,
      playlistName: playlistName + '.m3u8',
    };
    const uri = '/createPlaylist';
    let ret = this.httpService.post<string>(this.baseUri, uri, createPL);
    ret.subscribe((newId) => {
      // An empty id means the server refused the create and already reported
      // why — nothing changed, so nothing to refresh.
      if (!newId) {
        return;
      }
      this.updateServerAccessiblePlaylists();
      this.playlistStructureChanged$.next({ containerId: containerId });
    });
    return ret;
  }

  public addSongToServerPlaylist(
    songId: string,
    playlistId: string,
  ): Observable<any> {
    const uri = '/addToServerPlaylist'; // void return
    const req: ServerPlaylistEntry = {
      serverUdn: this.selectedMediaServer().udn,
      songObjectId: songId,
      playlistObjectId: playlistId,
    };
    let ret = this.httpService.post(this.baseUri, uri, req);
    ret.subscribe();
    return ret;
  }

  /**
   * Deletes an object on the media server device — a playlist, or a single entry
   * within one.
   *
   * @param objectId object id as delivered by the content directory
   */
  public deleteObject(objectId: string): Observable<any> {
    const uri = '/deleteObject';
    const req: ServerDeleteObjectRequest = {
      serverUdn: this.selectedMediaServer().udn,
      objectId: objectId,
    };
    let ret = this.httpService.post(this.baseUri, uri, req);
    ret.subscribe(() => {
      this.afterMediaServerChanged();
      this.playlistStructureChanged$.next({ removedObjectId: objectId });
    });
    return ret;
  }
}
