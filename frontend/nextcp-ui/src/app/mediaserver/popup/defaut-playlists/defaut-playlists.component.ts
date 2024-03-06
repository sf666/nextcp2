import { PopupService } from './../../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject, ElementRef } from '@angular/core';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';

@Component({
    selector: 'defaut-playlists',
    templateUrl: './defaut-playlists.component.html',
    styleUrls: ['./defaut-playlists.component.scss'],
    standalone: true
})
export class DefautPlaylistsComponent implements OnInit {

  private data;
  private readonly _matDialogRef: MatDialogRef<DefautPlaylistsComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    _matDialogRef: MatDialogRef<DefautPlaylistsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    private popupService: PopupService,
    public serverPlaylistService: ServerPlaylistService
  ) {
    this.data = data;
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const popupHeight = this.serverPlaylistService.serverPl.serverPlaylists.length * 20 + 120;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, popupHeight);
  }

  close() {
    this._matDialogRef.close();
  }

  addToPlaylist(playlistName: string) {
    this.serverPlaylistService.addSongToServerPlaylist(this.data.id, playlistName);
    this._matDialogRef.close();
    this.data.parentPanel.closeThisPopup();
  }
}
