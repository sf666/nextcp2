import { ChangeDetectionStrategy, Component } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { ScrollLoadHandler } from 'src/app/mediaserver/display-container/defs';
import { DisplayContainerComponent } from 'src/app/mediaserver/display-container/display-container.component';
import { ContentDirectoryService } from 'src/app/service/content-directory.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, MediaServerDto, MusicItemDto } from 'src/app/service/dto';
import { LayoutService } from 'src/app/service/layout.service';
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
  constructor(
    public layoutService: LayoutService,
    private deviceService: DeviceService,
    public contentDirectoryService: ContentDirectoryService) {
    console.log("constructor call : MyAlbumComponent");
    toObservable(this.deviceService.selectedMediaServerDevice).subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    this.loadRadioNetwork();
  }

  loadRadioNetwork() {
    const oid = "$DBID$AUDIOADDICT$";
    if (this.deviceService.selectedMediaServerDevice().udn) {
      this.contentDirectoryService.browseChildren(oid, "", this.deviceService.selectedMediaServerDevice().udn).subscribe();
    }
  }

  //
  // Event
  //

  getParentTitle(): string {
    return this.contentDirectoryService.currentContainerList().parentFolderTitle;
  }

  public backButtonPressed(event: any) {
    this.loadRadioNetwork();
  }

  //
  // bindings
  //
  getContentHandler(): ScrollLoadHandler {
    return { contentDirectoryService: this.contentDirectoryService, persistenceService: null }
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

