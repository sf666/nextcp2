import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
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


  recentServerPl : ServerPlaylists = {
    mediaServerUdn: '',
    containerId: '',
    serverPlaylists: [],
  };

  // Default server based playlists
  serverPl: ServerPlaylists = {
    mediaServerUdn: '',
    containerId: '',
    serverPlaylists: [],
  };
  serverPlPlaylistIds: string[] = [];

  selectedMediaServer: MediaServerDto;

  constructor(
    private httpService: HttpService,
    private deviceService: DeviceService,
    sseService: SseService,
  ) {
    sseService.mediaServerPlaylistChanged$.subscribe((data) => {
      if (deviceService.isMediaServerSelected(data.mediaServerUdn)) {
        this.serverPl = data;
        this.serverPl.serverPlaylists?.forEach((element) => {
          this.serverPlPlaylistIds.push(element.playlistId);
        });
      }
    });

    sseService.mediaServerRecentPlaylistChanged$.subscribe((data) => {
      this.recentServerPl = data;
    })

    deviceService.mediaServerChanged$.subscribe((server) =>
      this.afterMediaServerChanged(server),
    );
  }

  private afterMediaServerChanged(server: MediaServerDto) {
    this.selectedMediaServer = server;
    this.updateServerAccessiblePlaylists();
    this.updateRecentServerAccessiblePlaylists();
  }

  //
  // Playlists located in the configured folder name
  //
  public updateServerAccessiblePlaylists() {
    const uri = '/getServerPlaylists';
    this.httpService
      .post<ServerPlaylists>(this.baseUri, uri, this.selectedMediaServer.udn)
      .subscribe((data) => {
        this.serverPl = data;
        this.serverPl.serverPlaylists?.forEach((element) => {
          this.serverPlPlaylistIds.push(element.playlistId);
        });
      });
  }

  public updateRecentServerAccessiblePlaylists() {
    const uri = '/getRecentServerPlaylists';
    this.httpService
      .post<ServerPlaylists>(this.baseUri, uri, this.selectedMediaServer.udn)
      .subscribe((data) => {
        this.recentServerPl = data;
      });
  }

  public playlistIdExistsInServerPlaylists(id: string): boolean {
    return this.serverPlPlaylistIds.indexOf(id) > -1;
  }

  //
  // Filesystem Playlist actions (MediaServer actions)
  // ========================================================================

  public createPlaylist(playlistName: string): Observable<string> {
    const createPL: CreateServerPlaylistVO = {
      containerId: this.serverPl.containerId,
      mediaServerUdn: this.selectedMediaServer.udn,
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
      serverUdn: this.selectedMediaServer.udn,
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
      serverUdn: this.selectedMediaServer.udn,
      objectId: objectId,
    };
    let ret = this.httpService.post(this.baseUri, uri, req);
    ret.subscribe();
    return ret;
  }
}
