import { ConfigurationService } from './../configuration.service';
import { DeviceService } from 'src/app/service/device.service';
import { DtoGeneratorService } from './../../util/dto-generator.service';
import { ContentDirectoryService } from './../content-directory.service';
import { SearchResultDto } from './../dto.d';
import { Injectable } from '@angular/core';
import { debounce } from 'src/app/global';


@Injectable({
  providedIn: 'root'
})
export class GlobalSearchService {

  // QuickSearch Support (Global search) 
  public quickSearchResultList: SearchResultDto;
  public quickSearchQueryString: string;
  public quickSearchPanelVisible: boolean;


  private currentSearchText: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private doSearchFunc: any;

  private doSearchThrotteled = (): void => {
    this.searchContentDirectoryService.quickSearch(this.currentSearchText, "", this.deviceService.selectedMediaServerDevice.udn).subscribe(data => this.searchResultReceived(data));
  };


  constructor(
    public searchContentDirectoryService: ContentDirectoryService,
    private deviceService: DeviceService,
    private dtoGeneratorService: DtoGeneratorService,
    private configurationService: ConfigurationService
  ) {

    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;

    this.doSearchFunc = debounce(this.getSearchDelay(), this.doSearchThrotteled);
  }


  getSearchDelay(): number {
    const delay = this.configurationService.serverConfig?.applicationConfig?.globalSearchDelay != null ? this.configurationService.serverConfig.applicationConfig?.globalSearchDelay : 600;
    return Math.max(300, delay);
  }

  public showQuickSearchPanel(): void {
    this.quickSearchPanelVisible = true;
  }

  public hideQuickSearchPanel(): void {
    this.quickSearchPanelVisible = false;
  }


  private searchResultReceived(data: SearchResultDto): void {
    this.quickSearchResultList = data;
  }

  private doSearch(): void {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    this.doSearchFunc();
  }


  //
  // Search
  // =========================================================================

  get quickSearchString(): string {
    return this.quickSearchQueryString;
  }

  set quickSearchString(value: string) {
    this.quickSearchQueryString = value;
    if (value == '') {
      this.quickSearchPanelVisible = false;
    } else {
      if (value && value.length > 2) {
        this.quickSearchPanelVisible = true;
        this.currentSearchText = value;
        this.doSearch();
      }
    }
  }

  clearSearch(): void {
    this.quickSearchString = "";
    this.quickSearchResultList = this.dtoGeneratorService.generateEmptySearchResultDto();
    this.quickSearchPanelVisible = false;
  }

}

