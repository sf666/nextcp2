import { MatDialog } from '@angular/material/dialog';
import { DefautPlaylistsComponent } from './defaut-playlists.component';
import { Injectable, ElementRef } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DefaultPlaylistService {

  constructor(
    private dialog: MatDialog) { }

  public openAddPlaylistDialog(event: any, mbid: string) {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(DefautPlaylistsComponent, {
      data: { trigger: target, id :  mbid},
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }
}
