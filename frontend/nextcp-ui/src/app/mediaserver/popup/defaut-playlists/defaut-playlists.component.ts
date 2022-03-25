import { PopupService } from './../../../util/popup.service';
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
    private popupService: PopupService,
    public playlistService: PlaylistService
  ) {
    this.data = data;
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const popupHeight = this.playlistService.serverPlaylists.length * 20 + 120;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, popupHeight);
  }

  close() {
    this._matDialogRef.close();
  }

  addToPlaylist(playlistName: string) {
    this.playlistService.addSongToServerPlaylist(this.data.id, playlistName);
    this._matDialogRef.close();
    this.data.parentPanel.close();
  }
}
