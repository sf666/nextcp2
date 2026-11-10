import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs';
import { DtoGeneratorService } from '../util/dto-generator.service';
import { CdsUpdateService } from './cds-update.service';
import { ConfigurationService } from './configuration.service';
import { ContentDirectoryService } from './content-directory.service';
import { DeviceService } from './device.service';
import { ContainerUpdateIdsDto } from './dto.d';
import { GenericResultService } from './generic-result.service';
import { RadioBrowserService } from './radio-browser.service';
import { ServerPlaylistService } from './server-playlist.service';
import { SseService } from './sse/sse.service';
import { ToastService } from './toast/toast.service';

describe('Radio playlist refresh', () => {
  const playlistId = 'sunshine';
  const browseUrl = '/ContentDirectoryService/browseChildren';
  let cds: ContentDirectoryService;
  let dtos: DtoGeneratorService;
  let http: HttpTestingController;
  let serverUpdates: Subject<ContainerUpdateIdsDto>;

  function playlist(version: number, stationIds: string[] = ['existing']) {
    return {
      ...dtos.generateEmptyContainerItemDto(),
      currentContainer: {
        ...dtos.generateEmptyContainerDto(),
        id: playlistId,
        parentID: '0',
        objectClass: 'object.container.playlistContainer',
        albumartUri: '/playlist-cover?update=' + version,
      },
      items: stationIds.map((objectID) => ({
        ...dtos.emptyItemDto(),
        objectID,
        objectClass: 'object.item.audioItem.audioBroadcast',
        albumArtUrl: '/' + objectID + '-cover?update=' + version,
      })),
      totalMatches: stationIds.length,
    };
  }

  beforeEach(() => {
    vi.useFakeTimers();
    serverUpdates = new Subject();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        ContentDirectoryService,
        { provide: ConfigurationService, useValue: { applicationConfig: {} } },
        { provide: DeviceService, useValue: { selectedMediaServerDevice: () => ({ udn: 'ums' }) } },
        { provide: ServerPlaylistService, useValue: { playlistStructureChanged$: new Subject() } },
        { provide: SseService, useValue: { mediaServerContainerUpdateIds$: serverUpdates } },
        { provide: GenericResultService, useValue: {} },
        { provide: ToastService, useValue: {} },
      ],
    });
    cds = TestBed.inject(ContentDirectoryService);
    dtos = TestBed.inject(DtoGeneratorService);
    http = TestBed.inject(HttpTestingController);
    cds.updateContainer(playlist(1));
  });

  afterEach(() => {
    http.verify();
    TestBed.resetTestingModule();
    vi.useRealTimers();
  });

  it('picks up a late station with one refresh per server burst and keeps the existing artwork', () => {
    TestBed.inject(CdsUpdateService).announceRatingChange({
      objectID: playlistId,
      containerId: playlistId,
      rating: 5,
    });
    const existing = cds.musicTracks_()[0];

    TestBed.inject(RadioBrowserService).addStationToPlaylist(playlistId, 'uuid', 'New');
    http.expectOne('/MediaServerPlaylistService/addRadioStationToPlaylist').flush(null);
    http.expectOne(browseUrl).flush(playlist(2));
    expect(cds.musicTracks_()).toEqual([existing]);

    const update = { mediaServerUdn: 'ums', containerIds: [playlistId] };
    serverUpdates.next(update);
    vi.advanceTimersByTime(300);
    serverUpdates.next(update);
    vi.advanceTimersByTime(400);
    http.expectOne(browseUrl).flush(playlist(3, ['existing', 'new']));

    expect(cds.musicTracks_().map((item) => item.objectID)).toEqual(['existing', 'new']);
    expect(cds.musicTracks_()[0]).toBe(existing);
    expect(cds.currentContainerList().currentContainer.albumartUri).toBe('/playlist-cover?update=1');
    vi.advanceTimersByTime(5000);
    http.expectNone(browseUrl);
  });

  it('reloads an explicitly edited playlist cover even if the server keeps the URL', () => {
    TestBed.inject(CdsUpdateService).setNewAlbumArtUri(
      { objectID: playlistId, acoustID: '', musicBrainzIdTrackId: '' },
      '/playlist-cover?update=1',
      'https://example.com/cover.jpg',
    );
    http.expectOne('/ContentDirectoryService/updateAlbumArtUri').flush(null);
    http.expectOne(browseUrl).flush(playlist(2));

    expect(cds.currentContainerList().currentContainer.albumartUri).toMatch(
      /^\/playlist-cover\?update=1&nextcpArt=\d+$/,
    );
  });

  it('keeps the cover of a playlist tile while refreshing its metadata', () => {
    const parent = (version: number, childCount: number) => {
      const data = dtos.generateEmptyContainerItemDto();
      data.currentContainer.id = '0';
      data.containerDto = [{ ...playlist(version).currentContainer, childCount }];
      data.totalMatches = 1;
      return data;
    };
    cds.updateContainer(parent(1, 1));

    serverUpdates.next({ mediaServerUdn: 'ums', containerIds: [playlistId] });
    vi.advanceTimersByTime(1700);
    http.expectOne(browseUrl).flush(parent(2, 2));

    expect(cds.playlistList_()[0]).toMatchObject({
      childCount: 2,
      albumartUri: '/playlist-cover?update=1',
    });
  });
});
