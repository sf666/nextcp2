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
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { PlaylistService } from 'src/app/service/playlist.service';
import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { DeviceService } from 'src/app/service/device.service';
import { FormsModule, NgModel } from '@angular/forms';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';
import { PlaylistContainerComponent } from './playlist-container/playlist-container.component';

export enum PlaylistMode {
  Add,
  Create,
  Delete,
}

@Component({
  selector: 'app-playlist-management',
  standalone: true,
  imports: [MatFormField, MatButton, MatInput, MatIconModule, FormsModule, PlaylistContainerComponent],
  templateUrl: './add-playlist.component.html',
  styleUrl: './add-playlist.component.scss',
})

export class AddPlaylistComponent {
  PlaylistModeEnum: typeof PlaylistMode = PlaylistMode;
  private musicItemToAdd: MusicItemDto;
  private otherPlaylists: ServerPlaylistDto[];
  playlistFilter: string = "";
  playlistMode : PlaylistMode;
  newPlaylistName = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) data: { item: MusicItemDto },
    public serverPlaylistService: ServerPlaylistService,
    deviceService: DeviceService,
    private contentDirectoryService: ContentDirectoryService,
    private dtoGeneratorService: DtoGeneratorService,
    public dialogRef: MatDialogRef<AddPlaylistComponent>,
  ) {
    this.musicItemToAdd = data.item;
    if (data.item) {
      this.playlistMode = PlaylistMode.Add;
    } else {
      this.playlistMode = PlaylistMode.Create;
    }
    let sr = dtoGeneratorService.generateEmptySearchRequestDto();    
    sr.searchRequest = '';
    sr.mediaServerUDN = deviceService.selectedMediaServerDevice.udn;
    this.contentDirectoryService.searchAllPlaylist(sr).subscribe(data => this.updateAllPlaylists(data));
  }

  private updateAllPlaylists(data : SearchResultDto) : void {
    let newPl : ServerPlaylistDto[] = [];

    let other = data.playlistItems.filter((spe) => !this.serverPlaylistService.playlistIdExistsInServerPlaylists(spe.id));
    other?.forEach(pl => {
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

  getRecentPlaylists(): ServerPlaylistDto[] {
    if (this.serverPlaylistService?.recentServerPl?.serverPlaylists?.length > 0) {
      return this.serverPlaylistService.recentServerPl.serverPlaylists.filter(pl => pl.playlistName.toLowerCase().includes(this.playlistFilter.toLowerCase()));
    }
    return [];
  }

  getRecentPlaylistsCount() : number {
    return this.getRecentPlaylists()?.length
  }

  getServerPlaylists(): ServerPlaylistDto[] {
    if (this.serverPlaylistService?.serverPl?.serverPlaylists?.length > 0) {
      return this.serverPlaylistService.serverPl.serverPlaylists.filter(pl => pl.playlistName.toLowerCase().includes(this.playlistFilter.toLowerCase()));
    }
    return [];
  }

  getServerPlaylistsCount(): number {
    return this.getServerPlaylists()?.length;
  }

  getOtherPlaylists(): ServerPlaylistDto[] {
    if (this.otherPlaylists) {
      return this.otherPlaylists.filter(pl => pl.playlistName.toLowerCase().includes(this.playlistFilter.toLowerCase()));
    }
    return [];
  }
  
  getOtherPlaylistsCount(): number {
    return this.getOtherPlaylists()?.length;
  }
  
  addTo(serverPlaylist: ServerPlaylistDto) {
    this.serverPlaylistService.addSongToServerPlaylist(
      this.musicItemToAdd.objectID,
      serverPlaylist.playlistId,
    );
    this.close();
  }

  deletePlaylist(serverPlaylist: ServerPlaylistDto) {
    this.serverPlaylistService.deleteObject(serverPlaylist.playlistId).subscribe({
      next: (data) => {
        this.serverPlaylistService.serverPl.serverPlaylists = this.serverPlaylistService.serverPl.serverPlaylists.filter(
          pl => pl.playlistId !== serverPlaylist.playlistId);
      },
      error: (data) => {console.error(data);}
      
    }
    );
  }

  close() : void {
    this.dialogRef.close();
  }

  addPlaylistClick() : void {
    this.playlistMode = PlaylistMode.Add;
  }

  newPlaylistClick() : void {
    this.playlistMode = PlaylistMode.Create;
  }

  deletePlaylistClick() : void {
    this.playlistMode = PlaylistMode.Delete;
  }

  playlistActiveClass(mode: PlaylistMode) : string {
    if (mode.valueOf() === this.playlistMode.valueOf() ) {
      return "active";
    } else {
      return "inactive";
    }
  }

  isPlaylistMode(mode: PlaylistMode) : boolean {
    if (mode.valueOf() === this.playlistMode.valueOf() ) {
      return true;
    } else {
      return false;
    }
  }

  addDisabled() : boolean {
    return this.newPlaylistName.length == 0;
  }

  createPlaylistClicked() : void {
    this.serverPlaylistService.createPlaylist(this.newPlaylistName).subscribe(newId => this.newPlaylistId(newId))
    this.close();
  }

  cancelClicked() : void {
    this.newPlaylistName = '',
    this.close();
  }

  private newPlaylistId(newId : string) {
    this.addTo({playlistId:newId, albumArtUrl:'', playlistName:'', numberOfElements: 0, totalPlaytime:''});
    this.serverPlaylistService.updateServerAccessiblePlaylists();
  }

  get musicItemToAddExists() : boolean {
    return this.musicItemToAdd?.objectID?.length > 0;
  }
}
