import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
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
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");   
  }

  ngOnInit(): void {
    this.contentDirectoryService.browseToMyMusic();
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
    return this.contentDirectoryService.getMusicTracks();
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
