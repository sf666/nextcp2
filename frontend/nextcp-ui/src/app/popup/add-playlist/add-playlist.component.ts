import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import {
  MusicItemDto,
  ServerPlaylistDto,
  ServerPlaylists,
  SearchRequestDto,
  SearchResultDto,
  ContainerDto,
} from './../../service/dto.d';
import { Component, Inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormField } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { PlaylistService } from 'src/app/service/playlist.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { DeviceService } from 'src/app/service/device.service';
import { FormsModule, NgModel } from '@angular/forms';

@Component({
  selector: 'app-add-playlist',
  standalone: true,
  imports: [MatFormField, MatButton, MatInput, MatIcon, FormsModule],
  templateUrl: './add-playlist.component.html',
  styleUrl: './add-playlist.component.scss',
})
export class AddPlaylistComponent {
  private musicItemToAdd: MusicItemDto;
  private otherPlaylists: ServerPlaylistDto[];
  playlistFilter: string = "";

  constructor(
    @Inject(MAT_DIALOG_DATA) data: { item: MusicItemDto },
    public playlistService: PlaylistService,
    deviceService: DeviceService,
    private contentDirectoryService: ContentDirectoryService,
    private dtoGeneratorService: DtoGeneratorService,
    public dialogRef: MatDialogRef<AddPlaylistComponent>,
  ) {
    this.musicItemToAdd = data.item;
    let sr = dtoGeneratorService.generateEmptySearchRequestDto();
    sr.searchRequest = '';
    sr.mediaServerUDN = deviceService.selectedMediaServerDevice.udn;
    this.contentDirectoryService.searchAllPlaylist(sr).subscribe(data => this.updateAllPlaylists(data));
  }

  private updateAllPlaylists(data : SearchResultDto) : void {
    let newPl : ServerPlaylistDto[] = [];

    let other = data.playlistItems.filter((spe) => !this.playlistService.playlistIdExistsInServerPlaylists(spe.id));
    other.forEach(pl => {
      const entry = {} as ServerPlaylistDto;
      entry.albumArtUrl = pl.albumartUri,
      entry.playlistId = pl.id,
      entry.playlistName = pl.title,
      newPl.push(entry);
    })
    this.otherPlaylists = newPl;
  }

  public cancel(): void {
    this.dialogRef.close();
  }

  getServerPlaylists(): ServerPlaylistDto[] {
    if (this.playlistService.serverPl.serverPlaylists) {
      return this.playlistService.serverPl.serverPlaylists.filter(pl => pl.playlistName.toLowerCase().includes(this.playlistFilter.toLowerCase()));
    }
    return [];
  }

  getOtherPlaylists(): ServerPlaylistDto[] {
    if (this.otherPlaylists) {
      return this.otherPlaylists.filter(pl => pl.playlistName.toLowerCase().includes(this.playlistFilter.toLowerCase()));
    }
    return [];
  }

  addTo(serverPlaylist: ServerPlaylistDto) {
    this.playlistService.addSongToServerPlaylist(
      this.musicItemToAdd.objectID,
      serverPlaylist.playlistId,
    );
    this.close();
  }

  close() : void {
    this.dialogRef.close();
  }
}
