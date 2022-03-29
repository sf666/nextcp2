import { MusicItemDto } from './../../../service/dto.d';
import { MatDialog } from '@angular/material/dialog';
import { Injectable, ElementRef } from '@angular/core';
import { SongOptionsComponent } from './song-options/song-options.component';

@Injectable({
  providedIn: 'root'
})
export class SongOptionsServiceService {

  constructor(
    private dialog: MatDialog) { }

  public openOptionsDialog(event: MouseEvent, item: MusicItemDto): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(SongOptionsComponent, {
      hasBackdrop: true,
      data: { trigger: target, item :  item, event: event},
      panelClass: 'popup-rounded'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }
}
