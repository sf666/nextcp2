import { TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { Subject } from 'rxjs';
import { DeviceService } from 'src/app/service/device.service';
import { RATING_LIKED, RatingServiceService } from 'src/app/service/rating-service.service';
import { ServerFeature } from 'src/app/service/server-feature';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { ContainerTileComponent } from './container-tile.component';

describe('Album tile favourite control', () => {
  it('toggles the confirmed rating without navigating or starting a long press', () => {
    const liked = new Subject<void>();
    const unliked = new Subject<void>();
    const setResourceRating = vi.fn()
      .mockReturnValueOnce(liked)
      .mockReturnValueOnce(unliked);
    const openOptions = vi.fn();
    TestBed.configureTestingModule({
      imports: [ContainerTileComponent],
      providers: [
        { provide: DeviceService, useValue: {
          hasFeature: (feature: string) => feature === ServerFeature.ALBUM_LIKES,
          selectedMediaServerDevice: () => ({ udn: 'ums', extendedApi: true }),
        } },
        { provide: RatingServiceService, useValue: { setResourceRating } },
        { provide: MatDialog, useValue: { open: openOptions } },
      ],
    });
    const fixture = TestBed.createComponent(ContainerTileComponent);
    const album = {
      ...TestBed.inject(DtoGeneratorService).generateEmptyContainerDto(),
      id: 'album',
      parentID: 'folder',
      title: 'Test album',
      objectClass: 'object.container.album.musicAlbum',
    };
    const browse = vi.fn();
    fixture.componentInstance.browseClicked.subscribe(browse);
    fixture.componentRef.setInput('container', [album]);
    fixture.detectChanges();

    const tile: HTMLElement = fixture.nativeElement.querySelector('.albumTile');
    const heart: HTMLButtonElement = tile.querySelector('.tileLike')!;
    const pointerDown = vi.fn();
    tile.addEventListener('pointerdown', pointerDown);
    heart.dispatchEvent(new MouseEvent('pointerdown', { bubbles: true }));
    expect(pointerDown).not.toHaveBeenCalled();
    expect(heart.getAttribute('aria-pressed')).toBe('false');

    heart.click();
    heart.click();
    expect(setResourceRating).toHaveBeenCalledExactlyOnceWith(
      album.id, album.rating, RATING_LIKED, album.parentID, album.objectClass,
    );
    expect(heart.getAttribute('aria-pressed')).toBe('false');
    liked.next();
    liked.complete();
    fixture.detectChanges();
    expect(heart.getAttribute('aria-pressed')).toBe('true');

    heart.click();
    expect(setResourceRating).toHaveBeenLastCalledWith(
      album.id, RATING_LIKED, undefined, album.parentID, album.objectClass,
    );
    unliked.next();
    unliked.complete();
    fixture.detectChanges();
    expect(heart.getAttribute('aria-pressed')).toBe('false');
    expect(browse).not.toHaveBeenCalled();
    expect(openOptions).not.toHaveBeenCalled();

    tile.click();
    expect(browse).toHaveBeenCalledExactlyOnceWith(album);
  });
});
