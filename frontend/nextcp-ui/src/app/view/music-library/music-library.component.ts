import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto } from './../../service/dto.d';
import { Component } from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService]
})

export class MusicLibraryComponent {

  constructor(
    public layoutService: LayoutService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MusicLibraryComponent");
  }


  //
  // Nav-Bar bindngs
  //
  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }

  public backButtonPressed(event: any) {
    this.contentDirectoryService.gotoParent();
  }

  public backButtonDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
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
