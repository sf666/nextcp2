import { ContainerDto, ContainerItemDto, MusicItemDto } from './../../service/dto.d';
import { GlobalSearchService } from './../../service/search/global-search.service';
import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { DisplayContainerComponent } from 'src/app/mediaserver/display-container/display-container.component';
import { LayoutService } from 'src/app/service/layout.service';
import { DeviceService } from 'src/app/service/device.service';
import { CdsBrowsePathService } from 'src/app/util/cds-browse-path.service';
import { PersistenceService } from 'src/app/service/persistence/persistence.service';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-music-library',
  standalone: true,
  imports: [NavBarComponent, DisplayContainerComponent, RouterLink],
  providers: [ContentDirectoryService, CdsBrowsePathService, { provide: 'uniqueId', useValue: 'music-library_' }],
  templateUrl: './music-library.component.html',
  styleUrl: './music-library.component.scss'
})
export class MusicLibraryComponent  implements AfterViewInit{

  private lastOidIsRestoredFromCache: boolean;

  @ViewChild(DisplayContainerComponent) dispContainer: DisplayContainerComponent;
  @Input() objectId!: string;

  constructor(
    private route: ActivatedRoute,
    public contentDirectoryService: ContentDirectoryService,
    public layoutService: LayoutService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private persistenceService: PersistenceService,
    private deviceService: DeviceService,
    private globalSearchService: GlobalSearchService
  ) {
    console.log("constructor call : MusicLibraryComponent");
    globalSearchService.musicItemClicked$.subscribe(musicItem => this.musicItemClickedFromSearch(musicItem))
    globalSearchService.containerClicked$.subscribe(containerItem => this.containerItemClickedFromSearch(containerItem))
    
    globalSearchService.showAllItemClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllItems(searchReq));
    globalSearchService.showAllAlbumClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllAlbum(searchReq));
    globalSearchService.showAllArtistClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllArtists(searchReq));
    globalSearchService.showAllPlaylistClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllPlaylist(searchReq));

    route.params.subscribe(val => {
      console.log(val.objectId);
      this.initialBrowseToUid(val.objectId);
    });
  }

  //
  // Quick Search click on container or item
  //
  private musicItemClickedFromSearch(musicItem: MusicItemDto) {
    // Just play
    this.dispContainer.play(musicItem);
  }

  private containerItemClickedFromSearch(container: ContainerDto) {
    console.debug("selected container : " + container.objectClass);
    this.contentDirectoryService.browseChildrenByContainer(container);
  }


  ngAfterViewInit(): void {
    this.layoutService.setFramedView();
    if (this.objectId) {
      this.initialBrowseToUid(this.objectId);
    } else {
      this.initialBrowseToUid(this.persistenceService.getLastMediaServerPath());
    }
  }

  private initialBrowseToUid(objectId: string) {
    let udn: string;
    this.cdsBrowsePathService.restorePathToRoot();
    this.lastOidIsRestoredFromCache = true;
    udn = this.persistenceService.getCurrentMediaServerDevice();
    if (!(udn?.length > 0)) {
      udn = this.deviceService.selectedMediaServerDevice.udn;
      objectId = "0";
    }

    let prom = this.browseToOid(objectId, udn, true, "");
    if (prom) {
      prom.then(
        (val) => {if (!val) this.browseToRoot(udn)},
        (err) => console.error(err)
      );
    }
  }

  private browseToOid(oid: string, udn: string, stepIn: boolean, sortCriteria?: string): Promise<boolean> {
    if (this.dispContainer) {
      return this.dispContainer.browseToOid(oid, udn, stepIn, sortCriteria);
    }
  }

  public browseToRoot(udn: string, sortCriteria?: string): void {
    console.log("browsing to root folder ... ")
    this.cdsBrowsePathService.clearPath();
    this.browseToOid("0", udn, true, sortCriteria);
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

  //
  // Nav-Bar bindings
  //
  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList.parentFolderTitle;
  }

  rootButtonPressed(event: any) {
    const currentParent = this.contentDirectoryService?.currentContainerList?.currentContainer?.parentID;
    if (currentParent) {
      this.dispContainer.clearSearch();
      this.browseToOid("0", this.deviceService.selectedMediaServerDevice.udn, false, "");
    }
  }

  public backButtonPressed(event: any) {
    const currentParent = this.contentDirectoryService?.currentContainerList?.currentContainer?.parentID;
    if (currentParent) {
      this.dispContainer.clearSearch();
      this.browseToOid(currentParent, this.deviceService.selectedMediaServerDevice.udn, false, "");
    }
  }

  public backButtonDisabled(): boolean {
    if (this.contentDirectoryService?.currentContainerList?.currentContainer?.id) {
      return this.contentDirectoryService.currentContainerList.currentContainer.id === '0' ||
      this.contentDirectoryService.currentContainerList.currentContainer.id === '';
    }
    return false;
  }

}
