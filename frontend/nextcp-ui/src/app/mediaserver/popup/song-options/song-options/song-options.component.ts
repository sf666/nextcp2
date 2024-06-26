import { SongOptionsEvent } from './../song-options-event.d';
import { PopupService } from './../../../../util/popup.service';
import { PlaylistService } from './../../../../service/playlist.service';
import { DownloadService } from './../../../../util/download.service';
import { MusicItemDto, ContainerDto} from './../../../../service/dto.d';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, ElementRef, OnInit, ViewContainerRef, ChangeDetectionStrategy } from '@angular/core';
import { DefaultPlaylistService } from '../../defaut-playlists/default-playlist.service';
import { TransportService } from 'src/app/service/transport.service';
import { StarRatingComponent } from '../../../../view/star-rating/star-rating.component';
import { ServerPlaylistService } from 'src/app/service/server-playlist.service';

@Component({
    selector: 'app-song-options',
    templateUrl: './song-options.component.html',
    styleUrls: ['./song-options.component.scss'],
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [StarRatingComponent]
})
export class SongOptionsComponent implements OnInit {

  private item: MusicItemDto;
  private readonly _matDialogRef: MatDialogRef<SongOptionsComponent>;
  private readonly triggerElementRef: ElementRef;
  private playlistDialogOpen: boolean;
  private currentContainer: ContainerDto;

  constructor(
    private serverPlaylistService: ServerPlaylistService,
    private downloadService: DownloadService,
    private transportService: TransportService,
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
    this.item = data.item;
    this.currentContainer = data.currentContainer;
    console.log("current popup item : " + this.item);
    this._matDialogRef = _matDialogRef;
    this.triggerElementRef = data.trigger;
    this.playlistDialogOpen = false;

    _matDialogRef.afterClosed().subscribe(_res => {
//      this.closeAllDialogs();
    });
  }

  ngOnInit(): void {
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, 300);
  }


  public hasValidSongId(): boolean {
    if (this.item?.songId?.objectID) {
      return true;
    }
    return false;
  }

  download(): void {
    this.downloadService.downloadFileByMBID(this.item, this);
    this.closeThisPopup({type: 'download', data: this.item});
  }

  openAddToPlaylistDialog(event: Event): void {
    if (this.item?.songId.objectID != null) {
      const dialogRef = this.defaultPlaylistService.openAddGlobalPlaylistDialog(this.item);
      this._matDialogRef.close();
    }
  }

  deleteFromPlaylist(): void {
    this.serverPlaylistService.deleteObject(this.item.objectID);
    this.closeThisPopup({type: 'delete', data: this.item});
  }

  closeThisPopup(result: SongOptionsEvent): void {
    this.closeAllDialogs();
    this._matDialogRef.close(result);
  }

  closeAllDialogs(): void {
    this.defaultPlaylistService.close();
  }

  allDialogsClosed(): boolean {
    return !this.playlistDialogOpen;
  }

  actionPlayNext(): void {
    this.transportService.playResourceNext(this.item);
    this.closeThisPopup({type: 'next', data: this.item});
  }

  get selectedMusicItem(): MusicItemDto {
    return this.item;
  }

  get isParentPlaylist(): boolean {
    return this.currentContainer.objectClass === "object.container.playlistContainer";
  }
}
