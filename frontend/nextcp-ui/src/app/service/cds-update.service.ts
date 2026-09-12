import { MusicItemIdDto, UpdateAlbumArtUriRequest } from './dto.d';
import { Injectable, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { SseService } from './sse/sse.service';
import { auditTime, filter, groupBy, mergeMap, Subject } from 'rxjs';

/**
 * One entry whose rating was just written from this browser, so a view holding that entry can
 * update the value in place instead of reading the whole container again.
 */
export interface RatingChange {
  objectID: string;
  rating: number | undefined;
  /** The container listing the entry, used to recognise the media server's echo of this change. */
  containerId?: string;
}

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

  /**
   * A rating this browser has just written. Carries the new value, so the views patch the entry
   * they already hold - the rating is the only thing that changed, and both the rating filter and
   * the sort work on the arrays already loaded.
   */
  public itemRatingChanged$ = new Subject<RatingChange>();

  /**
   * Containers this browser has changed itself, with the time it did.
   *
   * UMS reports a rating back through ContainerUpdateIDs - StoreResourceRatings.setRating bumps the
   * update id of the entry and of every ancestor - and acting on that echo would replace the listing
   * that was just patched, which is the flicker this avoids. Media servers that push nothing send no
   * echo, so nothing is lost there; they are also the ones that cannot store a rating in the first
   * place.
   */
  private readonly selfInflicted = new Map<string, number>();

  /** How long an incoming change is still attributed to this browser. */
  private static readonly ECHO_WINDOW_MS = 5000;

  constructor() {
    // The same thing, reported by the media server instead of caused by us: a container whose
    // content changed after it was browsed - a web playlist whose streams were still resolving when
    // the browse was answered. Fed into the same subject, so the view that shows it browses again.
    this.sseService.mediaServerContainerUpdateIds$
      .pipe(
        filter(
          (update) =>
            update.mediaServerUdn ===
            this.deviceSerice.selectedMediaServerDevice().udn,
        ),
        mergeMap((update) => update.containerIds ?? []),
        // One change can bump the same container several times - the media server re-reads a
        // playlist file more than once after it was written. Measured seven identical browses for a
        // single delete. Collapse a burst per container into one.
        groupBy((containerId) => containerId),
        mergeMap((perContainer) => perContainer.pipe(auditTime(700))),
        // Decided here rather than before auditTime, so the window is measured against the moment
        // the refresh would actually happen.
        filter((containerId) => !this.isOwnChange(containerId)),
        takeUntilDestroyed(),
      )
      .subscribe((containerId) =>
        this.containerContentChanged$.next(containerId),
      );
  }

  /**
   * Announces a rating this browser has written: records the container as self-changed so the media
   * server's echo is ignored, then hands the new value to every view that shows the entry.
   */
  public announceRatingChange(change: RatingChange): void {
    if (change.containerId) {
      this.pruneExpired();
      this.selfInflicted.set(change.containerId, Date.now());
    }
    this.itemRatingChanged$.next(change);
  }

  private isOwnChange(containerId: string): boolean {
    const changedAt = this.selfInflicted.get(containerId);
    if (changedAt === undefined) {
      return false;
    }
    if (Date.now() - changedAt > CdsUpdateService.ECHO_WINDOW_MS) {
      this.selfInflicted.delete(containerId);
      return false;
    }
    return true;
  }

  /** An echo that never arrived would otherwise keep its entry forever. */
  private pruneExpired(): void {
    const deadline = Date.now() - CdsUpdateService.ECHO_WINDOW_MS;
    for (const [containerId, changedAt] of this.selfInflicted) {
      if (changedAt < deadline) {
        this.selfInflicted.delete(containerId);
      }
    }
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
