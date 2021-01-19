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
    const matDialogConfig: MatDialogConfig = new MatDialogConfig();
    const rect = this.triggerElementRef.nativeElement.getBoundingClientRect();
    matDialogConfig.position = { left: `${rect.left - 200}px`, top: `${rect.bottom - 20}px` };
    matDialogConfig.width = '250px';
    matDialogConfig.height = '300px';

    this._matDialogRef.updateSize(matDialogConfig.width, matDialogConfig.height);
    this._matDialogRef.updatePosition(matDialogConfig.position);
    this._matDialogRef.addPanelClass('popup');
  }


  public hasValidMusicBrainzId(): boolean {
    if (this.data?.item?.musicBrainzId?.TrackId?.length > 0) {
      return "00000000-0000-0000-0000-000000000000" !== this.data.item.musicBrainzId.TrackId;
    }
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
    // this.contentDirectoryService.currentContainerList.currentContainer
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

  get isParentPlaylist() : boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.playlistContainer";
  }
}
