import { MusicItemDto } from './../../service/dto.d';
import { PlaylistService } from '../../service/playlist.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss']
})
export class PlaylistComponent implements OnInit {

  constructor(
    public playlistService: PlaylistService) {
  }

  ngOnInit(): void {
    this.playlistService.updatePlaylistItems();
  }

  getActiveClass(item: MusicItemDto) {
    const id: number = +item.objectID;
    if (id === this.playlistService.playlistState.Id) {
      return "active";
    }
  }
}
