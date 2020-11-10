import { ScrollViewService } from './../../../util/scroll-view.service';
import { BackgroundImageService } from './../../../util/background-image.service';
import { delay } from './../../../global';
import { PlaylistService } from './../../../service/playlist.service';
import { ContainerDto, ContainerItemDto } from './../../../service/dto.d';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component, AfterViewChecked } from '@angular/core';

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

  public get itemsCount(): number {
    if (this.contentDirectoryService.currentContainerList?.musicItemDto?.length) {
      return this.contentDirectoryService.currentContainerList.musicItemDto.length;
    } else {
      return 0;
    }
  }

  public get hasChilds() {
    return this.containerList?.length > 0 || this.playlistList?.length > 0 || this.itemsCount > 0;
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

  public get currentObjectList(): ContainerItemDto {
    return this.contentDirectoryService.currentContainerList;
  }

  public browseTo(containerDto: ContainerDto) {
    // TODO: scroll to last parent doesn't work any more ... fix it.
    if (containerDto.parentID === "[PARENT]") {
      this.scrollID = this.contentDirectoryService.currentContainerList.currentContainer.id;
    }
    else {
      this.scrollID = '';
    }
    this.contentDirectoryService.browseChildrenByContiner(containerDto);
  }

  shufflePlaylist(container) {
    this.playlistService.addContainerToPlaylistAndPlay(container, true);
  }

  playPlaylist(container) {
    this.playlistService.addContainerToPlaylistAndPlay(container, false);
  }

  isLeaf(): boolean {
    let isLeaf = this.containerListWithoutMinimServerTags().length < 1;
    return isLeaf;
  }
}
