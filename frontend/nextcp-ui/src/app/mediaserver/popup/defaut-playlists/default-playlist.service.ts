import { MusicItemDto } from './../../../service/dto.d';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DefautPlaylistsComponent } from './defaut-playlists.component';
import { Injectable, ElementRef } from '@angular/core';
import { SongOptionsComponent } from '../song-options/song-options/song-options.component';
import { AddPlaylistComponent } from 'src/app/popup/add-playlist/add-playlist.component';

@Injectable({
  providedIn: 'root'
})
export class DefaultPlaylistService {

  private dialogRef : MatDialogRef<any,any>;

  constructor(
    private dialog: MatDialog) {
      this.dialog = dialog;
     }

  public openAddGlobalPlaylistDialog(item: MusicItemDto) : MatDialogRef<any,any> {
    this.dialogRef = this.dialog.open(AddPlaylistComponent, {
      hasBackdrop: false,
      height: '500px',
      width: '600px',
      data: { item: item},
    });

    return this.dialogRef;
  }

  public openAddGlobalPlaylistDialogWithBackdrop(item: MusicItemDto) : MatDialogRef<any,any> {
    this.dialogRef = this.dialog.open(AddPlaylistComponent, {
      hasBackdrop: true,
      height: '500px',
      width: '600px',
      data: { item: item},
    });

    return this.dialogRef;
  }

  /**
   * Opens a dialog next to a parent element.
   * 
   * @param event
   * @param trackId 
   * @param parent 
   * @returns 
   */
  public openAddPlaylistDialogWithParent(event: any, trackId: number, parent: SongOptionsComponent) : MatDialogRef<any,any> {
    let target : ElementRef;

    if (event.target ) {
      target = new ElementRef(event.target);
    } else if (event.nativeElement ) {
      target = new ElementRef(event.nativeElement);
    }
    this.dialogRef = this.dialog.open(DefautPlaylistsComponent, {
      hasBackdrop: false,
      data: { trigger: target, id : trackId, parentPanel: parent},
    });

    return this.dialogRef;
  }

  public openAddPlaylistDialog(event: any, trackId: number) : MatDialogRef<any,any> {
    return this.openAddPlaylistDialogWithParent(event, trackId, null);
  }

  public close(): void {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }
}
