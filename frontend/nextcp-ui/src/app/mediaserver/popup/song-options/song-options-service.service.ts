import { Observable } from 'rxjs';
import { MusicItemDto, ContainerDto } from './../../../service/dto.d';
import { MatDialog } from '@angular/material/dialog';
import { Injectable, ElementRef, ViewContainerRef } from '@angular/core';
import { SongOptionsComponent } from './song-options/song-options.component';

@Injectable({
  providedIn: 'root'
})
export class SongOptionsServiceService {

  constructor(
    private dialog: MatDialog) { }

  public openOptionsDialog(event: MouseEvent, item: MusicItemDto, currentContainer: ContainerDto): Observable<any> {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(SongOptionsComponent, {
      hasBackdrop: true,
      data: { trigger: target, item: item, event: event, currentContainer: currentContainer },
      panelClass: 'popup-rounded'
    });
    return dialogRef.afterClosed();
  }
}
