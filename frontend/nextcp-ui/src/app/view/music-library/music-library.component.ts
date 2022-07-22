import { DisplayContainerComponent } from './../../mediaserver/display-container/display-container.component';
import { Subject } from 'rxjs';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { PersistenceService } from './../../service/persistence/persistence.service';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto, MediaServerDto, ContainerItemDto } from './../../service/dto.d';
import { Component, ViewChild} from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService, CdsBrowsePathService, {provide: 'uniqueId', useValue: 'music-library_'}]
})

export class MusicLibraryComponent {

  private lastOidIsRestoredFromCache: boolean;

  @ViewChild(DisplayContainerComponent) dispContainer:DisplayContainerComponent;

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

  mediaServerChanged(data: MediaServerDto): void {
    if (!data.udn) {
      return;
    }

    let oid: string;
    if (this.persistenceService.isCurrentMediaServer(data.udn)) {
      oid = this.persistenceService.getLastMediaServerPath();
      this.cdsBrowsePathService.restorePathToRoot();
      this.lastOidIsRestoredFromCache = true;
    } else {
      this.lastOidIsRestoredFromCache = false;
      oid = '0';
      this.cdsBrowsePathService.clearPath();
      this.persistenceService.setNewMediaServerDevice(data.udn);
    }

    this.browseToOid(oid, "").subscribe(data => this.contentReceived(data));
  }

  private browseToOid(oid: string, sortCriteria?: string): Subject<ContainerItemDto> {
    this.persistenceService.setCurrentObjectID(oid);
    return this.contentDirectoryService.browseChildren(oid, sortCriteria, this.deviceService.selectedMediaServerDevice.udn);
  }

  contentReceived(data: ContainerItemDto): void {
    // Check if 
    if (this.lastOidIsRestoredFromCache && !(data.containerDto.length > 0 || data.musicItemDto.length > 0)) {
      this.browseToRoot('');
      this.lastOidIsRestoredFromCache = false;
    }
  }

  public browseToRoot(sortCriteria?: string): void {
    this.cdsBrowsePathService.clearPath();
    this.cdsBrowsePathService.stepIn("0");
    this.browseToOid("0", sortCriteria);
  }

  //
  // Nav-Bar bindngs
  //
  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }

  public backButtonPressed(event: any) {
    const currentParent = this.contentDirectoryService?.currentContainerList?.currentContainer?.parentID;
    if (currentParent) {
      this.dispContainer.clearSearch();
      this.browseToOid(currentParent, "");
      this.cdsBrowsePathService.stepOut();
    }
  }

  public backButtonDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }

  //
  // Event
  //
  containerSelected(event: ContainerDto) {
    // remember path here
    this.cdsBrowsePathService.stepIn(event.id);
    this.persistenceService.setCurrentObjectID(event.id);
    this.contentDirectoryService.browseChildrenByContiner(event);
  }

  //
  // bindings
  // =======================================================================

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
    return this.cdsBrowsePathService.scrollToID;  // this.scrollToId_;
  }
}
