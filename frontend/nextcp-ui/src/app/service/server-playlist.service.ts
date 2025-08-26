import { ConfigurationService } from 'src/app/service/configuration.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { Injectable, computed, signal } from '@angular/core';
import { Observable } from 'rxjs';
import {
  CreateServerPlaylistVO,
  MediaServerDto,
  ServerDeleteObjectRequest,
  ServerPlaylistEntry,
  ServerPlaylists,
} from './dto';
import { HttpService } from './http.service';
import { SseService } from './sse/sse.service';
import { DeviceService } from './device.service';

@Injectable({
  providedIn: 'root',
})
export class ServerPlaylistService {
  baseUri = '/MediaServerPlaylistService';


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

  selectedMediaServer = computed(() => { return this.deviceService.selectedMediaServerDevice() });

  constructor(
    private httpService: HttpService,
    private configurationService: ConfigurationService,
    private deviceService: DeviceService,
    sseService: SseService,
  ) {
    // reconstruct sidebar after potential playlist folder change 
    configurationService.applicationConfigChanged$.subscribe((appConfig) => {
      this.afterMediaServerChanged();
    });

    toObservable(this.selectedMediaServer).subscribe(data => this.afterMediaServerChanged())

    sseService.mediaServerPlaylistChanged$.subscribe((data) => {
      if (deviceService.isMediaServerSelected(data.mediaServerUdn)) {
        this.serverPl.set(data);
      }
    });

    sseService.mediaServerRecentPlaylistChanged$.subscribe((data) => {
      console.log("updating recently used playlists ... ");
      this.recentServerPl.set(data);
    })
  }

  private afterMediaServerChanged() {
    this.updateServerAccessiblePlaylists();
    this.updateRecentServerAccessiblePlaylists();
  }

  //
  // Playlists located in the configured folder name
  //
  public updateServerAccessiblePlaylists() {
    if (this.selectedMediaServer().udn) {
      const uri = '/getServerPlaylists';
      this.httpService
        .post<ServerPlaylists>(this.baseUri, uri, this.selectedMediaServer().udn)
        .subscribe((data) => {
          this.serverPl.set(data);
        });
    } else {
      console.log("updateServerAccessiblePlaylists : skipping, no server selected ...");
    }
  }

  public updateRecentServerAccessiblePlaylists() {
    if (this.selectedMediaServer().udn) {
      const uri = '/getRecentServerPlaylists';
      this.httpService
        .post<ServerPlaylists>(this.baseUri, uri, this.selectedMediaServer().udn)
        .subscribe((data) => {
          this.recentServerPl.set(data);
        });
    } else {
      console.log("updateRecentServerAccessiblePlaylists : skipping, no server selected ...");
    }
  }

  public playlistIdExistsInServerPlaylists(id: string): boolean {
    return this.serverPlPlaylistIds().indexOf(id) > -1;
  }

  //
  // Filesystem Playlist actions (MediaServer actions)
  // ========================================================================

  public createPlaylist(playlistName: string, containerId: string): Observable<string> {
    const createPL: CreateServerPlaylistVO = {
      containerId: containerId,
      mediaServerUdn: this.selectedMediaServer().udn,
      playlistName: playlistName + '.m3u8',
    };
    const uri = '/createPlaylist';
    let ret = this.httpService.post<string>(this.baseUri, uri, createPL)
    ret.subscribe(() => this.updateServerAccessiblePlaylists());
    return ret;
  }

  public addSongToServerPlaylist(songId: string, playlistId: string): Observable<any> {
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

  public deletePlaylistFile(objectId: string): Observable<any> {
    let ret = this.deleteObject(objectId);
    ret.subscribe(() => this.updateServerAccessiblePlaylists());
    return ret;
  }

  public deletePlaylistSongEntry(objectId: string): Observable<any> {
    return this.deleteObject(objectId);
  }

  /**
   *
   * @param songId This method deletes objects located on the media server device, like playlists or playlists entries.
   * @param playlistId
   */
  public deleteObject(objectId: string): Observable<any> {
    const uri = '/deleteObject';
    const req: ServerDeleteObjectRequest = {
      serverUdn: this.selectedMediaServer().udn,
      objectId: objectId,
    };
    let ret = this.httpService.post(this.baseUri, uri, req);
    ret.subscribe();
    return ret;
  }
}
