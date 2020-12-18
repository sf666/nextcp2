import { MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';

@Component({
  selector: 'app-song-options',
  templateUrl: './song-options.component.html',
  styleUrls: ['./song-options.component.scss']
})
export class SongOptionsComponent implements OnInit {

  private data;
  private readonly _matDialogRef: MatDialogRef<SongOptionsComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    private defaultPlaylistService: DefaultPlaylistService,
    _matDialogRef: MatDialogRef<SongOptionsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
  ) {
    this.data = data;
    this._matDialogRef = _matDialogRef;
    this.triggerElementRef = data.trigger;
  }

  openAddToPlaylistDialog() {
    if (this.data?.item?.musicBrainzId?.TrackId) {
      this.defaultPlaylistService.openAddPlaylistDialog(this.data.event, this.data.item.musicBrainzId.TrackId);
      this._matDialogRef.close();
    }
  }

  ngOnInit(): void {
    const matDialogConfig: MatDialogConfig = new MatDialogConfig();
    const rect = this.triggerElementRef.nativeElement.getBoundingClientRect();
    matDialogConfig.position = { left: `${rect.left - 200}px`, top: `${rect.bottom - 20}px` };
    matDialogConfig.width = '220px';
    matDialogConfig.height = '200px';

    this._matDialogRef.updateSize(matDialogConfig.width, matDialogConfig.height);
    this._matDialogRef.updatePosition(matDialogConfig.position);
    this._matDialogRef.addPanelClass('popup');
  }
}
