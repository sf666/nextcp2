import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  output,
  untracked,
} from '@angular/core';
import { ContainerDto, ItemDto } from 'src/app/service/dto';
import { AlbumArtService } from 'src/app/util/album-art.service';
import { isBroadcastItem } from 'src/app/util/broadcast-item';
import { matchesTextFilter } from 'src/app/util/browse-filter';
import { SongOptionsServiceService } from '../../popup/song-options/song-options-service.service';
import { BROWSE_WINDOW_THRESHOLD, BrowseWindowDirective } from '../browse-window.directive';

@Component({
  selector: 'other-item-tile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [BrowseWindowDirective],
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
  items = computed(() => {
    const query = this.quickSearchString();
    return this.otherItems().filter((item) => matchesTextFilter(item.title, query));
  });
  protected readonly windowed = computed(() => this.otherItems().length >= BROWSE_WINDOW_THRESHOLD);

  playItemClicked = output<ItemDto>();

  constructor() {
    let previous = this.quickSearchString();
    effect(() => {
      const query = this.quickSearchString();
      if (query !== previous && untracked(this.windowed)) {
        const parent = document.getElementById('mainContent');
        if (parent) {
          parent.scrollTop = 0;
        }
      }
      previous = query;
    });
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
