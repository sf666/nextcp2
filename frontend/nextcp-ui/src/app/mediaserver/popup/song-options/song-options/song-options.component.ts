import { MusicItemDto } from './../../../../service/dto.d';
import { MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';
import { AvtransportService } from 'src/app/service/avtransport.service';

@Component({
  selector: 'app-song-options',
  templateUrl: './song-options.component.html',
  styleUrls: ['./song-options.component.scss']
})
export class SongOptionsComponent implements OnInit {

  private data;
  private readonly _matDialogRef: MatDialogRef<SongOptionsComponent>;
  private readonly triggerElementRef: ElementRef;
  private playlistDialogOpen: boolean;

  constructor(
    private avtransportService: AvtransportService,
    private defaultPlaylistService: DefaultPlaylistService,
    _matDialogRef: MatDialogRef<SongOptionsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
  ) {
    this.data = data;
    this._matDialogRef = _matDialogRef;
    this.triggerElementRef = data.trigger;
    this.playlistDialogOpen = false;

    _matDialogRef.afterClosed().subscribe(_res => {
      this.closeAllDialogs();
    });     
  }

  openAddToPlaylistDialog() {
    if (this.data?.item?.musicBrainzId?.TrackId) {
      if (!this.playlistDialogOpen) {
        let dialogRef =  this.defaultPlaylistService.openAddPlaylistDialogWithParent(this.data.event, this.data.item.musicBrainzId.TrackId, this);
        dialogRef.afterClosed().subscribe(_res => {
          this.playlistDialogOpen = false;
        });        
        this.playlistDialogOpen = true;
      }
    }
  }

  close() {
    this.closeAllDialogs();
    this._matDialogRef.close();
  }

  closeAllDialogs() {
    this.defaultPlaylistService.close();
  }

  allDialogsClosed(): boolean {
    return !this.playlistDialogOpen;
  }

  actionPlayNext() {
    this.avtransportService.playResourceNext(this.selectedMusicItem);
  }

  ngOnInit(): void {
    const matDialogConfig: MatDialogConfig = new MatDialogConfig();
    const rect = this.triggerElementRef.nativeElement.getBoundingClientRect();
    matDialogConfig.position = { left: `${rect.left - 200}px`, top: `${rect.bottom - 20}px` };
    matDialogConfig.width = '250px';
    matDialogConfig.height = '200px';

    this._matDialogRef.updateSize(matDialogConfig.width, matDialogConfig.height);
    this._matDialogRef.updatePosition(matDialogConfig.position);
    this._matDialogRef.addPanelClass('popup');
  }

  get selectedMusicItem(): MusicItemDto {
    return this.data.item;
  }
}
