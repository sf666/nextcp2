import { ChangeDetectionStrategy, Component, Input, Output, computed, input, output, signal } from '@angular/core';
import { ContainerDto } from 'src/app/service/dto';

@Component({
  selector: 'container-tile',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [],
  templateUrl: './container-tile.component.html',
  styleUrl: './container-tile.component.scss',
})
export class ContainerTileComponent {
  container = input.required<ContainerDto[]>();
  smallIcons = input<boolean>(false);
  showPlayOverlay = input<boolean>(false);
  quickSearchString = input<string>("");
  selectedGenres = input<Array<string>>([]);
  containerList = computed(() => this.filteredContainer(this.container()));

  browseClicked = output<ContainerDto>();

  private filteredContainer(container : ContainerDto[]): ContainerDto[] {
    let tracks: Array<ContainerDto>;
    if (this.quickSearchString()) {
      tracks = container.filter((item) =>
        this.doFilterText(item.title, this.quickSearchString())
      );
    } else {
      tracks = container;
    }
    if (this?.selectedGenres()?.length > 0) {
      tracks = tracks.filter((item) => this.doFilterGenreByContainer(item));
    }
    return tracks;
  }

  /**
   * Filter an album by genre
   * @param container album container
   * @returns
   */
  private doFilterGenreByContainer(container: ContainerDto): boolean {
    let add = false;
    this.selectedGenres()?.forEach((genre) => {
      if (this.doFilterText(container.genre, genre)) {
        add = true;
      }
    });
    return add;
  }

  public containerFilter(filter?: string): ContainerDto[] {
    if (filter) {
      return this.container().filter((item) =>
        this.doFilterText(item.title, filter)
      );
    } else {
      return this.container();
    }
  }

  private doFilterText(title: string, filter?: string): boolean {
    if (!filter) {
      return true;
    }
    if (!title && 'NONE' == filter) {
      return true;
    } else if (!title) {
      return false;
    }
    return title.toLowerCase().includes(filter.toLowerCase());
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
