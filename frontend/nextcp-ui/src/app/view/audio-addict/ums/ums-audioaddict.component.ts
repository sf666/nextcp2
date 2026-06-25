import {
  ChangeDetectionStrategy,
  Component,
  signal,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';
import { DisplayContainerComponent } from 'src/app/mediaserver/display-container/display-container.component';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { DeviceService } from 'src/app/service/device.service';
import {
  ContainerDto,
  ContainerItemDto,
  MediaServerDto,
  MusicItemDto,
} from 'src/app/service/dto';
import { LayoutService } from 'src/app/service/layout.service';
import { PersistenceService } from 'src/app/service/persistence/persistence.service';
import { NavBarComponent } from '../../nav-bar/nav-bar.component';

@Component({
  selector: 'ums-audioaddict',
  templateUrl: './ums-audioaddict.component.html',
  styleUrl: './ums-audioaddict.component.scss',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NavBarComponent, DisplayContainerComponent],
})
export class UmsAudioaddictComponent {
  layoutService = inject(LayoutService);
  private deviceService = inject(DeviceService);
  private persistenceService = inject(PersistenceService);
  contentDirectoryService = inject(ContentDirectoryService);

  private rootID = '';
  // Object ID to restore once after a page reload (captured before any browse overwrites it).
  private restoreOid = '';
  private restoreDone = false;
  hasAudioAddictService = signal<boolean>(true);

  constructor() {
    console.log('constructor call : MyAlbumComponent');
    this.restoreOid =
      this.persistenceService.getLastAudioAddictObjectId() ?? '';
    // Remember the last displayed container so a reload returns to it.
    this.contentDirectoryService.browseFinished$
      .pipe(takeUntilDestroyed())
      .subscribe((data) => {
        const id = data?.currentContainer?.id;
        if (id && id.length > 0) {
          this.persistenceService.setLastAudioAddictObjectId(id);
        }
      });
    toObservable(this.deviceService.selectedMediaServerDevice).subscribe(
      (data) => this.mediaServerChanged(data),
    );
  }

  mediaServerChanged(data: MediaServerDto): void {
    this.loadRadioNetwork();
  }

  loadRadioNetwork() {
    const oid = '$DBID$AUDIOADDICT$';
    const udn = this.deviceService.selectedMediaServerDevice().udn;
    if (!udn) {
      return;
    }

    // On the first load with a saved sub-container, render that container
    // directly (single round trip, no root "Radio Networks" flash) and resolve
    // the root metadata in parallel WITHOUT rendering it. The root metadata is
    // only needed for the back-button-at-root check and the service-availability
    // warning - neither is required for the initial render, so it must not block
    // or delay showing the last visited page (e.g. "Events").
    if (!this.restoreDone && this.restoreOid) {
      this.restoreDone = true;
      this.contentDirectoryService
        .browseChildren(this.restoreOid, '', udn)
        .subscribe();
      this.contentDirectoryService
        .browseChildrenMetadataOnly(oid, '', udn)
        .subscribe((data) => this.applyRootMetadata(data));
      return;
    }

    // Normal path: browse and display the root "Radio Networks" page.
    this.restoreDone = true;
    this.contentDirectoryService
      .browseChildren(oid, '', udn)
      .subscribe((data) => this.applyRootMetadata(data));
  }

  // Capture root container info (resolved root id and service availability)
  // from a browse result without assuming it is the rendered container.
  private applyRootMetadata(data: ContainerItemDto): void {
    console.log(
      'root id of UMS audioaddict network node is ' +
        data?.currentContainer?.id,
    );
    this.rootID = data.currentContainer.id;
    this.hasAudioAddictService.set((data.containerDto?.length ?? 0) > 0);
  }

  //
  // Event
  //

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList()
      .parentFolderTitle;
  }

  public homeButtonPressed(event: any) {
    this.loadRadioNetwork();
  }

  public backButtonPressed(event: any) {
    this.contentDirectoryService.browseToParent('');
  }

  //
  // bindings
  //
  backButtonVisible(): boolean {
    console.log(
      this.contentDirectoryService.currentContainerList().currentContainer.id,
    );
    return (
      this.contentDirectoryService.currentContainerList().currentContainer
        .id !== this.rootID
    );
  }

  getContentHandler(): ScrollLoadHandler {
    return {
      contentDirectoryService: this.contentDirectoryService,
      persistenceService: undefined,
    };
  }

  showTopHeader(): boolean {
    return true;
  }

  currentContainer(): ContainerDto {
    return this.contentDirectoryService.currentContainerList().currentContainer;
  }

  musicTracks(): MusicItemDto[] {
    return this.contentDirectoryService.musicTracks_();
  }

  otherItems_(): MusicItemDto[] {
    return this.contentDirectoryService.otherItems_();
  }

  albums(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList().albumDto;
  }

  playlists(): ContainerDto[] {
    return this.contentDirectoryService.playlistList_();
  }

  otherContainer(): ContainerDto[] {
    return this.contentDirectoryService.containerList_();
  }

  scrollToID(): string {
    return '';
  }
}
