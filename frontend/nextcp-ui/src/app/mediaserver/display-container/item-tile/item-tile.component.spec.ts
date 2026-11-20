import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { RendererService } from 'src/app/service/renderer.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';
import { ItemTileComponent } from './item-tile.component';

describe('Search result artwork', () => {
  it.each([1, 2])('shows covers for %i hits from the same album in list view', (count) => {
    TestBed.configureTestingModule({
      imports: [ItemTileComponent],
      providers: [
        { provide: RendererService, useValue: { currentTrack: () => undefined } },
        { provide: SongOptionsServiceService, useValue: {} },
      ],
    });
    const isSearchResultDisplayed = signal(false);
    const dtos = TestBed.inject(DtoGeneratorService);
    const tracks = Array.from({ length: count }, (_, index) => ({
      ...dtos.emptyItemDto(),
      objectID: 'track' + index,
      album: 'One album',
      albumArtUrl: '/cover' + index + '.jpg',
      numberOfThisDisc: (index + 1).toString(),
    }));
    const fixture = TestBed.createComponent(ItemTileComponent);
    fixture.componentRef.setInput('tracks', tracks);
    fixture.componentRef.setInput('isListView', true);
    fixture.componentRef.setInput('extendedApi', false);
    fixture.componentRef.setInput('contentDirectoryService', {
      albumIdExists: () => false,
      currentContainerList: () => ({ totalMatches: count }),
      isSearchResultDisplayed,
    });
    fixture.detectChanges();

    const list: HTMLElement = fixture.nativeElement.querySelector('.track-list');
    expect(list.classList.contains('same-album')).toBe(true);
    expect(list.querySelector('.track-thumb')).toBeNull();

    isSearchResultDisplayed.set(true);
    fixture.detectChanges();

    expect(list.classList.contains('same-album')).toBe(false);
    expect(list.querySelector('.disc-label')).toBeNull();
    expect(Array.from(list.querySelectorAll('.track-thumb'), (img) => img.getAttribute('src')))
      .toEqual(tracks.map((track) => track.albumArtUrl));
  });
});
