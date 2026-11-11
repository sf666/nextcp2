import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of, throwError } from 'rxjs';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { CdsUpdateService } from 'src/app/service/cds-update.service';
import { UpdateAlbumArtComponent } from './update-album-art.component';

describe('UpdateAlbumArtComponent', () => {
  const data = {
    objectId: 'track-1',
    previousAlbumArtUri: '/old.jpg',
    containerId: 'album-1',
  };
  const ids = { objectID: 'track-1', acoustID: '', musicBrainzIdTrackId: '' };
  const dialog = { close: vi.fn(), disableClose: false };
  const setNewAlbumArtUri = vi.fn();
  let component: UpdateAlbumArtComponent;
  let element: HTMLElement;

  beforeEach(() => {
    dialog.close.mockReset();
    setNewAlbumArtUri.mockReset().mockReturnValue(of(undefined));
    TestBed.configureTestingModule({
      imports: [UpdateAlbumArtComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: data },
        { provide: MatDialogRef, useValue: dialog },
        { provide: CdsUpdateService, useValue: { setNewAlbumArtUri } },
      ],
    });
    const fixture = TestBed.createComponent(UpdateAlbumArtComponent);
    fixture.detectChanges();
    component = fixture.componentInstance;
    element = fixture.nativeElement;
  });

  afterEach(() => TestBed.resetTestingModule());

  it('sends the selected file bytes unchanged through the artwork update', async () => {
    element.querySelectorAll<HTMLInputElement>('input[type=radio]')[1].click();
    await TestBed.tick();
    const input = element.querySelector<HTMLInputElement>('input[type=file]')!;
    const bytes = new Uint8Array([137, 80, 78, 71]);
    Object.defineProperty(input, 'files', {
      value: [new File([bytes], 'cover.png', { type: 'image/png' })],
    });
    input.dispatchEvent(new Event('change'));

    await component.save();

    expect(setNewAlbumArtUri).toHaveBeenCalledWith(
      ids,
      data.previousAlbumArtUri,
      'data:image/png;base64,iVBORw==',
      data.containerId,
    );
    expect(dialog.close).toHaveBeenCalledOnce();
  });

  it('keeps the URL available for retry when the server rejects the update', async () => {
    setNewAlbumArtUri.mockReturnValueOnce(
      throwError(() => new Error('Update failed')),
    );
    component.url.set('https://example.com/cover.jpg');

    await component.save();

    expect(dialog.close).not.toHaveBeenCalled();
    expect(component.canSave()).toBe(true);
    await component.save();
    expect(setNewAlbumArtUri).toHaveBeenLastCalledWith(
      ids,
      data.previousAlbumArtUri,
      'https://example.com/cover.jpg',
      data.containerId,
    );
    expect(dialog.close).toHaveBeenCalledOnce();
  });
});
