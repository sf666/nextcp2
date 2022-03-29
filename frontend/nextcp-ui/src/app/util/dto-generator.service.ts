import { ContainerDto, SearchResultDto, ContainerItemDto, SearchRequestDto, MusicItemDto, AudioFormat, MusicBrainzId, TrackTimeDto, TrackInfoDto, SystemInformationDto, MusicItemIdDto } from './../service/dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
/**
 * Utility class for generating DTOs.
 */
export class DtoGeneratorService {

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
      title: '',
      genre: '',
      media_date: ''
    };
  }

  public generateSystemInformationDto(): SystemInformationDto {
    return {
      buildNumber: 'unknown'
    }
  }

  public generateEmptySearchResultDto(): SearchResultDto {
    return {
      parentID: '0',
      albumItems: [],
      artistItems: [],
      musicItems: [],
      playlistItems: []
    }
  }

  public generateEmptyContainerItemDto(): ContainerItemDto {
    return {
      parentFolderTitle: '',
      currentContainer: this.generateEmptyContainerDto(),
      containerDto: [],
      minimServerSupportTags: [],
      musicItemDto: [],
      albumDto: []
    }
  }

  public generateQuickSearchDto(_searchRequest: string, _mediaServerUDN: string, _sortCriteria: string, parentObjectID: string, start?: number, count?: number): SearchRequestDto {
    return {
      startElement: start ? start : 0,
      requestCount: count ? count : 4,
      mediaServerUDN: _mediaServerUDN,
      searchRequest: _searchRequest,
      sortCriteria: _sortCriteria,
      parentObjectID: parentObjectID
    }
  }

  emptyMusicItem(): MusicItemDto {
    return {
      album: '',
      albumArtUrl: '',
      artistName: '',
      audioFormat: this.emptyAudioFormat(),
      creator: '',
      currentTrackMetadata: '',
      date: '',
      mediaServerUDN: '',
      numberOfThisDisc: '',
      objectClass: '',
      objectID: '',
      originalTrackNumber: '',
      parentId: '',
      rating: 0,
      refId: '',
      streamingURL: '',
      title: '',
      genre: '',
      musicBrainzId: this.emptyMusicBrainzId(),
      songId: this.emptySongId()
    }
  }

  emptySongId(): MusicItemIdDto {
    return {
      umsAudiotrackId: -1,
      acoustID: '',
      musicBrainzIdTrackId: ''
    }
  }

  emptyTrackTime(): TrackTimeDto {
    return {
      duration: 0,
      streaming: false,
      durationDisp: '00:00',
      mediaRendererUdn: '',
      percent: 0,
      seconds: 0,
      secondsDisp: '00:00',
      trackCount: 0
    }
  }

  emptyTrackInfo(): TrackInfoDto {
    return {
      mediaRendererUdn: '',
      codecName: '',
      detailsCount: 0,
      metadata: '',
      metatext: '',
      metatextCount: 0,
      trackCount: 0,
      uri: '',
      duration: '',
      currentTrack: this.emptyMusicItem(),
    }
  }

  emptyAudioFormat(): AudioFormat {
    return {
      bitrate: 0,
      bitsPerSample: 0,
      filetype: '',
      nrAudioChannels: 2,
      sampleFrequency: 0,
      durationDisp: '',
      durationInSeconds: 0
    }
  }

  emptyMusicBrainzId(): MusicBrainzId {
    return {
      AlbumArtistId: '',
      AlbumId: '',
      ArtistId: '',
      ReleaseTrackId: '',
      WorkId: ''
    }
  }
}
