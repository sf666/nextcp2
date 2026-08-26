import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  model,
  signal,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { ContainerDto, RadioBrowserFilterValueDto, RadioBrowserStationDto } from './../../service/dto.d';
import { RadioBrowserService } from './../../service/radio-browser.service';
import { debounce } from './../../global';

/**
 * Picks a station from radio-browser.info and adds it to the playlist that is on screen.
 *
 * A browsable folder of every station would be useless — there are hundreds of thousands — so this
 * is a search with filters instead.
 */
@Component({
  selector: 'app-add-radio-station',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule],
  templateUrl: './add-radio-station.component.html',
  styleUrl: './add-radio-station.component.scss',
})
export class AddRadioStationComponent {
  private radioBrowserService = inject(RadioBrowserService);
  dialogRef = inject<MatDialogRef<AddRadioStationComponent>>(MatDialogRef);
  private data: { playlist: ContainerDto } = inject(MAT_DIALOG_DATA);

  readonly playlist = this.data.playlist;

  /** Station names at radio-browser are user maintained and run to hundreds of characters. */
  private static readonly MAX_TITLE_LENGTH = 70;
  private static readonly SEARCH_DELAY_MS = 500;
  private static readonly MIN_TAG_LENGTH = 2;

  // filter state
  name = model<string>('');
  countryCode = model<string>('');
  language = model<string>('');
  tagQuery = model<string>('');
  tag = signal<string>('');

  // lists
  countries = signal<RadioBrowserFilterValueDto[]>([]);
  languages = signal<RadioBrowserFilterValueDto[]>([]);
  tagSuggestions = signal<RadioBrowserFilterValueDto[]>([]);
  stations = signal<RadioBrowserStationDto[]>([]);

  searching = signal<boolean>(false);
  searched = signal<boolean>(false);

  /** Set while the chosen station waits for its title to be confirmed. */
  selected = signal<RadioBrowserStationDto | undefined>(undefined);
  entryTitle = model<string>('');

  private searchDebounced: () => void;
  private tagLookupDebounced: () => void;

  constructor() {
    this.searchDebounced = debounce(AddRadioStationComponent.SEARCH_DELAY_MS, () => this.search());
    this.tagLookupDebounced = debounce(AddRadioStationComponent.SEARCH_DELAY_MS, () => this.lookupTags());

    this.radioBrowserService.filterValues('countries').subscribe((values) => this.countries.set(values));
    this.radioBrowserService.filterValues('languages').subscribe((values) => this.languages.set(values));
  }

  hasFilter = computed(
    () =>
      this.name().trim().length > 0 ||
      this.countryCode().length > 0 ||
      this.language().length > 0 ||
      this.tag().length > 0,
  );

  //
  // filters
  //

  filterChanged(): void {
    this.searchDebounced();
  }

  tagQueryChanged(): void {
    // typing a genre both offers suggestions and searches with what is typed
    this.tag.set(this.tagQuery().trim());
    this.tagLookupDebounced();
    this.searchDebounced();
  }

  pickTag(value: string): void {
    this.tagQuery.set(value);
    this.tag.set(value);
    this.tagSuggestions.set([]);
    this.search();
  }

  clearFilters(): void {
    this.name.set('');
    this.countryCode.set('');
    this.language.set('');
    this.tagQuery.set('');
    this.tag.set('');
    this.tagSuggestions.set([]);
    this.stations.set([]);
    this.searched.set(false);
  }

  private lookupTags(): void {
    const query = this.tagQuery().trim();
    if (query.length < AddRadioStationComponent.MIN_TAG_LENGTH) {
      this.tagSuggestions.set([]);
      return;
    }
    this.radioBrowserService
      .filterValues('tags', query)
      .subscribe((values) => this.tagSuggestions.set(values.slice(0, 8)));
  }

  //
  // search
  //

  search(): void {
    if (!this.hasFilter()) {
      this.stations.set([]);
      this.searched.set(false);
      return;
    }
    this.searching.set(true);
    this.radioBrowserService
      .searchStations({
        name: this.name().trim(),
        countryCode: this.countryCode(),
        language: this.language(),
        tag: this.tag(),
      })
      .subscribe({
        next: (result) => {
          this.stations.set(result ?? []);
          this.searching.set(false);
          this.searched.set(true);
        },
        error: () => {
          this.searching.set(false);
          this.searched.set(true);
        },
      });
  }

  //
  // selection and confirmation
  //

  selectStation(station: RadioBrowserStationDto): void {
    this.selected.set(station);
    this.entryTitle.set(this.shorten(station.name));
  }

  cancelSelection(): void {
    this.selected.set(undefined);
    this.entryTitle.set('');
  }

  confirmSelection(): void {
    const station = this.selected();
    if (!station) {
      return;
    }
    this.radioBrowserService.addStationToPlaylist(
      this.playlist.id,
      station.uuid,
      this.entryTitle().trim(),
    );
    this.close();
  }

  /** What the entry is named by default — the full name stays available to type back in. */
  private shorten(value: string): string {
    const text = (value ?? '').trim();
    if (text.length <= AddRadioStationComponent.MAX_TITLE_LENGTH) {
      return text;
    }
    return text.substring(0, AddRadioStationComponent.MAX_TITLE_LENGTH).trimEnd() + '…';
  }

  //
  // bindings
  //

  stationQuality(station: RadioBrowserStationDto): string {
    const parts: string[] = [];
    if (station.codec) {
      parts.push(station.codec);
    }
    if (station.bitrate > 0) {
      parts.push(station.bitrate + ' kbps');
    }
    return parts.join(' · ');
  }

  close(): void {
    this.dialogRef.close();
  }
}
