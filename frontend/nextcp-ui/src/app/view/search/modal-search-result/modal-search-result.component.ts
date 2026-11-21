import { GlobalSearchService } from './../../../service/search/global-search.service';
/* eslint-disable @typescript-eslint/restrict-plus-operands */
/* eslint-disable no-restricted-syntax */
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ChangeDetectionStrategy, Component, ElementRef, inject } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { QualityBadgeComponent } from '../../../util/comp/quality-badge/quality-badge.component';
import {
  DisplayHeaderOptionsComponent,
  DisplayHeaderOptionsDialogData,
} from 'src/app/mediaserver/popup/display-header-options/display-header-options.component';
import { SongOptionsServiceService } from 'src/app/mediaserver/popup/song-options/song-options-service.service';
import { DeviceService } from 'src/app/service/device.service';
import { ContainerDto, ItemDto } from 'src/app/service/dto';
import { RATING_LIKED, RatingServiceService } from 'src/app/service/rating-service.service';
import { ServerFeature } from 'src/app/service/server-feature';

@Component({
  selector: 'modal-search-result',
  templateUrl: './modal-search-result.component.html',
  styleUrls: ['./modal-search-result.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  // non singleton injections for global search service
  providers: [
    { provide: ContentDirectoryService, useClass: ContentDirectoryService },
  ],
  imports: [NgTemplateOutlet, QualityBadgeComponent],
})

/**
 * This class supports the search popup dialog.
 */
export class ModalSearchResultComponent {
  private songOptionsServiceService = inject(SongOptionsServiceService);
  private gss = inject(GlobalSearchService);
  private readonly dialog = inject(MatDialog);
  private readonly deviceService = inject(DeviceService);
  private readonly ratingService = inject(RatingServiceService);

  get globalSearchService(): GlobalSearchService {
    return this.gss;
  }

  showSongPopup(event: MouseEvent, item: ItemDto): void {
    this.songOptionsServiceService
      .openOptionsDialog(event, item, undefined)
      .subscribe((result) => {
        // handle individual user action from dialog if needed
      });
  }

  protected canOpenContainerOptions(item: ContainerDto): boolean {
    return !!item.id && item.id !== '0' &&
      item.mediaServerUDN === this.deviceService.selectedMediaServerDevice().udn;
  }

  protected showContainerPopup(event: MouseEvent, item: ContainerDto): void {
    event.stopPropagation();
    if (!this.canOpenContainerOptions(item)) {
      return;
    }
    const isLiked = item.rating === RATING_LIKED;
    const dialogRef = this.dialog.open<
      DisplayHeaderOptionsComponent, DisplayHeaderOptionsDialogData, 'toggleLike'
    >(DisplayHeaderOptionsComponent, {
      hasBackdrop: true,
      panelClass: ['popup-glass'],
      data: {
        trigger: new ElementRef(event.currentTarget),
        currentContainer: item,
        listingContainerId: item.parentID,
        canLike: this.deviceService.hasFeature(ServerFeature.ALBUM_LIKES),
        isLiked,
        childFolderCount: 0,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'toggleLike' && this.canOpenContainerOptions(item)) {
        this.toggleLike(item, isLiked);
      }
    });
  }

  private toggleLike(item: ContainerDto, isLiked: boolean): void {
    const rating = isLiked ? undefined : RATING_LIKED;
    this.ratingService.setResourceRating(
      item.id, item.rating ?? undefined, rating, item.parentID, item.objectClass,
    ).subscribe({
      next: () => {
        const patch = (items: ContainerDto[]) =>
          items.map((hit) => hit === item ? { ...hit, rating: rating ?? 0 } : hit);
        this.gss.quickSearchResultList.update((result) => ({
          ...result,
          albumItems: patch(result.albumItems),
          artistItems: patch(result.artistItems),
          playlistItems: patch(result.playlistItems),
        }));
      },
      error: () => { /* HttpService displays the error. */ },
    });
  }
}
