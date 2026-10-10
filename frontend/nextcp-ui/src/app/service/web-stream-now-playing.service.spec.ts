import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Subject } from 'rxjs';
import { WebStreamNowPlayingService } from './web-stream-now-playing.service';
import { DeviceService } from './device.service';
import { SseService } from './sse/sse.service';
import { ItemDto, TrackInfoDto, WebStreamNowPlayingDto } from './dto.d';

describe('WebStreamNowPlayingService', () => {
  let service: WebStreamNowPlayingService;
  let http: HttpTestingController;
  let pushes: Subject<WebStreamNowPlayingDto>;
  let opened: Subject<void>;
  const station = { objectID: '2044', mediaServerUDN: '', title: 'Mellow Smooth Jazz',
    albumArtUrl: 'station-small', albumArtUrlLarge: 'station-large',
    audioFormat: { durationInSeconds: 0 } } as ItemDto;
  const url = '/MediaServerPlaylistService/getWebStreamNowPlaying/ums/2044';
  const live: WebStreamNowPlayingDto = { objectID: '2044', streamTitle: 'Oli Silk - Sanctuary St.',
    artist: 'Oli Silk', title: 'Sanctuary St.', artUrl: '//cdn-images.audioaddict.com/cover.jpg' };

  beforeEach(() => {
    vi.useFakeTimers();
    pushes = new Subject();
    opened = new Subject();
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: DeviceService, useValue: { hasFeature: () => true, selectedMediaServerDevice: () => ({ udn: 'ums' }) } },
      { provide: SseService, useValue: { webStreamNowPlaying$: pushes, connectionOpened$: opened } },
    ] });
    service = TestBed.inject(WebStreamNowPlayingService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    http.verify();
    TestBed.resetTestingModule();
    vi.useRealTimers();
  });

  it('uses live titles and artwork for both cover sizes', () => {
    pushes.next(live);
    const result = service.withLiveTitle({ currentTrack: station } as TrackInfoDto).currentTrack;
    expect(result.title).toBe('Sanctuary St.');
    expect(result.artistName).toBe('Oli Silk');
    expect(result.album).toBe('Mellow Smooth Jazz');
    expect(result.albumArtUrl).toBe('https://cdn-images.audioaddict.com/cover.jpg');
    expect(result.albumArtUrlLarge).toBe(result.albumArtUrl);
    expect(station.albumArtUrlLarge).toBe('station-large');
  });

  it('refreshes the active stream after a reconnect even when it is cached', () => {
    service.watch(station, true);
    http.expectOne(url).flush(live);
    opened.next();
    http.expectOne(url).flush({ ...live, title: 'Next song', artUrl: 'https://cdn/new.jpg' });
    const result = service.withLiveTitle({ currentTrack: station } as TrackInfoDto).currentTrack;
    expect(result.title).toBe('Next song');
    expect(result.albumArtUrlLarge).toBe('https://cdn/new.jpg');
  });

  it('recovers a missed push for the active stream without overlapping requests', () => {
    service.watch(station, true);
    http.expectOne(url).flush(live);
    vi.advanceTimersByTime(15000);
    const request = http.expectOne(url);
    vi.advanceTimersByTime(5000);
    http.expectNone(url);
    request.flush({ ...live, title: 'Updated' });
    expect(service.withLiveTitle({ currentTrack: station } as TrackInfoDto).currentTrack.title).toBe('Updated');
  });

  it('does not poll a paused stream or a finite audio file', () => {
    service.watch(station, false);
    http.expectOne(url).flush(live);
    vi.advanceTimersByTime(60000);
    http.expectNone(url);
    service.watch({ ...station, audioFormat: { ...station.audioFormat, durationInSeconds: 180 } }, true);
    vi.advanceTimersByTime(60000);
    http.expectNone(url);
  });

  it('retries after a failed snapshot instead of permanently marking the stream as requested', () => {
    service.ensureKnown(station);
    http.expectOne(url).flush('offline', { status: 503, statusText: 'Unavailable' });
    service.ensureKnown(station);
    http.expectOne(url).flush(live);
    expect(service.withLiveTitle({ currentTrack: station } as TrackInfoDto).currentTrack.title).toBe(live.title);
  });

  it('keeps recent pushed metadata and stops polling a deselected stream', () => {
    service.watch(station, true);
    http.expectOne(url).flush(live);
    vi.advanceTimersByTime(10000);
    pushes.next(live);
    vi.advanceTimersByTime(5000);
    http.expectNone(url);
    service.watch(null, false);
    vi.advanceTimersByTime(60000);
    http.expectNone(url);
  });

  it('returns to station artwork when the stream clears its live title', () => {
    pushes.next(live);
    pushes.next({ objectID: '2044', title: '', streamTitle: '', artist: '', artUrl: '' });
    const info = { currentTrack: station } as TrackInfoDto;
    expect(service.withLiveTitle(info)).toBe(info);
  });
});
