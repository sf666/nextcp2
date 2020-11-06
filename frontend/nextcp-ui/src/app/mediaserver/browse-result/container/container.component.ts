import { ScrollViewService } from './../../../util/scroll-view.service';
import { BackgroundImageService } from './../../../util/background-image.service';
import { delay } from './../../../global';
import { PlaylistService } from './../../../service/playlist.service';
import { ContainerDto } from './../../../service/dto.d';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component,AfterViewChecked } from '@angular/core';

@Component({
  selector: 'browseResultContainer',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.scss']
})
export class ContainerComponent implements AfterViewChecked {

  private scrollID: string = '';

  constructor(
    private contentDirectoryService: ContentDirectoryService,
    private backgroundImageService: BackgroundImageService,
    private scrollViewService: ScrollViewService,
    public playlistService: PlaylistService) { }

  ngAfterViewChecked(): void {
    if (this.scrollID) {
      this.scrollViewService.scrollIntoViewID(this.scrollID);
      this.scrollID = '';
    }

    this.backgroundImageService.setBackgroundImageMainScreen(this.currentContainer.albumartUri);
  }


  public get containerList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
  }

  public get playlistList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
  }

  public containerListWithoutMinimServerTags(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.containerDto.filter(item => !item.title.startsWith(">> "));
  }

  public get currentContainerLabel(): string {
    return this.contentDirectoryService.currentContainerList.currentContainer.title;
  }

  public get currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList.currentContainer;
  }

  public browseTo(containerDto: ContainerDto) {
    if (containerDto.parentID === "[PARENT]") {
      this.scrollID = this.contentDirectoryService.currentContainerList.currentContainer.id;
    }
    else {
      this.scrollID = '';
    }
    this.contentDirectoryService.browseChildren(containerDto.id, containerDto.parentID, containerDto.mediaServerUDN);
  }

  shufflePlaylist(container) {
    this.playlistService.setShuffle(true);
    this.playlistService.addContainerToPlaylistAndPlay(container);
  }

  playPlaylist(container) {
    this.playlistService.setShuffle(false);
    this.playlistService.addContainerToPlaylistAndPlay(container);
  }

  isLeaf(): boolean {
    let isLeaf = this.containerListWithoutMinimServerTags().length < 1;
    return isLeaf;
  }
}
