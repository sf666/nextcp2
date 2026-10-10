import { DestroyRef, Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize, timeout } from 'rxjs';
import { DeviceService } from './device.service';
import { ServerFeature } from './server-feature';
import { SseService } from './sse/sse.service';
import { ItemDto, TrackInfoDto, WebStreamNowPlayingDto } from './dto.d';

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
  private readonly http = inject(HttpClient);
  private readonly destroyRef = inject(DestroyRef);
  private readonly deviceService = inject(DeviceService);
  private readonly sseService = inject(SseService);

  private readonly baseUri = '/MediaServerPlaylistService';

  // A signal, so every computed that shows a track re-runs when a title arrives.
  private readonly byObjectId = signal<Record<string, WebStreamNowPlayingDto>>({});
  // Streams already asked about, so a display that renders often does not ask again.
  private readonly requested = new Set<string>();
  private readonly inFlight = new Set<string>();
  private readonly lastPush = new Map<string, number>();
  private readonly lastSnapshot = new Map<string, number>();
  private active: { udn: string; objectId: string; playing: boolean } | null = null;

  constructor() {
    this.sseService.webStreamNowPlaying$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((info) => {
      if (info?.objectID) {
        this.lastPush.set(info.objectID, Date.now());
      }
      this.remember(info);
    });
    this.sseService.connectionOpened$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      // GENA only sends changes. Fetch the current title and artwork after any SSE reconnect.
      if (this.active) {
        this.readSnapshot(this.active.udn, this.active.objectId);
      }
    });
    const timer = setInterval(() => {
      const active = this.active;
      if (active?.playing && Date.now() - (this.lastPush.get(active.objectId) ?? 0) >= 15000 &&
        Date.now() - (this.lastSnapshot.get(`${active.udn}/${active.objectId}`) ?? 0) >= 15000) {
        // Only the displayed live stream is checked; a lost push must not freeze its cover forever.
        this.readSnapshot(active.udn, active.objectId);
      }
    }, 5000);
    this.destroyRef.onDestroy(() => clearInterval(timer));
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
    const currentTrack: ItemDto = {
      ...track,
      album: track.title,
      title,
      // A line without an artist - a jingle, an ad - keeps the station instead.
      artistName: live.artist || track.artistName,
      albumArtUrl: this.artworkUrl(live.artUrl) || track.albumArtUrl,
      albumArtUrlLarge: this.artworkUrl(live.artUrl) || track.albumArtUrlLarge,
    };
    return { ...info, currentTrack };
  }

  /**
   * Asks once what a stream is playing. Only changes are pushed, so a display that starts in the
   * middle of a track - after a reload, or when a renderer is selected while it already runs -
   * would show the station name until the next track begins.
   */
  public ensureKnown(track: ItemDto | null | undefined): void {
    // Nothing here is worth breaking a caller for - this only fills in a title.
    try {
      const reference = this.referenceFor(track);
      if (!reference) {
        return;
      }
      const { udn, objectId } = reference;
      const key = `${udn}/${objectId}`;
      if (this.requested.has(key) || this.byObjectId()[objectId]) {
        return;
      }
      this.requested.add(key);
      this.readSnapshot(udn, objectId);
    } catch (err) {
      console.warn('[web-stream-now-playing] cannot ask what the stream is playing', err);
    }
  }

  public watch(track: ItemDto | null | undefined, playing: boolean): void {
    const reference = this.referenceFor(track);
    if (!reference) {
      this.active = null;
      return;
    }
    const changed = this.active?.udn !== reference.udn || this.active?.objectId !== reference.objectId;
    this.active = { ...reference, playing };
    if (changed) {
      this.readSnapshot(reference.udn, reference.objectId);
    }
  }

  private referenceFor(track: ItemDto | null | undefined): { udn: string; objectId: string } | null {
    if (!track?.objectID || (track.audioFormat?.durationInSeconds ?? 0) > 0 ||
      !this.deviceService.hasFeature(ServerFeature.WEB_STREAM_NOW_PLAYING)) {
      return null;
    }
    const udn = track.mediaServerUDN || this.deviceService.selectedMediaServerDevice()?.udn;
    return udn ? { udn, objectId: track.objectID } : null;
  }

  private readSnapshot(udn: string, objectId: string): void {
    const key = `${udn}/${objectId}`;
    if (this.inFlight.has(key)) {
      return;
    }
    this.inFlight.add(key);
    this.lastSnapshot.set(key, Date.now());
    this.http.get<WebStreamNowPlayingDto>(
      `${this.baseUri}/getWebStreamNowPlaying/${encodeURIComponent(udn)}/${encodeURIComponent(objectId)}`,
    ).pipe(
      timeout(10000),
      takeUntilDestroyed(this.destroyRef),
      finalize(() => this.inFlight.delete(key)),
    ).subscribe({
      next: (info) => this.remember(info),
      error: () => this.requested.delete(key),
    });
  }

  private artworkUrl(url: string): string {
    return url?.startsWith('//') ? `https:${url}` : url;
  }

  private remember(info: WebStreamNowPlayingDto): void {
    if (!info?.objectID) {
      return;
    }
    const title = info.title || info.streamTitle;
    this.byObjectId.update((current) => {
      const previous = current[info.objectID];
      if (previous && previous.streamTitle === info.streamTitle && previous.title === info.title &&
        previous.artist === info.artist && previous.artUrl === info.artUrl) {
        return current;
      }
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
