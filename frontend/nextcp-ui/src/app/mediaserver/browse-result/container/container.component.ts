import { delay } from './../../../global';
import { AvtransportService } from './../../../service/avtransport.service';
import { PlaylistService } from './../../../service/playlist.service';
import { ContainerDto } from './../../../service/dto.d';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { Component, OnChanges, SimpleChanges, DoCheck, AfterViewInit, AfterContentChecked, AfterViewChecked } from '@angular/core';

@Component({
  selector: 'browseResultContainer',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.scss']
})
export class ContainerComponent implements AfterViewChecked {

  private scrollID: string = '';

  constructor(
    private contentDirectoryService: ContentDirectoryService,
    private avtransportService: AvtransportService,
    public playlistService: PlaylistService) { }

  ngAfterViewChecked(): void {
    if (this.scrollID) {
      let elmnt = document.getElementById(this.scrollID);
      if (elmnt) {
        elmnt.scrollIntoView({
          behavior: "auto",
          block: "center",
          inline: "center",
        });
        this.scrollID = '';
      }
    }
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

  playPlaylist(container) {
    this.playlistService.addContainerToPlaylistAndPlay(container);
  }

  isLeaf(): boolean {
    let isLeaf = this.containerListWithoutMinimServerTags().length < 1;
    return isLeaf;
  }

  showParentUpButton(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id !== '0';
  }

  gotoParant() {
    this.contentDirectoryService.browseChildren(this.contentDirectoryService.currentContainerList.currentContainer.parentID, "",
      this.contentDirectoryService.currentContainerList.currentContainer.mediaServerUDN);
  }
}
