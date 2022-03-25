import { PopupService } from './../../../../util/popup.service';
import { UuidService } from './../../../../util/uuid.service';
import { PlaylistService } from './../../../../service/playlist.service';
import { ContentDirectoryService } from './../../../../service/content-directory.service';
import { DownloadService } from './../../../../util/download.service';
import { MusicItemDto} from './../../../../service/dto.d';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';
import { AvtransportService } from 'src/app/service/avtransport.service';

@Component({
  selector: 'app-song-options',
  templateUrl: './song-options.component.html',
  styleUrls: ['./song-options.component.scss']
})
export class SongOptionsComponent implements OnInit {

  private item : MusicItemDto;
  private readonly _matDialogRef: MatDialogRef<SongOptionsComponent>;
  private readonly triggerElementRef: ElementRef;
  private playlistDialogOpen: boolean;

  constructor(
    private playlistService: PlaylistService,
    private contentDirectoryService: ContentDirectoryService,
    private downloadService: DownloadService,
    private avtransportService: AvtransportService,
    private defaultPlaylistService: DefaultPlaylistService,
    private uuidService: UuidService,
    private popupService: PopupService,
    _matDialogRef: MatDialogRef<SongOptionsComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, item :  MusicItemDto, event: PointerEvent},
  ) {
    this.item = data.item;
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
    if (this.item?.fileId > 0) {
      return true;
    }
    return false;
  }

  download(): void {
    this.downloadService.downloadFileByMBID(this.item, this);
    this.closeThisPopup();
  }

  openAddToPlaylistDialog(event: Event): void {
    if (this.item?.musicBrainzId?.TrackId) {
      if (!this.playlistDialogOpen) {
        const dialogRef = this.defaultPlaylistService.openAddPlaylistDialogWithParent(event, this.item.fileId, this);
        dialogRef.afterClosed().subscribe(_res => {
          this.playlistDialogOpen = false;
        });
        this.playlistDialogOpen = true;
      }
    }
  }

  deleteFromPlaylist(): void {
    if (this.item?.musicBrainzId?.TrackId) {
      this.playlistService.removeSongFromServerPlaylist(this.item.fileId, this.contentDirectoryService.currentContainerList.currentContainer.title);
    }
    this.closeThisPopup();
    setTimeout( () => { this.contentDirectoryService.refreshCurrentContainer()}, 200);
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

  actionPlayNext() : void {
    this.avtransportService.playResourceNext(this.selectedMusicItem);
    this.closeThisPopup();
  }

  get selectedMusicItem(): MusicItemDto {
    return this.item;
  }

  get isParentPlaylist(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.playlistContainer";
  }
}
