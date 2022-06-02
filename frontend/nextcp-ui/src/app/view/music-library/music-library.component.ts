import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerItemDto, MediaRendererDto, MediaServerDto, ContainerDto, MusicItemDto } from './../../service/dto.d';
import { Component } from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService]
})

export class MusicLibraryComponent {

  private containerItemDto: ContainerItemDto;

  constructor(
    private contentDirectoryService: ContentDirectoryService) {
    contentDirectoryService.browseFinished$.subscribe(data => this.browseFinished(data));
    contentDirectoryService.searchFinished$.subscribe(data => this.searchFinished(data));
  }

  searchFinished(data: ContainerItemDto): void {
    throw new Error('Method not implemented.');
  }

  browseFinished(data: ContainerItemDto): void {
    this.containerItemDto = data;
  }


  //
  // bindings
  //

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.containerItemDto.currentContainer;
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
