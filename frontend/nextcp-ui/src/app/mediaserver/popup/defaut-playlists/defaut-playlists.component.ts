import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { PlaylistService } from './../../../service/playlist.service';
import { Component, OnInit, Inject, ElementRef } from '@angular/core';

@Component({
  selector: 'defaut-playlists',
  templateUrl: './defaut-playlists.component.html',
  styleUrls: ['./defaut-playlists.component.scss']
})
export class DefautPlaylistsComponent implements OnInit {

  private data;
  private readonly _matDialogRef: MatDialogRef<DefautPlaylistsComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    _matDialogRef: MatDialogRef<DefautPlaylistsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    public playlistService: PlaylistService
  ) { 
    this.data = data;
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const matDialogConfig: MatDialogConfig = new MatDialogConfig();
    const rect = this.triggerElementRef.nativeElement.getBoundingClientRect();
    matDialogConfig.position = { left: `${rect.left - 455}px`, top: `${rect.bottom - 20}px` };
    matDialogConfig.width = '250px';
    matDialogConfig.height = '200px';

    this._matDialogRef.updateSize(matDialogConfig.width, matDialogConfig.height);
    this._matDialogRef.updatePosition(matDialogConfig.position);
    this._matDialogRef.addPanelClass('popup');
  }

  close() {
    this._matDialogRef.close();
  }

  addToPlaylist(playlistName: string) {
    this.playlistService.addToFilesystemPlaylistByMBID(this.data.id, playlistName);
    this._matDialogRef.close();
    this.data.parentPanel.close();
  }
}
