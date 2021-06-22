import { PopupService } from './../../../../util/popup.service';
import { UuidService } from './../../../../util/uuid.service';
import { PlaylistService } from './../../../../service/playlist.service';
import { ContentDirectoryService } from './../../../../service/content-directory.service';
import { HttpClient } from '@angular/common/http';
import * as sr from 'streamsaver';
import { DownloadService } from './../../../../util/download.service';
import { MusicItemDto, MusicBrainzId, TrackInfoDto } from './../../../../service/dto.d';
import { MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';
import { AvtransportService } from 'src/app/service/avtransport.service';
import { tap } from 'lodash';

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
    private elRef: ElementRef,
    private playlistService: PlaylistService,
    private contentDirectoryService: ContentDirectoryService,
    private downloadService: DownloadService,
    private avtransportService: AvtransportService,
    private defaultPlaylistService: DefaultPlaylistService,
    private uuidService: UuidService,
    private popupService: PopupService,
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

  ngOnInit(): void {
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 300);
  }


  public hasValidMusicBrainzId(): boolean {
    if (this.data?.item?.musicBrainzId?.TrackId?.length > 0) {
      return this.uuidService.isValidUuid(this.data.item.musicBrainzId.TrackId);
    }
    return false;
  }

  download() {
    this.downloadService.downloadFileByMBID(this.data.item, this);
  }

  openAddToPlaylistDialog() {
    if (this.data?.item?.musicBrainzId?.TrackId) {
      if (!this.playlistDialogOpen) {
        let dialogRef = this.defaultPlaylistService.openAddPlaylistDialogWithParent(this.elRef, this.data.item.musicBrainzId.TrackId, this);
        dialogRef.afterClosed().subscribe(_res => {
          this.playlistDialogOpen = false;
        });
        this.playlistDialogOpen = true;
      }
    }
  }

  deleteFromPlaylist() {
    if (this.data?.item?.musicBrainzId?.TrackId) {
      this.playlistService.removeFromFilesystemPlaylistByMBID(this.data.item.musicBrainzId.TrackId, this.contentDirectoryService.currentContainerList.currentContainer.title);
    }
    close();
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

  get selectedMusicItem(): MusicItemDto {
    return this.data.item;
  }

  get isParentPlaylist(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.playlistContainer";
  }
}
