import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContainerDto, MusicItemDto, MediaServerDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'myAlbums',
  templateUrl: './my-album.component.html',
  styleUrls: ['./my-album.component.scss'],
  providers: [ContentDirectoryService]
})
export class MyAlbumComponent implements OnInit {

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");
    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    // TODO check if media server has extended api
  }

  ngOnInit(): void {
    this.loadMyAlbums();
  }

  private loadMyAlbums() {
    const oid = "$DBID$MYMUSIC$";
    this.contentDirectoryService.browseChildren(oid, "", this.deviceService.selectedMediaServerDevice.udn).subscribe();
  }

  hasExtendedApi(): boolean {
    return this.deviceService.selectedMediaServerDeviceHasExtendedApi;
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) {
    this.contentDirectoryService.browseChildrenByContiner(event);
  }

  //
  // bindings
  //

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_;
  }

  otherItems_(): MusicItemDto[] {
    return this.contentDirectoryService.otherItems_;
  }

  albums(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.albumDto;
  }

  playlists(): ContainerDto[] {
    return this.contentDirectoryService.playlistList_;
  }

  otherContainer(): ContainerDto[] {
    return this.contentDirectoryService.containerList_;
  }

  scrollToID(): string {
    return "";
  }
}
