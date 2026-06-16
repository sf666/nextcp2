import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';
import { DisplayContainerComponent } from 'src/app/mediaserver/display-container/display-container.component';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, MediaServerDto, MusicItemDto } from 'src/app/service/dto';
import { LayoutService } from 'src/app/service/layout.service';
import { PersistenceService } from 'src/app/service/persistence/persistence.service';
import { NavBarComponent } from '../../nav-bar/nav-bar.component';

@Component({
  selector: 'ums-audioaddict',
  templateUrl: './ums-audioaddict.component.html',
  styleUrl: './ums-audioaddict.component.scss',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NavBarComponent, DisplayContainerComponent]
})
export class UmsAudioaddictComponent {
  private rootID = '';
  // Object ID to restore once after a page reload (captured before any browse overwrites it).
  private restoreOid = '';
  private restoreDone = false;
  hasAudioAddictService = signal<boolean>(true);

  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    private persistenceService: PersistenceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");
    this.restoreOid = this.persistenceService.getLastAudioAddictObjectId() ?? '';
    // Remember the last displayed container so a reload returns to it.
    this.contentDirectoryService.browseFinished$
      .pipe(takeUntilDestroyed())
      .subscribe((data) => {
        const id = data?.currentContainer?.id;
        if (id && id.length > 0) {
          this.persistenceService.setLastAudioAddictObjectId(id);
        }
      });
    toObservable(this.deviceService.selectedMediaServerDevice).subscribe(data => this.mediaServerChanged(data));
  }


  mediaServerChanged(data: MediaServerDto): void {
    this.loadRadioNetwork();
  }

  loadRadioNetwork() {
    const oid = "$DBID$AUDIOADDICT$";
    const udn = this.deviceService.selectedMediaServerDevice().udn;
    if (udn) {
      this.contentDirectoryService.browseChildren(oid, "", udn).subscribe((data) => {
        console.log("root id of UMS audioaddict network node is " + data?.currentContainer?.id);
        this.rootID = data.currentContainer.id;
        if (data.containerDto.length > 0) {
          this.hasAudioAddictService.set(true);
        } else {
          this.hasAudioAddictService.set(false);
        }
        // On the first load only, restore the last visited sub-container.
        if (!this.restoreDone) {
          this.restoreDone = true;
          if (this.restoreOid && this.restoreOid !== this.rootID) {
            this.contentDirectoryService.browseChildren(this.restoreOid, "", udn).subscribe();
          }
        }
      });
    }
  }

  //
  // Event
  //

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList().parentFolderTitle;
  }

  public homeButtonPressed(event: any) {
    this.loadRadioNetwork();
  }

  public backButtonPressed(event: any) {
    this.contentDirectoryService.browseToParent('');;
  }

  //
  // bindings
  //
  backButtonVisible(): boolean {
    console.log(this.contentDirectoryService.currentContainerList().currentContainer.id);
    return this.contentDirectoryService.currentContainerList().currentContainer.id !== this.rootID;
  }

  getContentHandler(): ScrollLoadHandler {
    return { contentDirectoryService: this.contentDirectoryService, persistenceService: undefined }
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
    return "";
  }
}

