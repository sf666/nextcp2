import { ContainerDto, QuickSearchResultDto, ContainerItemDto, QuickSearchRequestDto } from './../service/dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
/**
 * Utility class for generating DTOs.
 */
export class DtoGeneratorService {

  constructor() { }

  public generateEmptyContainerDto(): ContainerDto {
    return {
      albumartUri: '',
      artist: '',
      childCount: 0,
      createClass: '',
      creator: '',
      id: '',
      mediaServerUDN: '',
      objectClass: '',
      parentID: '',
      rating: 0,
      searchClass: '',
      searchable: false,
      title: ''
    };
  }

  public generateEmptyQuickSearchResultDto(): QuickSearchResultDto {
    return {
      albumItems: [],
      artistItems: [],
      musicItems: [],
      playlistItems: []
    }
  }

  public generateEmptyContainerItemDto(): ContainerItemDto {
    return {
      currentContainer: this.generateEmptyContainerDto(),
      containerDto: [],
      musicItemDto: [],
      albumDto: []
    }
  }

  public generateQuickSearchDto(_searchRequest: string, _mediaServerUDN: string, _sortCriteria): QuickSearchRequestDto {
    return {
      requestCount: 4,
      mediaServerUDN: _mediaServerUDN,
      searchRequest: _searchRequest,
      sortCriteria: _sortCriteria
    }
  }

}
