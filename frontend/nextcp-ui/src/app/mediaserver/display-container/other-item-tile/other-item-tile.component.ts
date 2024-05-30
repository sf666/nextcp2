import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MusicItemDto } from 'src/app/service/dto';

@Component({
  selector: 'other-item-tile',
  standalone: true,
  imports: [],
  templateUrl: './other-item-tile.component.html',
  styleUrl: './other-item-tile.component.scss'
})
export class OtherItemTileComponent {

  @Input() otherItems: MusicItemDto[];
  @Input() quickSearchString: string;

  @Output() playItemClicked = new EventEmitter<MusicItemDto>();

  get items(): MusicItemDto[] {
    return this.getOtherItemsFilter(this.quickSearchString);
  }

  private getOtherItemsFilter(filter?: string): MusicItemDto[] {
    if (filter) {
      return this.otherItems.filter((item) =>
        this.doFilterText(item.title, filter)
      );
    } else {
      return this.otherItems;
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
