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
import { ActivatedRoute } from '@angular/router';
import { MusicLibraryService } from 'src/app/service/music-library/music-library.service';

@Component({
  selector: 'app-music-library',
  standalone: true,
  imports: [NavBarComponent, DisplayContainerComponent],
  providers: [ContentDirectoryService, CdsBrowsePathService, { provide: 'uniqueId', useValue: 'music-library_' }],
  templateUrl: './music-library.component.html',
  styleUrl: './music-library.component.scss'
})
export class MusicLibraryComponent implements AfterViewInit {

  @ViewChild(DisplayContainerComponent) dispContainer: DisplayContainerComponent;
  @Input() objectId!: string;

  constructor(
    private route: ActivatedRoute,
    public contentDirectoryService: ContentDirectoryService,
    public layoutService: LayoutService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private persistenceService: PersistenceService,
    private deviceService: DeviceService,
    private musicLibraryService: MusicLibraryService,
    private globalSearchService: GlobalSearchService
  ) {
    console.log("constructor call : MusicLibraryComponent");
    globalSearchService.musicItemClicked$.subscribe(musicItem => this.musicItemClickedFromSearch(musicItem))
    globalSearchService.containerClicked$.subscribe(containerItem => this.containerItemClickedFromSearch(containerItem))

    globalSearchService.showAllItemClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllItems(searchReq));
    globalSearchService.showAllAlbumClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllAlbum(searchReq));
    globalSearchService.showAllArtistClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllArtists(searchReq));
    globalSearchService.showAllPlaylistClicked$.subscribe(searchReq => this.contentDirectoryService.searchAllPlaylist(searchReq));

    this.getContentHandler().contentDirectoryService.browseFinished$.subscribe(data => this.browseFinished(data));

    // for first call of application wait till we know the devices ...
    deviceService.mediaServerInitiated$.subscribe(() => {
      let udn = deviceService.selectedMediaServerDevice().udn;
      route.params.subscribe(val => {
        console.log("route parameter given at constructor time : " + val.objectId);
        if (val.objectId) {
          this.browseToUidAfterViewInit(udn, this.objectId);
        } else {
          if (udn?.length > 0) {
            this.initViewData(udn);
          } else {
            console.log("no media server device selected.");
          }
        }
      });
    });
  }

  ngAfterViewInit(): void {
    this.layoutService.setFramedView();
    let udn = this.deviceService.selectedMediaServerDevice().udn;
    this.initViewData(udn);

  }

  private browseFinished(data: ContainerItemDto): void {
    this.musicLibraryService.updateCurrentContainer(data);
  }

  //
  // Quick Search click on container or item
  //
  private musicItemClickedFromSearch(musicItem: MusicItemDto) {
    // Just play
    this.dispContainer.playItem(musicItem);
  }

  private containerItemClickedFromSearch(container: ContainerDto) {
    console.debug("selected container : " + container.objectClass);
    this.contentDirectoryService.browseChildrenByContainer(container);
  }

  private initViewData(udn: string): void {
    console.log("Music Library : initViewData ...");
    this.layoutService.setFramedView();
    if (this.objectId) {
      console.log("browse to injected OID : " + this.objectId);
      this.browseToUidAfterViewInit(udn, this.objectId);
    } else {
      if (this.persistenceService.getLastObjectId() !== undefined && this.persistenceService.getLastObjectId() !== '') {
        console.log("browse to last persistent object ID : " + this.persistenceService.getLastObjectId());
        this.browseToUidAfterViewInit(udn, this.persistenceService.getLastObjectId());
      } else {
        console.log("browse to object ID : 0");
        this.browseToUidAfterViewInit(udn, "0");
      }
    }
  }

  private browseToUidAfterViewInit(udn: string, objectId: string) {
    this.cdsBrowsePathService.restorePathToRoot();
    if (!(udn?.length > 0)) {
      console.log("last media server device not found ... ");
      udn = this.deviceService.selectedMediaServerDevice().udn;
      console.log("Music Library - initial : selected media server : " + udn);
      objectId = "0";
    } else {
      console.log();
      let prom = this.browseToOid(objectId, udn, true, "");
      if (prom) {
        prom.then(
          (val) => { if (!val) this.browseToRoot(udn) },
          (err) => console.error(err)
        );
      }
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
  }

  //
  // bindings
  // =======================================================================

  showTopHeader(): boolean {
    return true;
  }

  getContentHandler(): ScrollLoadHandler {
    return { contentDirectoryService: this.contentDirectoryService, persistenceService: this.persistenceService }
  }

  //
  // Nav-Bar bindings
  //
  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList().parentFolderTitle;
  }

  rootButtonPressed(event: any) {
    const currentParent = this.contentDirectoryService?.currentContainerList().currentContainer?.parentID;
    if (currentParent) {
      this.browseToOid("0", this.deviceService.selectedMediaServerDevice().udn, false, "");
    }
  }

  public backButtonPressed(event: any) {
    const currentParent = this.contentDirectoryService?.currentContainerList().currentContainer?.parentID;
    if (currentParent) {
      this.browseToOid(currentParent, this.deviceService.selectedMediaServerDevice().udn, false, "");
    }
  }

  public backButtonDisabled(): boolean {
    if (this.contentDirectoryService?.currentContainerList().currentContainer?.id) {
      return this.contentDirectoryService.currentContainerList().currentContainer.id === '0' ||
        this.contentDirectoryService.currentContainerList().currentContainer.id === '';
    }
    return false;
  }
}
