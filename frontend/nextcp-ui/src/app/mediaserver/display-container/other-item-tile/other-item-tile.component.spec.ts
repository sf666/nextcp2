import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EMPTY } from 'rxjs';
import { vi } from 'vitest';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';

import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';
import { OtherItemTileComponent } from './other-item-tile.component';

describe('OtherItemTileComponent', () => {
  let component: OtherItemTileComponent;
  let fixture: ComponentFixture<OtherItemTileComponent>;
  const openOptionsDialog = vi.fn(() => EMPTY);

  beforeEach(async () => {
    openOptionsDialog.mockClear();
    await TestBed.configureTestingModule({
      imports: [OtherItemTileComponent],
      providers: [
        { provide: SongOptionsServiceService, useValue: { openOptionsDialog } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(OtherItemTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each(['videoItem', 'imageItem'])('opens song options for a playlist %s without starting playback', (kind) => {
    const dtos = TestBed.inject(DtoGeneratorService);
    const playlist = {
      ...dtos.generateEmptyContainerDto(),
      id: 'playlist',
      objectClass: 'object.container.playlistContainer',
    };
    const item = {
      ...dtos.emptyItemDto(),
      objectID: 'entry',
      objectClass: 'object.item.' + kind,
    };
    const play = vi.fn();
    component.playItemClicked.subscribe(play);
    fixture.componentRef.setInput('otherItems', [item]);
    fixture.componentRef.setInput('currentContainer', playlist);
    fixture.detectChanges();

    const options: HTMLButtonElement = fixture.nativeElement.querySelector('button');
    options.click();

    expect(openOptionsDialog).toHaveBeenCalledExactlyOnceWith(
      expect.any(MouseEvent),
      item,
      playlist,
    );
    expect(play).not.toHaveBeenCalled();

    const tile: HTMLElement = fixture.nativeElement.querySelector('.albumTile');
    tile.click();
    expect(play).toHaveBeenCalledExactlyOnceWith(item);
  });
});
