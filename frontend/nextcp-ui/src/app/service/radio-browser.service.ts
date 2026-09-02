import { Injectable, inject } from '@angular/core';
import { Subject } from 'rxjs';
import { HttpService } from './http.service';
import { DeviceService } from './device.service';
import { CdsUpdateService } from './cds-update.service';
import {
  AddRadioStationRequest,
  RadioBrowserFilterRequest,
  RadioBrowserFilterValueDto,
  RadioBrowserSearchRequest,
  RadioBrowserStationDto,
} from './dto.d';

/** Which list of filter values to fetch. */
export type RadioFilterKind = 'countries' | 'languages' | 'tags';

/**
 * Which half of an ICY stream title carries the artist. Most stations announce "Artist - Title",
 * some announce "Track - Station", and the media server guesses per station unless told.
 */
export type IcyOrder = 'auto' | 'artist-first' | 'title-first';

/**
 * Searches radio-browser.info through the media server and adds a station to one of its playlists.
 * The media server holds the radio-browser client, so the station is read there rather than here.
 */
@Injectable({
  providedIn: 'root',
})
export class RadioBrowserService {
  private httpService = inject(HttpService);
  private deviceService = inject(DeviceService);
  private cdsUpdateService = inject(CdsUpdateService);

  private baseUri = '/MediaServerPlaylistService';

  /**
   * The setting belongs to the station, not to a renderer - the media server keeps it in the
   * playlist the station is listed in.
   */
  public icyOrder(objectId: string): Subject<{ icyOrder: string }> {
    const udn = this.deviceService.selectedMediaServerDevice().udn;
    return this.httpService.get<{ icyOrder: string }>(
      this.baseUri,
      `/getWebStreamIcyOrder/${udn}/${encodeURIComponent(objectId)}`,
      'web radio',
    );
  }

  public setIcyOrder(objectId: string, order: IcyOrder): Subject<void> {
    const udn = this.deviceService.selectedMediaServerDevice().udn;
    return this.httpService.get<void>(
      this.baseUri,
      `/setWebStreamIcyOrder/${udn}/${encodeURIComponent(objectId)}/${order}`,
      'web radio',
    );
  }

  public searchStations(
    criteria: Partial<RadioBrowserSearchRequest>,
  ): Subject<RadioBrowserStationDto[]> {
    const request: RadioBrowserSearchRequest = {
      serverUdn: this.deviceService.selectedMediaServerDevice().udn,
      name: criteria.name ?? '',
      countryCode: criteria.countryCode ?? '',
      language: criteria.language ?? '',
      tag: criteria.tag ?? '',
      offset: criteria.offset ?? 0,
      limit: criteria.limit ?? 50,
    };
    return this.httpService.post<RadioBrowserStationDto[]>(
      this.baseUri,
      '/searchRadioStations',
      request,
      'radio search',
    );
  }

  /**
   * @param search only used for tags — there are over 12000 of them and a plain list is cut off at
   *               the top 1000, so a genre has to be typed rather than picked from everything
   */
  public filterValues(
    kind: RadioFilterKind,
    search?: string,
  ): Subject<RadioBrowserFilterValueDto[]> {
    const request: RadioBrowserFilterRequest = {
      serverUdn: this.deviceService.selectedMediaServerDevice().udn,
      kind: kind,
      search: search ?? '',
    };
    return this.httpService.post<RadioBrowserFilterValueDto[]>(
      this.baseUri,
      '/getRadioFilterValues',
      request,
      'radio filter',
    );
  }

  /**
   * @param title what the entry is named; empty keeps the name radio-browser has for the station
   */
  public addStationToPlaylist(
    playlistObjectId: string,
    stationUuid: string,
    title: string,
  ): Subject<void> {
    const request: AddRadioStationRequest = {
      serverUdn: this.deviceService.selectedMediaServerDevice().udn,
      playlistObjectId: playlistObjectId,
      stationUuid: stationUuid,
      title: title ?? '',
    };
    const result = this.httpService.post<void>(
      this.baseUri,
      '/addRadioStationToPlaylist',
      request,
      'add radio station',
    );
    // the media server writes the entry before it answers, so the browse that follows sees it
    result.subscribe({
      next: () => this.cdsUpdateService.containerContentChanged$.next(playlistObjectId),
      error: () => {},
    });
    return result;
  }
}
