import { Observable } from 'rxjs';
import { MusicItemDto, ContainerDto } from './../../../service/dto.d';
import { MatDialog } from '@angular/material/dialog';
import { Injectable, ElementRef, inject } from '@angular/core';
import { SongOptionsComponent } from './song-options/song-options.component';

@Injectable({
  providedIn: 'root',
})
export class SongOptionsServiceService {
  private dialog = inject(MatDialog);

  public openOptionsDialog(
    event: MouseEvent,
    item: MusicItemDto,
    currentContainer?: ContainerDto,
  ): Observable<any> {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(SongOptionsComponent, {
      // No height here: the component measures itself and asks PopupService for
      // the size it actually needs, so a shorter menu is not padded out with
      // empty glass and a longer one is not cut off.
      hasBackdrop: true,
      // Transparent, shadow-less dialog wrapper — the component's own glass
      // panel is the whole surface. Without this the Material surface keeps its
      // elevation shadow and, since the pane is taller than the menu, that
      // shadow shows up as a dark block below the popup.
      panelClass: ['popup-glass'],
      data: {
        trigger: target,
        item: item,
        event: event,
        currentContainer: currentContainer,
      },
    });
    return dialogRef.afterClosed();
  }
}
