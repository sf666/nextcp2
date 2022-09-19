import { PopupService } from './../../../../util/popup.service';
import { PlaylistService } from './../../../../service/playlist.service';
import { ContentDirectoryService } from './../../../../service/content-directory.service';
import { DownloadService } from './../../../../util/download.service';
import { MusicItemDto, ContainerDto, ContainerItemDto } from './../../../../service/dto.d';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit, ViewContainerRef } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';
import { AvtransportService } from 'src/app/service/avtransport.service';

@Component({
  selector: 'app-song-options',
  templateUrl: './song-options.component.html',
  styleUrls: ['./song-options.component.scss']
})
export class SongOptionsComponent implements OnInit {

  private item: MusicItemDto;
  private readonly _matDialogRef: MatDialogRef<SongOptionsComponent>;
  private readonly triggerElementRef: ElementRef;
  private playlistDialogOpen: boolean;
  private currentContainer: ContainerDto;
  private viewContainerRef: ViewContainerRef;

  constructor(
    private playlistService: PlaylistService,
    private downloadService: DownloadService,
    private avtransportService: AvtransportService,
    private defaultPlaylistService: DefaultPlaylistService,
    private popupService: PopupService,
    _matDialogRef: MatDialogRef<SongOptionsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { 
      trigger: ElementRef, 
      item: MusicItemDto, 
      event: PointerEvent,
      viewContainerRef: ViewContainerRef,
      currentContainer: ContainerDto
      },
  ) {
    this.viewContainerRef = data.viewContainerRef;
    this.item = data.item;
    this.currentContainer = data.currentContainer;
    console.log("current popup item : " + this.item);
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


  public hasValidSongId(): boolean {
    if (this.item?.songId.umsAudiotrackId > 0) {
      return true;
    }
    console.log("song has no ums audio track id : " + this.item);
    return false;
  }

  download(): void {
    this.downloadService.downloadFileByMBID(this.item, this);
    this.closeThisPopup();
  }

  openAddToPlaylistDialog(event: Event): void {
    if (this.item?.songId.umsAudiotrackId != null) {
      if (!this.playlistDialogOpen) {
        const dialogRef = this.defaultPlaylistService.openAddPlaylistDialogWithParent(event, this.item.songId.umsAudiotrackId, this);
        dialogRef.afterClosed().subscribe(_res => {
          this.playlistDialogOpen = false;
        });
        this.playlistDialogOpen = true;
      }
    }
  }

  deleteFromPlaylist(): void {
    if (this.item?.songId.umsAudiotrackId != null) {
      this.playlistService.removeSongFromServerPlaylist(this.item.songId.umsAudiotrackId, this.currentContainer.title);
    }
    this.closeThisPopup();
    setTimeout(() => { 
      this.viewContainerRef; 
    }, 200);
  }

  closeThisPopup(): void {
    this.closeAllDialogs();
    this._matDialogRef.close();
  }

  closeAllDialogs(): void {
    this.defaultPlaylistService.close();
  }

  allDialogsClosed(): boolean {
    return !this.playlistDialogOpen;
  }

  actionPlayNext(): void {
    this.avtransportService.playResourceNext(this.selectedMusicItem);
    this.closeThisPopup();
  }

  get selectedMusicItem(): MusicItemDto {
    return this.item;
  }

  get isParentPlaylist(): boolean {
    return this.currentContainer.objectClass === "object.container.playlistContainer";
  }
}
