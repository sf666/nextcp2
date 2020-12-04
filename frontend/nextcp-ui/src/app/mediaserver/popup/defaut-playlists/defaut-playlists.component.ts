import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PlaylistService } from './../../../service/playlist.service';
import { Component, OnInit, Inject, ElementRef } from '@angular/core';

@Component({
  selector: 'defaut-playlists',
  templateUrl: './defaut-playlists.component.html',
  styleUrls: ['./defaut-playlists.component.scss']
})
export class DefautPlaylistsComponent implements OnInit {

  private data;
  private readonly dialogRef: MatDialogRef<DefautPlaylistsComponent>;
  constructor(
    _matDialogRef: MatDialogRef<DefautPlaylistsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    public playlistService: PlaylistService
  ) { 
    this.data = data;
    this.dialogRef = _matDialogRef;
  }

  ngOnInit(): void {
  }

  addToPlaylist(playlistName: string) {
    this.playlistService.addToFilesystemPlaylist(this.data.id, playlistName);
    this.dialogRef.close();
  }
}
