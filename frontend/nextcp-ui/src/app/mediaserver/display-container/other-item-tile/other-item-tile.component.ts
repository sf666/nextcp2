import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  output,
} from '@angular/core';
import { ContainerDto, ItemDto } from 'src/app/service/dto';
import { AlbumArtService } from 'src/app/util/album-art.service';
import { isBroadcastItem } from 'src/app/util/broadcast-item';
import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';

@Component({
  selector: 'other-item-tile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [],
  templateUrl: './other-item-tile.component.html',
  styleUrl: './other-item-tile.component.scss',
})
export class OtherItemTileComponent {
  readonly albumArt = inject(AlbumArtService);
  private readonly songOptions = inject(SongOptionsServiceService);

  otherItems = input<ItemDto[]>([]);
  currentContainer = input<ContainerDto>();
  protected readonly isPlaylist = computed(
    () => this.currentContainer()?.objectClass?.startsWith('object.container.playlistContainer'),
  );
  quickSearchString = input<string>('');
  /** Off where the section heading already says what these are, e.g. the Videos section. */
  showKind = input<boolean>(true);
  items = computed(() =>
    this.getOtherItemsFilter(this.otherItems(), this.quickSearchString())
  );

  playItemClicked = output<ItemDto>();

  private getOtherItemsFilter(
    data: ItemDto[],
    filterStr: string
  ): ItemDto[] {
    return data.filter((item) => this.doFilterText(item.title, this.quickSearchString()));
  }

  private doFilterText(title: string, filter: string): boolean {
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

  playResource(otherItem: ItemDto) {
    this.playItemClicked.emit(otherItem);
  }

  protected showSongPopup(event: MouseEvent, item: ItemDto): void {
    event.stopPropagation();
    this.songOptions.openOptionsDialog(event, item, this.currentContainer());
  }

  getOtherItemHeadline(item: ItemDto): string {
    // object.item.audioItem
    if (item.objectClass?.startsWith('object.item.audioItem')) {
      if (isBroadcastItem(item)) {
        return 'RADIO';
      }
      return 'IMAGE';
    } else if (item.objectClass?.startsWith('object.item.imageItem')) {
      return 'IMAGE';
    } else if (item.objectClass?.startsWith('object.item.videoItem')) {
      return 'VIDEO';
    } else if (item.objectClass?.startsWith('object.item.playlistItem')) {
      return 'PLAYLIST';
    } else if (item.objectClass?.startsWith('object.item.textItem')) {
      return 'TEXT';
    } else if (item.objectClass?.startsWith('object.item.bookmarkItem')) {
      return 'BOOKMARK';
    }
    return '';
  }
}
