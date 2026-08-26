import { MusicItemIdDto, UpdateAlbumArtUriRequest } from './dto.d';
import { Injectable, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { SseService } from './sse/sse.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CdsUpdateService {
  private httpService = inject(HttpService);
  private deviceSerice = inject(DeviceService);
  private sseService = inject(SseService);

  baseUri = '/ContentDirectoryService';

  /**
   * Object id of a container whose content changed metadata the browse result
   * carries - a new cover for one of its entries, for instance. The view
   * showing that container has to browse again for the change to appear.
   */
  public containerContentChanged$ = new Subject<string>();

  constructor() {
    // The same thing, reported by the media server instead of caused by us: a container whose
    // content changed after it was browsed - a web playlist whose streams were still resolving when
    // the browse was answered. Fed into the same subject, so the view that shows it browses again.
    this.sseService.mediaServerContainerUpdateIds$
      .pipe(takeUntilDestroyed())
      .subscribe((update) => {
        if (
          update.mediaServerUdn !==
          this.deviceSerice.selectedMediaServerDevice().udn
        ) {
          return;
        }
        update.containerIds?.forEach((containerId) =>
          this.containerContentChanged$.next(containerId),
        );
      });
  }

  public setNewAlbumArtUri(
    ids: MusicItemIdDto,
    oldAlbumArtURI: string,
    albumArtURI: string,
    containerId?: string,
  ): Subject<void> {
    const uri = '/updateAlbumArtUri';

    const updateRequest: UpdateAlbumArtUriRequest = {
      previousAlbumArtUri: oldAlbumArtURI,
      newAlbumArtUri: albumArtURI,
      musicItemIdDto: ids,
      mediaServerDevice: this.deviceSerice.selectedMediaServerDevice().udn,
    };

    const result = this.httpService.post<void>(this.baseUri, uri, updateRequest);
    if (containerId) {
      // The media server stores the picture before it answers, so the browse
      // that follows already sees it.
      result.subscribe({
        next: () => this.containerContentChanged$.next(containerId),
        error: () => {},
      });
    }
    return result;
  }
}
