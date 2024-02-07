import { MusicItemDto, ServerPlaylistDto, ServerPlaylists } from './../../service/dto.d';
import { Component, Inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormField } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { PlaylistService } from 'src/app/service/playlist.service';

@Component({
  selector: 'app-add-playlist',
  standalone: true,
  imports: [MatFormField, MatButton, MatInput, MatIcon],
  templateUrl: './add-playlist.component.html',
  styleUrl: './add-playlist.component.scss',
})
export class AddPlaylistComponent {
  private musicItemToAdd : MusicItemDto;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: { item: MusicItemDto },
    public playlistService: PlaylistService,
    public dialogRef: MatDialogRef<AddPlaylistComponent>,
  ) {
    this.musicItemToAdd = data.item;
  }

  public cancel() : void{
    this.dialogRef.close();
  }

  getServerPlaylists(): ServerPlaylistDto[] {
    return this.playlistService.serverPl.serverPlaylists;
  }

  addTo(serverPlaylist: ServerPlaylistDto) {
    this.playlistService.addSongToServerPlaylist(this.musicItemToAdd.songId.umsAudiotrackId.toString(), serverPlaylist.playlistId);
//    this.playlistService.touchPlaylist(playlistName);
    this.dialogRef.close();
  }
}
