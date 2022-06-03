import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { PersistenceService } from './../../service/persistence/persistence.service';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto, MediaServerDto, ContainerItemDto } from './../../service/dto.d';
import { Component } from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService]
})

export class MusicLibraryComponent {


  private lastOidIsResoredFromCache: boolean;

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private persistenceService: PersistenceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MusicLibraryComponent");
    // select current mediaServer and subscribe to changes ...
    this.mediaServerChanged(deviceService.selectedMediaServerDevice);
    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));     
  }

  //
  // Browse responses
  //
  mediaServerChanged(data: MediaServerDto): void {
    // Update to root folder of media server
    let oid: string;
    if (this.persistenceService.isCurrentMediaServer(data.udn)) {
      oid = this.persistenceService.getLastMediaServerPath();
      this.cdsBrowsePathService.restorePathToRoot();
      this.lastOidIsResoredFromCache = true;
    } else {
      this.lastOidIsResoredFromCache = false;
      oid = '0';
      this.cdsBrowsePathService.clearPath();
      this.persistenceService.setNewMediaServerDevice(data.udn);
    }
    this.contentDirectoryService.browseChildren(oid, "", data.udn).subscribe(data => this.contentReceived(data));
    // objectID: string, sortCriteria: string, mediaServerUdn?: string, isStepOut?: boolean): void {
  }

  contentReceived(data: ContainerItemDto): void {
    // BrowseTo Root
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
