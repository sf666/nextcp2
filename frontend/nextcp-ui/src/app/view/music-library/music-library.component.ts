import { ScrollLoadHandler } from './../../mediaserver/display-container/defs.d';
import { DisplayContainerComponent } from './../../mediaserver/display-container/display-container.component';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { PersistenceService } from './../../service/persistence/persistence.service';
import { DeviceService } from 'src/app/service/device.service';
import { LayoutService } from './../../service/layout.service';
import { ContentDirectoryService } from './../../service/content-directory.service';
import { ContainerDto, MusicItemDto, MediaServerDto, ContainerItemDto } from './../../service/dto.d';
import { AfterViewInit, Component, ViewChild } from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService, PersistenceService, CdsBrowsePathService, { provide: 'uniqueId', useValue: 'music-library_' }]
})

export class MusicLibraryComponent implements AfterViewInit{

  private lastOidIsRestoredFromCache: boolean;

  @ViewChild(DisplayContainerComponent) dispContainer: DisplayContainerComponent;

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private persistenceService: PersistenceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MusicLibraryComponent");
    // select current mediaServer and subscribe to changes ...
    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
    deviceService.mediaServerInitiated$.subscribe(d => this.deviceService.setMediaServerByUdn(this.persistenceService.getCurrentMediaServerDevice()));
  }

  ngAfterViewInit(): void {
    this.browseToLastKnownUdn();
  }

  mediaServerChanged(data: MediaServerDto): void {
    if (this.persistenceService.isCurrentMediaServer(data.udn)) {
      this.browseToLastKnownUdn();
    } else {
      this.persistenceService.setNewMediaServerDevice(data.udn);
      this.browseToRoot(data.udn);
    }
  }

  private browseToLastKnownUdn() {
    let oid: string;
    let udn: string;
    oid = this.persistenceService.getLastMediaServerPath();
    this.cdsBrowsePathService.restorePathToRoot();
    this.lastOidIsRestoredFromCache = true;
    udn = this.persistenceService.getCurrentMediaServerDevice();

    this.browseToOid(oid, udn, true, "").then(
      (val) => {if (!val) this.browseToRoot(udn)},
      (err) => console.error(err)
    );
  }

  private browseToOid(oid: string, udn: string, stepIn: boolean, sortCriteria?: string): Promise<boolean> {
    return this.dispContainer.browseToOid(oid, udn, stepIn, sortCriteria);
  }

  contentReceived(udn: string, data: ContainerItemDto): void {
    // Check if 
    if (this.lastOidIsRestoredFromCache && !(data.containerDto.length > 0 || data.musicItemDto.length > 0)) {
      this.browseToRoot(udn, '');
      this.lastOidIsRestoredFromCache = false;
    }
  }

  public browseToRoot(udn: string, sortCriteria?: string): void {
    console.log("browsing to root folder ... ")
    this.cdsBrowsePathService.clearPath();
//    this.cdsBrowsePathService.stepIn("0");
    this.browseToOid("0", udn, true, sortCriteria);
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
      this.browseToOid(currentParent, this.deviceService.selectedMediaServerDevice.udn, false, "");
    }
  }

  public backButtonDisabled(): boolean {
    return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
  }

  //
  // Events
  //
  containerSelected(event: ContainerDto) {
    // no special activities yet ... 
  }

  itemDeleted(event: MusicItemDto) {    
    this.contentDirectoryService.deleteMusicTrack(event);
//    this.contentDirectoryService.refreshCurrentContainer();
  }

  //
  // bindings
  // =======================================================================

  showTopHeader(): boolean {
    return true;
  }

  getContentHandler(): ScrollLoadHandler {
    return { cdsBrowsePathService: this.cdsBrowsePathService, contentDirectoryService: this.contentDirectoryService, persistenceService: this.persistenceService }
  }

}
