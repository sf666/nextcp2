import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output, input, output } from '@angular/core';
import { ServerPlaylistDto } from 'src/app/service/dto';
import { PlaylistMode } from '../add-playlist.component';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { server } from 'typescript';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'playlist-container',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './playlist-container.component.html',
  styleUrl: './playlist-container.component.scss',
})
export class PlaylistContainerComponent {
  playlists = input<ServerPlaylistDto[]>();
  playlistMode = input<PlaylistMode>();

  addClicked = output<ServerPlaylistDto>();
  deleteClicked = output<ServerPlaylistDto>();

  PlaylistModeEnum: typeof PlaylistMode = PlaylistMode;

  constructor(public serverPlaylistService: ServerPlaylistService) {
  }

  isPlaylistMode(mode: PlaylistMode): boolean {
    if (mode.valueOf() === this.playlistMode().valueOf()) {
      return true;
    } else {
      return false;
    }
  }

  addTo(serverPlaylist: ServerPlaylistDto) {
    this.addClicked.emit(serverPlaylist);
  }

  deletePlaylist(serverPlaylist: ServerPlaylistDto) {
    this.deleteClicked.emit(serverPlaylist);
  }
}
