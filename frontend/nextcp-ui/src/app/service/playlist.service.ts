import { Subject } from 'rxjs';
import { SseService } from './sse/sse.service';
import { GenericResultService } from './generic-result.service';
import { GenericBooleanRequest, GenericNumberRequest, MusicItemDto, PlayRequestDto, PlaylistState, ContainerDto, PlaylistAddContainerRequest, FileSystemPlaylistAdd } from './dto.d';
import { DeviceService } from './device.service';
import { HttpService } from './http.service';
import { Injectable, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService implements OnInit {

  baseUri = '/PlaylistService';

  public playlistState: PlaylistState = {
    udn: '',
    Id: 0,
    ProtocolInfo: '',
    Repeat: false,
    Shuffle: false,
    TracksMax: 255,
    TransportState: 'unknown'
  }

  // Default Playlists located on the file system
  fsPlaylists: string[] = [];


  // Playlist items of selected media renderer device
  public playlistItems: MusicItemDto[] = [];

  constructor(
    private httpService: HttpService,
    sseService: SseService,
    private genericResultService: GenericResultService,
    private deviceService: DeviceService) {

    // 
    // register for domain events (playlist items & playlist state)
    // ================================================================================

    sseService.mediaRendererPlaylistItemsChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        this.playlistItems = data.musicItemDto;
      };
    });

    sseService.mediaRendererPlaylistStateChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        this.playlistState = data
      }
    });

    // 
    // read available default Playlists
    // ================================================================================
    const uri = '/getDefaultPlaylists';

    this.httpService.get<string[]>(this.baseUri, uri).subscribe(data => {
      this.fsPlaylists = data.sort();
    });
  }

  ngOnInit(): void {

    //
    // init state variables (playlist items & playlist state)
    // ================================================================================
    this.updatePlaylistItems();


    // subscribe to device changes
    this.deviceService.mediaRendererChanged$.subscribe(data => {
      this.getPlaylistItems(data.udn);
      this.getPlaylistState(data.udn);
    })
  }

  public updatePlaylistItems() {
    if (this.deviceService.selectedMediaRendererDevice.udn !== '') {
      this.getPlaylistItems(this.deviceService.selectedMediaRendererDevice.udn);
      this.getPlaylistState(this.deviceService.selectedMediaRendererDevice.udn);
    }
  }

  //
  // Ui action endpoints
  // ===========================================================================

  public toggleShuffle() {
    this.setShuffle(!this.playlistState.Shuffle);
  }

  public toggleRepeat() {
    this.setRepeat(!this.playlistState.Repeat);
  }

  private getSelectedMediaRendererUdn(): string {
    if (this.deviceService.selectedMediaRendererDevice.udn !== '') {
      return this.deviceService.selectedMediaRendererDevice.udn;
    }
    this.genericResultService.displayErrorMessage("output device not selected. Aborting ... ", "Output device error");
  }

  //
  // renderer playlist actions
  // ===========================================================================

  public seekId(id: number) {
    if (this.getSelectedMediaRendererUdn() !== '') {
      const uri = '/seekId';
      const genericNumberRequest: GenericNumberRequest = { deviceUDN: this.getSelectedMediaRendererUdn(), value: id };
      this.httpService.post(this.baseUri, uri, genericNumberRequest).subscribe();
    }
  }

  public getPlaylistItems(udn: string) {
    const uri = '/getPlaylistItems';
    if (udn !== '') {
      this.httpService.post<MusicItemDto[]>(this.baseUri, uri, udn).subscribe(data => this.playlistItems = data);
    }
  }

  public getPlaylistState(udn: string) {
    const uri = '/getState';
    if (udn !== '') {
      this.httpService.post<PlaylistState>(this.baseUri, uri, udn).subscribe(data => this.playlistState = data);
    }
  }

  public readList() {
    const uri = '/readList';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public addToPlaylist(musicItemDto: MusicItemDto) {
    const uri = '/insert';
    const playRequestDto: PlayRequestDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice,
      streamMetadata: musicItemDto.currentTrackMetadata,
      streamUrl: musicItemDto.streamingURL
    }

    this.httpService.post(this.baseUri, uri, playRequestDto).subscribe();
  }

  public addContainerToPlaylist(containerDto: ContainerDto) {
    const uri = '/insertContainer';
    const playlistAddContainerRequest: PlaylistAddContainerRequest = {
      containerDto: containerDto,
      shuffle: false,
      mediaRendererUdn: this.getSelectedMediaRendererUdn()
    }

    this.httpService.postWithSuccessMessage(this.baseUri, uri, playlistAddContainerRequest, 'Playlist', 'Songs successfully added.', "Error adding songs to playlist.").subscribe();
  }

  public addContainerToPlaylistAndPlay(containerDto: ContainerDto, _shuffle: boolean) {
    const uri = '/insertAndPlayContainer';
    const playlistAddContainerRequest: PlaylistAddContainerRequest = {
      containerDto: containerDto,
      shuffle: _shuffle,
      mediaRendererUdn: this.getSelectedMediaRendererUdn()
    }

    this.httpService.postWithSuccessMessage(this.baseUri, uri, playlistAddContainerRequest, 'Playlist', 'Songs successfully added. Start playing ... ', "Error adding songs to playlist.").subscribe();
  }

  public setShuffle(shuffle: boolean): void {
    const uri = '/setShuffle';
    var req: GenericBooleanRequest = {
      deviceUDN: this.getSelectedMediaRendererUdn(),
      value: shuffle
    };

    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  public pause(): void {
    const uri = '/pause';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public deleteAll(): void {
    const uri = '/deleteAll';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public setRepeat(repeat: boolean): void {
    const uri = '/setRepeat';
    var req: GenericBooleanRequest = {
      deviceUDN: this.getSelectedMediaRendererUdn(),
      value: repeat
    };

    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  public delete(id: number): void {
    const uri = '/delete';
    var req: GenericNumberRequest = {
      deviceUDN: this.getSelectedMediaRendererUdn(),
      value: id
    };

    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  public play(): void {
    const uri = '/play';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public next(): void {
    const uri = '/next';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public previous(): void {
    const uri = '/previous';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  //
  // Filesystem Playlist actions
  // ========================================================================

  public addToFilesystemPlaylist(musicBrainzId : string, playlistName: string): void {
    const uri = '/addToFilesystemPlaylist';
    let req : FileSystemPlaylistAdd = {musicBrainzId: musicBrainzId, playlistName: playlistName};
    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

}
