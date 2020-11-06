// Generated using typescript-generator version 2.0.400 on 2020-11-06 09:05:30.

export interface AudioFormat {
    nrAudioChannels: number;
    sampleFrequency: number;
    bitsPerSample: number;
    bitrate: number;
    filetype: string;
}

export interface BrowseRequestDto {
    mediaServerUDN: string;
    objectID: string;
    sortCriteria: string;
}

export interface Config {
    generateUpnpCode: boolean;
    generateUpnpCodePath: string;
    libraryPath: string;
    embeddedServerPort: number;
    sseEmitterTimeout: number;
    log4jConfigFile: string;
    loggingDateTimeFormat: string;
    clientConfig: UiClientConfig[];
    radioStation: RadioStation[];
    rendererDevices: RendererDeviceConfiguration[];
    ratingSupport: RatingSupport;
}

export interface ContainerDto {
    id: string;
    parentID: string;
    title: string;
    objectClass: string;
    childCount: number;
    createClass: string;
    searchClass: string;
    searchable: boolean;
    mediaServerUDN: string;
    albumartUri: string;
    artist: string;
    rating: number;
    creator: string;
}

export interface ContainerItemDto {
    currentContainer: ContainerDto;
    containerDto: ContainerDto[];
    albumDto: ContainerDto[];
    musicItemDto: MusicItemDto[];
}

export interface DeviceDriverCapability {
    deviceType: string;
    deviceDescription: string;
}

export interface DeviceDriverState {
    rendererUDN: string;
    volume: number;
    standby: boolean;
}

export interface GenericBooleanRequest {
    deviceUDN: string;
    value: boolean;
}

export interface GenericNumberRequest {
    deviceUDN: string;
    value: number;
}

export interface GenericStringRequest {
    deviceUDN: string;
    value: string;
}

export interface InputSourceDto {
    id: number;
    Name: string;
    Type: string;
    Visible: boolean;
}

export interface MediaRendererDto {
    udn: string;
    friendlyName: string;
}

export interface MediaRendererSetVolume {
    rendererUDN: string;
    volume: number;
}

export interface MediaRendererSwitchPower {
    rendererUDN: string;
    standby: boolean;
}

export interface MediaServerDto {
    udn: string;
    friendlyName: string;
}

export interface MusicItemDto {
    mediaServerUDN: string;
    streamingURL: string;
    objectID: string;
    objectClass: string;
    parentId: string;
    refId: string;
    currentTrackMetadata: string;
    creator: string;
    title: string;
    artistName: string;
    numberOfThisDisc: string;
    originalTrackNumber: string;
    album: string;
    date: string;
    audioFormat: AudioFormat;
    albumArtUrl: string;
    rating: number;
}

export interface PlayOpenHomeRadioDto {
    mediaRendererDto: MediaRendererDto;
    radioStation: MusicItemDto;
}

export interface PlayRadioDto {
    mediaRendererDto: MediaRendererDto;
    radioStation: RadioStation;
}

export interface PlayRequestDto {
    mediaRendererDto: MediaRendererDto;
    streamUrl: string;
    streamMetadata: string;
}

export interface PlaylistAddContainerRequest {
    shuffle: boolean;
    containerDto: ContainerDto;
    mediaRendererUdn: string;
}

export interface PlaylistState {
    udn: string;
    TracksMax: number;
    Shuffle: boolean;
    Repeat: boolean;
    TransportState: string;
    ProtocolInfo: string;
    Id: number;
}

export interface QuickSearchRequestDto {
    mediaServerUDN: string;
    searchRequest: string;
    sortCriteria: string;
}

export interface QuickSearchResultDto {
    musicItems: MusicItemDto[];
    albumItems: ContainerDto[];
    artistItems: ContainerDto[];
    playlistItems: ContainerDto[];
}

export interface RadioState {
    ChannelsMax: number;
    Id: number;
    Metadata: string;
    ProtocolInfo: string;
    TransportState: string;
    Uri: string;
}

export interface RadioStation {
    id: number;
    stationName: string;
    resourceUrl: string;
    artworkUrl: string;
}

export interface RatingSupport {
    musicRootPath: string;
    databaseFilename: string;
    supportedFileTypes: string;
}

export interface RendererDeviceConfiguration {
    mediaRenderer: MediaRendererDto;
    ip: string;
    displayString: string;
    active: boolean;
    hasOpenHomeDeviceDriver: boolean;
    deviceDriverType: string;
    connectionString: string;
}

export interface RendererPlaylist {
    udn: string;
    musicItemDto: MusicItemDto[];
}

export interface TrackInfoDto {
    mediaRendererUdn: string;
    streaming: boolean;
    detailsCount: number;
    metatextCount: number;
    metadata: string;
    trackCount: number;
    uri: string;
    codecName: string;
    metatext: string;
    currentTrack: MusicItemDto;
    duration: string;
}

export interface TrackTimeDto {
    mediaRendererUdn: string;
    duration: number;
    durationDisp: string;
    seconds: number;
    secondsDisp: string;
    trackCount: number;
    percent: number;
}

export interface UiClientConfig {
    uuid: string;
    clientName: string;
    defaultMediaServer: MediaServerDto;
    defaultMediaRenderer: MediaRendererDto;
}

export interface UpnpAvTransportState {
    mediaRenderer: MediaRendererDto;
    AbsoluteTimePosition: string;
    CurrentTrackURI: string;
    CurrentTrackMetaData: MusicItemDto;
    RelativeCounterPosition: number;
    TransportStatus: string;
    AVTransportURIMetaData: MusicItemDto;
    TransportState: string;
    CurrentTrack: number;
    PlaybackStorageMedium: string;
    PossibleRecordQualityModes: string;
    NextAVTransportURIMetaData: string;
    NumberOfTracks: number;
    CurrentMediaDuration: string;
    NextAVTransportURI: string;
    RecordStorageMedium: string;
    AVTransportURI: string;
    TransportPlaySpeed: string;
    AbsoluteCounterPosition: number;
    RelativeTimePosition: string;
    CurrentPlayMode: string;
    CurrentTrackDuration: string;
    PossiblePlaybackStorageMedia: string;
    CurrentRecordQualityMode: string;
    RecordMediumWriteStatus: string;
    CurrentTransportActions: string;
    PossibleRecordStorageMedia: string;
}
