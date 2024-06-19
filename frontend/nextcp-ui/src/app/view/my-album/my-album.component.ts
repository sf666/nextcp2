import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContainerDto, MusicItemDto, MediaServerDto } from './../../service/dto.d';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { Component, OnInit } from '@angular/core';
import { DisplayContainerComponent } from '../../mediaserver/display-container/display-container.component';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { Router } from '@angular/router';

@Component({
    selector: 'myAlbums',
    templateUrl: './my-album.component.html',
    styleUrls: ['./my-album.component.scss'],
    providers: [ContentDirectoryService],
    standalone: true,
    imports: [NavBarComponent, DisplayContainerComponent]
})
export class MyAlbumComponent implements OnInit {

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private router: Router,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");
    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    this.loadMyAlbums();
  }

  ngOnInit(): void {
    this.layoutService.setFramedView();
    this.loadMyAlbums();
  }

  private loadMyAlbums() {
    const oid = "$DBID$MYMUSIC$";
    if (this.deviceService.selectedMediaServerDevice().udn) {
      this.contentDirectoryService.browseChildren(oid, "", this.deviceService.selectedMediaServerDevice().udn).subscribe();
    }
  }

  hasExtendedApi(): boolean {
    return this.deviceService.selectedMediaServerDeviceHasExtendedApi;
  }

  //
  // Event
  //

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList().parentFolderTitle;
  }

  public backButtonPressed(event: any) {
    this.loadMyAlbums();
  }


  containerSelected(event: ContainerDto) {
    
  }

  //
  // bindings
  //
  getContentHandler(): ScrollLoadHandler {
    return {contentDirectoryService: this.contentDirectoryService, persistenceService: null }
  }

  backButtonDisabled() {
    return false;
  }

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList().currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_;
  }

  otherItems_(): MusicItemDto[] {
    return this.contentDirectoryService.otherItems_;
  }

  albums(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList().albumDto;
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
