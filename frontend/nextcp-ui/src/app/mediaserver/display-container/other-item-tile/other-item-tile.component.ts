import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  output,
} from '@angular/core';
import { MusicItemDto } from 'src/app/service/dto';

@Component({
  selector: 'other-item-tile',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [],
  templateUrl: './other-item-tile.component.html',
  styleUrl: './other-item-tile.component.scss',
})
export class OtherItemTileComponent {
  otherItems = input<MusicItemDto[]>();
  quickSearchString = input<string>('');
  items = computed(() =>
    this.getOtherItemsFilter(this.otherItems(), this.quickSearchString())
  );

  playItemClicked = output<MusicItemDto>();

  private getOtherItemsFilter(
    data: MusicItemDto[],
    filterStr: string
  ): MusicItemDto[] {
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

  playResource(otherItem: MusicItemDto) {
    this.playItemClicked.emit(otherItem);
  }

  getOtherItemHeadline(item: MusicItemDto): string {
    // object.item.audioItem
    if (item.objectClass?.startsWith('object.item.audioItem')) {
      if (item?.audioFormat?.isStreaming) {
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
