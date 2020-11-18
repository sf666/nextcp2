import { TimeDisplayService } from './../../../util/time-display.service';
import { CdsBrowsePathService } from './../../../util/cds-browse-path.service';
import { ScrollViewService } from './../../../util/scroll-view.service';
import { BackgroundImageService } from './../../../util/background-image.service';
import { PlaylistService } from './../../../service/playlist.service';
import { ContainerDto, ContainerItemDto } from './../../../service/dto.d';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component, AfterViewChecked, AfterViewInit } from '@angular/core';

@Component({
  selector: 'browseResultContainer',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.scss']
})
export class ContainerComponent implements AfterViewChecked {

  constructor(
    private contentDirectoryService: ContentDirectoryService,
    private backgroundImageService: BackgroundImageService,
    private scrollViewService: ScrollViewService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private timeDisplayService: TimeDisplayService,
    public playlistService: PlaylistService) { }

  ngAfterViewChecked(): void {
    this.backgroundImageService.setBackgroundImageMainScreen(this.currentContainer.albumartUri);
  }

  public get containerList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.containerDto.filter(item => item.objectClass !== "object.container.playlistContainer");
  }

  public get playlistList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.containerDto.filter(item => item.objectClass === "object.container.playlistContainer");
  }

  public get minimTagsList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.minimServerSupportTags;
  }

  public get itemsCount(): number {
    if (this.contentDirectoryService.currentContainerList?.musicItemDto?.length) {
      return this.contentDirectoryService.currentContainerList.musicItemDto.length;
    } else {
      return 0;
    }
  }

  get containerType(): string {
    if (this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.playlistContainer") {
      return "Playlist";
    } else if (this.contentDirectoryService.currentContainerList.currentContainer.objectClass === "object.container.album.musicAlbum") {
      return "Album";
    } else {
      return "Folder";
    }
  }

  get totalPlaytime(): string {
    let completeTime: number;
    completeTime = 0;
    if (this.contentDirectoryService.currentContainerList?.musicItemDto) {
      this.contentDirectoryService.currentContainerList.musicItemDto.forEach(
        el => completeTime = completeTime + el.audioFormat.durationInSeconds
      );
    }
    if (completeTime) {
      return this.timeDisplayService.convertLongToDateString(completeTime);
    }
    return "";
  }

  public get hasChilds() {
    return this.containerList?.length > 0 || this.playlistList?.length > 0 || this.itemsCount > 0;
  }

  public get albumList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.albumDto;
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
