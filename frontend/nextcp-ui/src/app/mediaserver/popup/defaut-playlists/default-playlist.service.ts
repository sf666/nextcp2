import { ContainerDto, MusicItemDto } from './../../../service/dto.d';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DefautPlaylistsComponent } from './defaut-playlists.component';
import { Injectable, ElementRef, inject } from '@angular/core';
import { SongOptionsComponent } from '../song-options/song-options/song-options.component';
import { AddPlaylistComponent } from 'src/app/popup/add-playlist/add-playlist.component';

@Injectable({
  providedIn: 'root',
})
export class DefaultPlaylistService {
  private dialog = inject(MatDialog);

  private dialogRef: MatDialogRef<any, any> | undefined;

  public openAddGlobalPlaylistDialog(
    item: MusicItemDto,
    folder: ContainerDto,
  ): MatDialogRef<any, any> {
    this.dialogRef = this.dialog.open(AddPlaylistComponent, {
      hasBackdrop: false,
      height: '500px',
      width: '700px',
      // Transparent, shadow-less wrapper — the component's glass panel is the
      // whole surface (see app-styles.scss).
      panelClass: ['popup-glass'],
      data: { item: item, container: folder },
    });

    return this.dialogRef;
  }

  public openAddGlobalPlaylistDialogWithBackdrop(
    item: MusicItemDto | undefined,
    folder: ContainerDto,
  ): MatDialogRef<any, any> {
    this.dialogRef = this.dialog.open(AddPlaylistComponent, {
      hasBackdrop: true,
      height: '500px',
      width: '700px',
      panelClass: ['popup-glass'],
      backdropClass: 'glass-backdrop',
      data: { item: item, container: folder },
    });

    return this.dialogRef;
  }

  /**
   * Opens a dialog next to a parent element.
   *
   * @param event
   * @param objectID
   * @param parent
   * @returns
   */
  public openAddPlaylistDialogWithParent(
    event: any,
    objectID: string,
    parent?: SongOptionsComponent,
  ): MatDialogRef<any, any> {
    let target: ElementRef | undefined;

    if (event.target) {
      target = new ElementRef(event.target);
    } else if (event.nativeElement) {
      target = new ElementRef(event.nativeElement);
    }
    this.dialogRef = this.dialog.open(DefautPlaylistsComponent, {
      hasBackdrop: false,
      data: { trigger: target, id: objectID, parentPanel: parent },
    });

    return this.dialogRef;
  }

  public openAddPlaylistDialog(
    event: any,
    objectID: string,
  ): MatDialogRef<any, any> {
    return this.openAddPlaylistDialogWithParent(event, objectID, undefined);
  }

  public close(): void {
    if (this.dialogRef != undefined) {
      this.dialogRef.close();
      this.dialogRef = undefined;
    }
  }
}
