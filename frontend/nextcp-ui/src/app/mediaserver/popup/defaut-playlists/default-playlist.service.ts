import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DefautPlaylistsComponent } from './defaut-playlists.component';
import { Injectable, ElementRef } from '@angular/core';
import { SongOptionsComponent } from '../song-options/song-options/song-options.component';

@Injectable({
  providedIn: 'root'
})
export class DefaultPlaylistService {

  private dialogRef : MatDialogRef<any,any>;

  constructor(
    private dialog: MatDialog) {
      this.dialog = dialog;
     }

  public openAddPlaylistDialogWithParent(event: any, mbid: string, parent: SongOptionsComponent) : MatDialogRef<any,any> {
    const target = new ElementRef(event.target);
    this.dialogRef = this.dialog.open(DefautPlaylistsComponent, {
      hasBackdrop: false,
      data: { trigger: target, id :  mbid, parentPanel: parent},
      panelClass: 'popup-rounded'
    });

    return this.dialogRef;
  }

  public openAddPlaylistDialog(event: any, mbid: string) : MatDialogRef<any,any> {
    return this.openAddPlaylistDialogWithParent(event, mbid, null);
  }

  public close() {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }
}
