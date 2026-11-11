import { NO_ERRORS_SCHEMA, signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Subject } from 'rxjs';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, ItemDto } from 'src/app/service/dto';
import { LocalPlayerService } from 'src/app/service/local-player.service';
import { LocalVideoPlayerService } from 'src/app/service/local-video-player.service';
import { PersistenceService } from 'src/app/service/persistence/persistence.service';
import { PlaylistService } from 'src/app/service/playlist.service';
import { RatingServiceService } from 'src/app/service/rating-service.service';
import { GlobalSearchService } from 'src/app/service/search/global-search.service';
import { TransportService } from 'src/app/service/transport.service';
import { BackgroundImageService } from 'src/app/util/background-image.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { DisplayContainerComponent } from './display-container.component';
import { ContainerTileComponent } from './container-tile/container-tile.component';
import { ItemTileComponent } from './item-tile/item-tile.component';

describe('Browse type filter', () => {
  const musicTracks = signal<ItemDto[]>([]);
  const otherItems = signal<ItemDto[]>([]);
  const playlists = signal<ContainerDto[]>([]);
  const currentContainer = signal<ContainerDto | undefined>(undefined);
  let fixture: ComponentFixture<DisplayContainerComponent>;
  let dtos: DtoGeneratorService;

  beforeEach(() => {
    musicTracks.set([]);
    otherItems.set([]);
    playlists.set([]);
    TestBed.configureTestingModule({
      imports: [DisplayContainerComponent],
      providers: [
        ...[
          ConfigurationService,
          GlobalSearchService,
          LocalPlayerService,
          LocalVideoPlayerService,
          PlaylistService,
          RatingServiceService,
          TransportService,
        ].map((service) => ({ provide: service, useValue: {} })),
        { provide: DeviceService, useValue: { hasFeature: () => false } },
        { provide: PersistenceService, useValue: { getLastFocusId: () => '' } },
        {
          provide: BackgroundImageService,
          useValue: {
            setDisplayContainerHeaderImage: vi.fn(),
            setBackgroundImageMainScreen: vi.fn(),
            applyAmbientTint: vi.fn(),
          },
        },
      ],
    }).overrideComponent(DisplayContainerComponent, {
      remove: { imports: [ContainerTileComponent, ItemTileComponent] },
      add: { schemas: [NO_ERRORS_SCHEMA] },
    });

    dtos = TestBed.inject(DtoGeneratorService);
    currentContainer.set({
      ...dtos.generateEmptyContainerDto(),
      id: 'sunshine',
      objectClass: 'object.container.playlistContainer',
      title: 'Sunshine',
    });
    const cds = {
      currentContainerList: () => ({ currentContainer: currentContainer() }),
      browseFinished$: new Subject(),
      inPlaceRefreshStarted$: new Subject(),
      isInPlaceRefresh: () => false,
      isSearchResultDisplayed: () => false,
      musicTracks_: musicTracks,
      otherItems_: otherItems,
      albumList_: () => [],
      containerList_: () => [],
      playlistList_: playlists,
      artistList_: () => [],
    };

    fixture = TestBed.createComponent(DisplayContainerComponent);
    fixture.componentRef.setInput('contentHandler', {
      contentDirectoryService: cds as unknown as ContentDirectoryService,
    });
  });

  function summary(): string[][] {
    const root: HTMLElement = fixture.nativeElement;
    return Array.from(root.querySelectorAll('.audio-heading-group'), (group) => [
      group.querySelector('.section-label')!.textContent.trim(),
      group.querySelector('.section-count')!.textContent.trim(),
    ]);
  }

  function typeButtons(): HTMLButtonElement[] {
    const root: HTMLElement = fixture.nativeElement;
    return Array.from(root.querySelectorAll('.type-filter button'));
  }

  it('labels a radio-only playlist in both the header and the item list', () => {
    musicTracks.set(
      Array.from({ length: 10 }, (_, index) => ({
        ...dtos.emptyItemDto(),
        objectClass: 'object.item.audioItem.audioBroadcast',
        title: `Station ${index + 1}`,
      })),
    );
    fixture.detectChanges();

    const header: HTMLElement = fixture.nativeElement.querySelector('.meta-tracks');
    expect(header.textContent.trim()).toBe('10 web radio stations');
    expect(summary()).toEqual([['Web radio', '10 items']]);
    expect(typeButtons()).toEqual([]);
  });

  it('filters sections by type and starts a new listing on All', () => {
    playlists.set([{ ...dtos.generateEmptyContainerDto(), id: 'subplaylist' }]);
    musicTracks.set([{
      ...dtos.emptyItemDto(),
      audioFormat: { ...dtos.emptyAudioFormat(), isStreaming: false },
    }]);
    fixture.detectChanges();

    const root: HTMLElement = fixture.nativeElement;
    const [all, playlist, tracks] = typeButtons();
    expect(typeButtons().map((button) => button.textContent.trim())).toEqual(['All', 'Playlists', 'Tracks']);
    expect(all.getAttribute('aria-pressed')).toBe('true');

    playlist.click();
    fixture.detectChanges();
    expect(root.querySelector('#head_browse_result_playlist_items_ID')).not.toBeNull();
    expect(root.querySelector('#head_browse_result_items_ID')).toBeNull();

    tracks.click();
    fixture.detectChanges();
    expect(root.querySelector('#head_browse_result_playlist_items_ID')).toBeNull();
    expect(summary()).toEqual([['Tracks', '1 item']]);

    currentContainer.set({ ...currentContainer()!, id: 'another-playlist' });
    fixture.detectChanges();
    expect(all.getAttribute('aria-pressed')).toBe('true');
    expect(root.querySelector('#head_browse_result_playlist_items_ID')).not.toBeNull();
  });

  it('narrows mixed audio without changing the text filter', () => {
    const audio = dtos.emptyItemDto();
    musicTracks.set([
      {
        ...audio,
        title: 'Song',
        objectClass: 'object.item.audioItem.musicTrack',
        audioFormat: { ...audio.audioFormat, isStreaming: false },
      },
      { ...audio, title: 'Station', objectClass: 'object.item.audioItem' },
    ]);
    fixture.detectChanges();

    expect(summary()).toEqual([
      ['Tracks', '1 item'],
      ['Web radio', '1 item'],
    ]);

    fixture.componentInstance.displayFilterString.set('Station');
    fixture.detectChanges();

    const [all, tracks, radio] = typeButtons();
    tracks.click();
    fixture.detectChanges();
    expect(summary()).toEqual([['Tracks', '0 of 1 item']]);
    radio.click();
    fixture.detectChanges();
    expect(summary()).toEqual([['Web radio', '1 item']]);
    expect(fixture.componentInstance.audioItems().map((item) => item.title)).toEqual(['Station']);
    all.click();
    fixture.detectChanges();
    expect(summary()).toEqual([['Tracks', '0 of 1 item'], ['Web radio', '1 item']]);
  });

  it('keeps image tiles in Other items', () => {
    otherItems.set([{
      ...dtos.emptyItemDto(),
      objectID: 'photo',
      objectClass: 'object.item.imageItem.photo',
      title: 'Photo',
    }]);
    fixture.detectChanges();

    const root: HTMLElement = fixture.nativeElement;
    const section = root.querySelector('#head_browse_other_items_ID')!;
    expect(section.querySelector('.section-label')?.textContent.trim()).toBe('Other items');
    expect(section.querySelector('.section-count')?.textContent.trim()).toBe('1 item');
    expect(section.querySelector('.otherItemTitle')?.textContent.trim()).toBe('Photo');
    expect(typeButtons()).toEqual([]);
  });
});
