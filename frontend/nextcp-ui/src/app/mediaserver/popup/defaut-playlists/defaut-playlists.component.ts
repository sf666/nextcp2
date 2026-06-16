import { PopupService } from './../../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  Component,
  OnInit,
  ElementRef,
  ChangeDetectionStrategy,
  inject,
} from '@angular/core';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';

@Component({
  selector: 'defaut-playlists',
  templateUrl: './defaut-playlists.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./defaut-playlists.component.scss'],
  standalone: true,
})
export class DefautPlaylistsComponent implements OnInit {
  private popupService = inject(PopupService);
  serverPlaylistService = inject(ServerPlaylistService);

  private data: {
    trigger: ElementRef;
    id: string;
    parentPanel?: { closeThisPopup: () => void };
  };
  private readonly _matDialogRef: MatDialogRef<DefautPlaylistsComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<DefautPlaylistsComponent>>(MatDialogRef);
    const data = inject<{
      trigger: ElementRef;
      id: string;
    }>(MAT_DIALOG_DATA);

    this.data = data;
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const popupHeight =
      this.serverPlaylistService.serverPl().serverPlaylists.length * 20 + 120;
    this.popupService.configurePopupPosition(
      this._matDialogRef,
      this.triggerElementRef,
      250,
      popupHeight,
    );
  }

  close() {
    this._matDialogRef.close();
  }

  addToPlaylist(playlistName: string) {
    this.serverPlaylistService.addSongToServerPlaylist(
      this.data.id,
      playlistName,
    );
    this._matDialogRef.close();
    this.data.parentPanel?.closeThisPopup();
  }
}
