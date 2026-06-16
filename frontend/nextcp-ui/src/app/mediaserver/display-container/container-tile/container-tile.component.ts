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
  // Sort/group criteria: 'NONE' | 'TITLE' | 'ARTIST' | 'GENRE'
  sortCriteria = input<string>('NONE');
  containerList = computed(() => this.filteredContainer(this.container()));

  // Containers grouped into sections according to sortCriteria.
  // A section with an empty key is rendered without a header.
  groupedContainers = computed(() =>
    this.groupAndSort(this.containerList(), this.sortCriteria())
  );

  browseClicked = output<ContainerDto>();

  private groupAndSort(
    items: ContainerDto[],
    criteria: string
  ): { key: string; items: ContainerDto[] }[] {
    // No sorting: keep original order, single section without header.
    if (!criteria || criteria === 'NONE') {
      return [{ key: '', items }];
    }

    // "Album Title" sorts alphabetically without section headers.
    if (criteria === 'TITLE') {
      const sorted = [...items].sort((a, b) =>
        this.compareText(a.title, b.title)
      );
      return [{ key: '', items: sorted }];
    }

    // "Album Artist" / "Genre" group into labelled sections.
    const groups = new Map<string, ContainerDto[]>();
    for (const item of items) {
      const key = this.groupKey(item, criteria);
      const bucket = groups.get(key);
      if (bucket) {
        bucket.push(item);
      } else {
        groups.set(key, [item]);
      }
    }

    const result = Array.from(groups.entries()).map(([key, groupItems]) => ({
      key,
      items: groupItems.sort((a, b) => this.compareText(a.title, b.title)),
    }));
    result.sort((a, b) => this.compareText(a.key, b.key));
    return result;
  }

  private groupKey(item: ContainerDto, criteria: string): string {
    if (criteria === 'ARTIST') {
      return item.artist?.trim() || 'Unknown';
    }
    if (criteria === 'GENRE') {
      // Use the first genre token only (e.g. "R&B / Soul" -> "R&B").
      const genre = item.genre?.split('/')[0]?.trim();
      return genre || 'Unknown';
    }
    return '';
  }

  private compareText(a?: string, b?: string): number {
    return (a ?? '').localeCompare(b ?? '', undefined, { sensitivity: 'base' });
  }

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
    if (this.smallIcons()) {
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
