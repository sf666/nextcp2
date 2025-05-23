import { RendererService } from './renderer.service';
import { SseService } from './sse/sse.service';
import { GenericResultService } from './generic-result.service';
import { GenericBooleanRequest, GenericNumberRequest, MusicItemDto, PlayRequestDto, PlaylistState, ContainerDto, PlaylistAddContainerRequest } from './dto.d';
import { DeviceService } from './device.service';
import { HttpService } from './http.service';
import { Injectable, OnInit, signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService implements OnInit {

  private baseUri = '/PlaylistService';

  playlistState = signal<PlaylistState>({
    udn: '',
    Id: 0,
    ProtocolInfo: '',
    Repeat: false,
    Shuffle: false,
    TracksMax: 255,
    TransportState: 'unknown'
  });

  // Playlist items of selected media renderer device
  public playlistItems = signal<MusicItemDto[]>([]);

  constructor(
    private httpService: HttpService,
    sseService: SseService,
    private rendererService: RendererService,
    private genericResultService: GenericResultService,
    private deviceService: DeviceService) {

    // 
    // register for domain events (playlist items & playlist state)
    // ================================================================================

    sseService.mediaRendererPlaylistItemsChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        console.log("playlist-service mediaRendererPlaylistItemsChanged. Item count : " + data.musicItemDto.length);
        this.playlistItems.set(data.musicItemDto);
      }
    });

    sseService.mediaRendererPlaylistStateChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        this.playlistState.set(data);
      }
    });
  }

  ngOnInit(): void {
    //
    // init state variables (playlist items & playlist state)
    // ================================================================================
    this.updatePlaylistItems();


    // subscribe to device changes
    toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(data => {
      this.getPlaylistItems(data.udn);
      this.getPlaylistState(data.udn);
    })
  }


  public updatePlaylistItems(): void {
    if (this.deviceService.selectedMediaRendererDevice().udn !== '') {
      this.getPlaylistItems(this.deviceService.selectedMediaRendererDevice().udn);
      this.getPlaylistState(this.deviceService.selectedMediaRendererDevice().udn);
    }
  }

  //
  // Ui action endpoints
  // ===========================================================================

  public toggleShuffle(): void {
    this.setShuffle(!this.rendererService.isShuffle());
  }

  public toggleRepeat(): void {
    this.setRepeat(!this.rendererService.isRepeat());
  }

  private getSelectedMediaRendererUdn(): string {
    if (this.deviceService.selectedMediaRendererDevice().udn !== '') {
      return this.deviceService.selectedMediaRendererDevice().udn;
    }
    this.genericResultService.displayErrorMessage("output device not selected. Aborting ... ", "Output device error");
  }

  //
  // renderer playlist actions
  // ===========================================================================

  public seekId(id: string): void {
    if (this.getSelectedMediaRendererUdn() !== '') {
      const uri = '/seekId';
      const genericNumberRequest: GenericNumberRequest = { deviceUDN: this.getSelectedMediaRendererUdn(), value: parseInt(id) };
      this.httpService.post(this.baseUri, uri, genericNumberRequest).subscribe();
    }
  }

  public getPlaylistItems(udn: string): void {
    const uri = '/getPlaylistItems';
    if (udn !== '') {
      this.httpService.post<MusicItemDto[]>(this.baseUri, uri, udn).subscribe(data => {
        this.playlistItems.set(data);
        console.log("playlist-service getPlaylistItems. Item count : " + data.length);
      });
    }
  }

  public getPlaylistState(udn: string): void {
    const uri = '/getState';
    if (udn !== '') {
      this.httpService.post<PlaylistState>(this.baseUri, uri, udn).subscribe(data => this.playlistState.set(data));
    }
  }

  public readList(): void {
    const uri = '/readList';
    this.httpService.post(this.baseUri, uri, this.getSelectedMediaRendererUdn()).subscribe();
  }

  public addToPlaylist(musicItemDto: MusicItemDto): void {
    const uri = '/insert';
    const playRequestDto: PlayRequestDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice(),
      streamMetadata: musicItemDto.currentTrackMetadata,
      streamUrl: musicItemDto.streamingURL
    }

    this.httpService.post(this.baseUri, uri, playRequestDto).subscribe();
  }

  public addToPlaylistNext(musicItemDto: MusicItemDto): void {
    const uri = '/insertNext';
    const playRequestDto: PlayRequestDto = {
      mediaRendererDto: this.deviceService.selectedMediaRendererDevice(),
      streamMetadata: musicItemDto.currentTrackMetadata,
      streamUrl: musicItemDto.streamingURL
    }

    this.httpService.post(this.baseUri, uri, playRequestDto).subscribe();
  }

  public addContainerToPlaylist(containerDto: ContainerDto): void {
    const uri = '/insertContainer';
    const playlistAddContainerRequest: PlaylistAddContainerRequest = {
      containerDto: containerDto,
      shuffle: false,
      mediaRendererUdn: this.getSelectedMediaRendererUdn()
    }

    this.httpService.postWithSuccessMessage(this.baseUri, uri, playlistAddContainerRequest, 'Playlist', 'Songs successfully added.', "Error adding songs to playlist.").subscribe();
  }

  public addContainerToPlaylistAndPlay(containerDto: ContainerDto, _shuffle: boolean): void {
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
    const req: GenericBooleanRequest = {
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
    const req: GenericBooleanRequest = {
      deviceUDN: this.getSelectedMediaRendererUdn(),
      value: repeat
    };

    this.httpService.post(this.baseUri, uri, req).subscribe();
  }

  public deleteSongFromRendererPlaylist(id: string): void {
    const uri = '/delete';
    const req: GenericNumberRequest = {
      deviceUDN: this.getSelectedMediaRendererUdn(),
      value: parseInt(id)
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
}
