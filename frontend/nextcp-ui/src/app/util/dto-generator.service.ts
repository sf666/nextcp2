import { ContainerDto, SearchResultDto, ContainerItemDto, SearchRequestDto, MusicItemDto, AudioFormat, MusicBrainzId, TrackTimeDto, TrackInfoDto, SystemInformationDto, MusicItemIdDto, InputSourceDto, TransportServiceStateDto, DeviceDriverState, UpnpAvTransportState, MediaRendererDto, AudioAddictConfig } from './../service/dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
/**
 * Utility class for generating DTOs.
 */
export class DtoGeneratorService {
  public emptyAudioAddictDto(): AudioAddictConfig {
    return {
      pass:'',
      preferEuropeanServer: true,
      token:'',
      user:''
    }
  }
  
  public emptyUpnpAvTransportState(): UpnpAvTransportState {
    return {
      AbsoluteCounterPosition: 0,
      AbsoluteTimePosition: '',
      AVTransportURI: '',
      AVTransportURIMetaData: this.emptyMusicItemDto(),
      CurrentMediaDuration: '',
      CurrentPlayMode: '',
      CurrentRecordQualityMode: '',
      CurrentTrack: 0,
      CurrentTrackDuration: '',
      CurrentTrackMetaData: this.emptyMusicItemDto(),
      CurrentTrackURI: '',
      CurrentTransportActions: '',
      mediaRenderer: this.emptyMediaRendererDto(),
      NextAVTransportURI: '',
      NextAVTransportURIMetaData: '',
      NumberOfTracks: 0,
      PlaybackStorageMedium: '',
      PossiblePlaybackStorageMedia: '',
      PossibleRecordQualityModes: '',
      PossibleRecordStorageMedia: '',
      RecordMediumWriteStatus: '',
      RecordStorageMedium: '',
      RelativeCounterPosition: 0,
      RelativeTimePosition: '',
      TransportPlaySpeed: '',
      TransportState: '',
      TransportStatus: '',
    }
  }

  public emptyMediaRendererDto(): MediaRendererDto {
    return {
      allSources: [],
      currentSource: this.emptyInputSourceDto(),
      friendlyName: '',
      services: [],
      udn: '',
    }
  }

  public emptyMusicItemDto(): MusicItemDto {
    return {
      album: '',
      albumArtUrl: '',
      artistName: '',
      audioFormat: this.emptyAudioFormat(),
      composer: '',
      conductor: '',
      creator: '',
      currentTrackMetadata: '',
      date: '',
      genre: '',
      mediaServerUDN: '',
      musicBrainzId: this.emptyMusicBrainzId(),
      numberOfThisDisc: '',
      objectClass: '',
      objectID: '',
      originalTrackNumber: '',
      parentId: '',
      rating: 0,
      refId: '',
      songId: this.emptySongId(),
      streamingURL: '',
      title: '',
    }
  }

  public generateEmptyTransportServiceStateDto(): TransportServiceStateDto {
    return {
      canPause: false,
      canRepeat: false,
      canSeek: false,
      canShuffle: false,
      canSkipNext: false,
      canSkipPrevious: false,
      repeat: false,
      shuffle: false,
      transportState: '',
      udn: ''
    }
  }

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
      media_date: '',
      composer: '',
      conductor: ''
    };
  }

  public generateSystemInformationDto(): SystemInformationDto {
    return {
      buildNumber: 'unknown',
      name:'',
      time:''
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
      albumDto: [],
      totalMatches: 0
    }
  }

  public generateEmptySearchRequestDto(): SearchRequestDto {
    return {
      startElement: 0,
      requestCount: 999,
      mediaServerUDN: '',
      searchRequest: '',
      sortCriteria: '',
      parentObjectID: ''
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
      songId: this.emptySongId(),
      composer: '',
      conductor: ''
    }
  }

  emptySongId(): MusicItemIdDto {
    return {
      acoustID: '',
      musicBrainzIdTrackId: '',
      objectID: '',
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
      bitDepth: 0,
      lossless: false,
      sampleRate: 0,
      bitrate: 0
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
      durationInSeconds: 0,
      contentFormat: '',
      size: 0,
      isStreaming: true,
    }
  }

  emptyInputSourceDto(): InputSourceDto {
    return {
      id: 0,
      Name: '',
      Type: '',
      Visible: false
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

  emptyDeviceDriverState(): DeviceDriverState {
    return {
      hasDeviceDriver: false,
      standby: true,
      volume: 0,
      balance: 0,
      rendererUDN: '',
      input: this.emptyInputSourceDto(),
    }
  }
}
