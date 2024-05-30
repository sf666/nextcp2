import { Component, Input, Output } from '@angular/core';
import { ContainerDto } from 'src/app/service/dto';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'container-tile',
  standalone: true,
  imports: [],
  templateUrl: './container-tile.component.html',
  styleUrl: './container-tile.component.scss',
})
export class ContainerTileComponent {
  @Input() container: ContainerDto[];
  @Input() smallIcons: boolean = false;
  @Input() showPlayOverlay: boolean = false;
  
  @Input() quickSearchString: string;

  @Output() browseClicked = new EventEmitter<ContainerDto>();

  get containerList(): ContainerDto[] {
    return this.container;
  }

  public browseTo(containerDto: ContainerDto): void {
    this.browseClicked.emit(containerDto);
  }

  getSmallCss() {
    if (this.smallIcons) {
      return ' small ';
    } else {
      return '';
    }
  }

  isAlbum(item: ContainerDto): boolean {
    return item.objectClass?.startsWith('object.container.album');
  }

  getOtherContainerHeadline(item: ContainerDto): string {
    if (item.objectClass.startsWith('object.container.person')) {
      return 'ARTIST';
    } else if (
      item.objectClass?.startsWith('object.container.playlistContainer')
    ) {
      return 'PLAYLIST';
    } else if (item.objectClass?.startsWith('object.container.album')) {
      return 'ALBUM';
    } else if (item.objectClass?.startsWith('object.container.genre')) {
      return 'GENRE';
    } else if (item.objectClass?.startsWith('object.container.channelGroup')) {
      return 'CHANNELS';
    } else if (item.objectClass?.startsWith('object.container.epgContainer')) {
      return 'EPG';
    } else if (item.objectClass?.startsWith('object.container.storageSystem')) {
      return 'DEVICE';
    } else if (item.objectClass?.startsWith('object.container.storageVolume')) {
      return 'DISC';
    } else if (item.objectClass?.startsWith('object.container.storageFolder')) {
      return '';
    } else if (
      item.objectClass?.startsWith('object.container.bookmarkFolder')
    ) {
      return 'BOOKMARKS';
    }
    return '';
  }
}
