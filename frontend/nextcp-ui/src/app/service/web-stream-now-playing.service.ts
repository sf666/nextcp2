import { Injectable, inject, signal } from '@angular/core';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { SseService } from './sse/sse.service';
import { MusicItemDto, TrackInfoDto, WebStreamNowPlayingDto } from './dto.d';

/**
 * What the continuous streams of the media server are playing right now, kept by objectID.
 *
 * The media server announces a change for the stream, not for a renderer - it does not know who is
 * listening. The titles are therefore collected here and applied wherever a track is displayed,
 * which puts a UPnP renderer and the browser player, that has no renderer at all, on one path.
 */
@Injectable({
  providedIn: 'root',
})
export class WebStreamNowPlayingService {
  private readonly httpService = inject(HttpService);
  private readonly deviceService = inject(DeviceService);
  private readonly sseService = inject(SseService);

  private readonly baseUri = '/MediaServerPlaylistService';

  // A signal, so every computed that shows a track re-runs when a title arrives.
  private readonly byObjectId = signal<Record<string, WebStreamNowPlayingDto>>({});
  // Streams already asked about, so a display that renders often does not ask again.
  private readonly requested = new Set<string>();

  constructor() {
    this.sseService.webStreamNowPlaying$.subscribe((info) => this.remember(info));
  }

  /**
   * Puts the live title into a track info, if the stream it shows has announced one. The station
   * name moves to the album so the context stays visible.
   */
  public withLiveTitle(info: TrackInfoDto): TrackInfoDto {
    const track = info?.currentTrack;
    const live = track?.objectID ? this.byObjectId()[track.objectID] : undefined;
    const title = live ? live.title || live.streamTitle : '';
    if (!track || !live || !title) {
      return info;
    }
    const currentTrack: MusicItemDto = {
      ...track,
      album: track.title,
      title,
      // A line without an artist - a jingle, an ad - keeps the station instead.
      artistName: live.artist || track.artistName,
      albumArtUrl: live.artUrl || track.albumArtUrl,
    };
    return { ...info, currentTrack };
  }

  /**
   * Asks once what a stream is playing. Only changes are pushed, so a display that starts in the
   * middle of a track - after a reload, or when a renderer is selected while it already runs -
   * would show the station name until the next track begins.
   */
  public ensureKnown(track: MusicItemDto | null | undefined): void {
    const objectId = track?.objectID;
    if (!objectId || this.requested.has(objectId) || this.byObjectId()[objectId]) {
      return;
    }
    const udn = track.mediaServerUDN || this.deviceService.selectedMediaServerDevice()?.udn;
    if (!udn) {
      return;
    }
    this.requested.add(objectId);
    this.httpService
      .get<WebStreamNowPlayingDto>(
        this.baseUri,
        `/getWebStreamNowPlaying/${udn}/${encodeURIComponent(objectId)}`,
        'web radio',
      )
      .subscribe((info) => this.remember(info));
  }

  private remember(info: WebStreamNowPlayingDto): void {
    if (!info?.objectID) {
      return;
    }
    const title = info.title || info.streamTitle;
    this.byObjectId.update((current) => {
      if (!title) {
        // The stream stopped announcing a track: back to the station name.
        if (!(info.objectID in current)) {
          return current;
        }
        const next = { ...current };
        delete next[info.objectID];
        return next;
      }
      return { ...current, [info.objectID]: info };
    });
  }
}
