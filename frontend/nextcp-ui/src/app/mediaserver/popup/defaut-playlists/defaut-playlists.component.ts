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

/** Popup width in px. Wide enough for the title's own minimum width. */
const POPUP_WIDTH = 280;

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
    // The row count used to be turned into a pixel height at 20px a row, half of
    // what a row actually is, so every list past a couple of entries lost its
    // bottom. The popup now measures itself.
    this.popupService.configurePopupAtTrigger(
      this._matDialogRef,
      this.triggerElementRef,
      POPUP_WIDTH,
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
