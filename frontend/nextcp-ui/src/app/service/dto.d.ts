/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2024-09-13 11:13:43.

export interface ApplicationConfig {
    myPlaylistFolderName: string;
    generateUpnpCode: boolean;
    generateUpnpCodePath: string;
    libraryPath: string;
    embeddedServerPort: number;
    embeddedServerSslPort: number;
    embeddedServerSslP12Keystore: string;
    embeddedServerSslP12KeystorePassword: string;
    sseEmitterTimeout: number;
    log4jConfigFile: string;
    loggingDateTimeFormat: string;
    globalSearchDelay: number;
    databaseFilename: string;
    itemsPerPage: number;
    nextPageAfter: number;
    pathToRestartScript: string;
    upnpStreamClient: string;
    upnpStreamServer: string;
    upnpBindInterface: string;
}

export interface AudioFormat {
    nrAudioChannels: number;
    sampleFrequency: number;
    bitsPerSample: number;
    bitrate: number;
    filetype: string;
    durationDisp: string;
    durationInSeconds: number;
    contentFormat: string;
    size: number;
    isStreaming: boolean;
}

export interface BrowseRequestDto {
    mediaServerUDN: string;
    objectID: string;
    sortCriteria: string;
    filter: string;
    start: number;
    count: number;
    searchInOID: string;
}

export interface Config {
    applicationConfig: ApplicationConfig;
    clientConfig: UiClientConfig[];
    radioStation: RadioStation[];
    musicbrainzSupport: MusicbrainzSupport;
    lastFmSessionKey: string;
    spotifyConfig: SpotifyConfigDto;
    mediaPlayerConfig: MediaPlayerConfigDto;
}

export interface ContainerDto {
    id: string;
    parentID: string;
    title: string;
    objectClass: string;
    conductor: string;
    composer: string;
    childCount: number;
    createClass: string;
    searchClass: string;
    searchable: boolean;
    mediaServerUDN: string;
    albumartUri: string;
    artist: string;
    rating: number;
    creator: string;
    media_date: string;
    genre: string;
}

export interface ContainerIdDto {
    id: string;
    title: string;
}

export interface ContainerItemDto {
    parentFolderTitle: string;
    currentContainer: ContainerDto;
    containerDto: ContainerDto[];
    albumDto: ContainerDto[];
    musicItemDto: MusicItemDto[];
    minimServerSupportTags: ContainerDto[];
    totalMatches: number;
}

export interface CreateServerPlaylistVO {
    mediaServerUdn: string;
    containerId: string;
    playlistName: string;
}

export interface DeviceDriverCapability {
    deviceType: string;
    deviceDescription: string;
}

export interface DeviceDriverState {
    hasDeviceDriver: boolean;
    rendererUDN: string;
    volume: number;
    standby: boolean;
    balance: number;
    input: InputSourceDto;
}

export interface DevicePowerChanged {
    udn: string;
    isPowerOn: boolean;
}

export interface DeviceVolumeChanged {
    udn: string;
    vol: number;
}

export interface FileChangedEventDto {
    type: string;
    path: string;
}

export interface GenericBooleanRequest {
    deviceUDN: string;
    value: boolean;
}

export interface GenericNumberRequest {
    deviceUDN: string;
    value: number;
}

export interface GenericResult {
    success: boolean;
    message: string;
    headerMessage: string;
}

export interface GenericStringRequest {
    deviceUDN: string;
    value: string;
}

export interface InputSourceChangeDto {
    udn: string;
    inputSource: InputSourceDto;
}

export interface InputSourceDto {
    id: number;
    Name: string;
    Type: string;
    Visible: boolean;
}

export interface MediaPlayerConfig {
    workdir: string;
    script: string;
    playType: string;
}

export interface MediaPlayerConfigDto {
    workdir: string;
    script: string;
    playType: string;
    overwrite: boolean;
    addToPlaylist: boolean;
    addToPlaylistId: ContainerIdDto;
    addToFolderId: ContainerIdDto;
    mediaServerUdn: string;
}

export interface MediaRendererDto {
    udn: string;
    friendlyName: string;
    services: MediaRendererServicesDto[];
    currentSource: InputSourceDto;
    allSources: InputSourceDto[];
}

export interface MediaRendererServicesDto {
    namespace: string;
    serviceName: string;
    version: string;
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
    extendedApi: boolean;
}

export interface MusicBrainzId {
    AlbumId: string;
    ArtistId: string;
    ReleaseTrackId: string;
    AlbumArtistId: string;
    WorkId: string;
}

export interface MusicItemDto {
    mediaServerUDN: string;
    streamingURL: string;
    objectID: string;
    objectClass: string;
    parentId: string;
    refId: string;
    songId: MusicItemIdDto;
    conductor: string;
    composer: string;
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
    genre: string;
    rating: number;
    musicBrainzId: MusicBrainzId;
}

export interface MusicItemIdDto {
    acoustID: string;
    musicBrainzIdTrackId: string;
    objectID: string;
}

export interface MusicbrainzSupport {
    username: string;
    password: string;
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

export interface RadioStation {
    id: number;
    stationName: string;
    resourceUrl: string;
    artworkUrl: string;
}

export interface RendererConfigDto {
    rendererDevices: RendererDeviceConfiguration[];
}

export interface RendererDeviceConfiguration {
    mediaRenderer: MediaRendererDto;
    ip: string;
    displayString: string;
    active: boolean;
    setCoveredUpnpDeviceToMaxVolume: boolean;
    hasOpenHomeDeviceDriver: boolean;
    deviceDriverType: string;
    connectionString: string;
    powerOnVolPercent: number;
    powerOnBalance: number;
}

export interface RendererPlaylist {
    udn: string;
    musicItemDto: MusicItemDto[];
}

export interface SearchRequestDto {
    parentObjectID: string;
    startElement: number;
    requestCount: number;
    mediaServerUDN: string;
    searchRequest: string;
    sortCriteria: string;
}

export interface SearchResultDto {
    parentID: string;
    musicItems: MusicItemDto[];
    albumItems: ContainerDto[];
    artistItems: ContainerDto[];
    playlistItems: ContainerDto[];
}

export interface SeekSecondsDto {
    rendererUDN: string;
    seconds: number;
}

export interface ServerConfigDto {
    serverDevices: ServerDeviceConfiguration[];
}

export interface ServerDeleteObjectRequest {
    serverUdn: string;
    objectId: string;
}

export interface ServerDeviceConfiguration {
    ip: string;
    displayString: string;
    enabled: boolean;
    mediaServer: MediaServerDto;
    apiKey: string;
}

export interface ServerPlaylistDto {
    albumArtUrl: string;
    playlistName: string;
    playlistId: string;
    numberOfElements: number;
    totalPlaytime: string;
}

export interface ServerPlaylistEntry {
    serverUdn: string;
    playlistObjectId: string;
    songObjectId: string;
}

export interface ServerPlaylists {
    mediaServerUdn: string;
    containerId: string;
    serverPlaylists: ServerPlaylistDto[];
}

export interface SpotifyConfigDto {
    accountConnected: boolean;
    clientId: string;
    refreshToken: string;
    redirectUrl: string;
}

export interface SystemInformationDto {
    buildNumber: string;
}

export interface ToastrMessage {
    clientID: string;
    type: string;
    header: string;
    body: string;
}

export interface TrackInfoDto {
    mediaRendererUdn: string;
    detailsCount: number;
    metatextCount: number;
    metadata: string;
    trackCount: number;
    uri: string;
    codecName: string;
    metatext: string;
    currentTrack: MusicItemDto;
    duration: string;
    sampleRate: number;
    lossless: boolean;
    bitDepth: number;
    bitrate: number;
}

export interface TrackTimeDto {
    mediaRendererUdn: string;
    duration: number;
    durationDisp: string;
    seconds: number;
    secondsDisp: string;
    trackCount: number;
    percent: number;
    streaming: boolean;
}

export interface TransportServiceStateDto {
    udn: string;
    transportState: string;
    canSkipNext: boolean;
    canSkipPrevious: boolean;
    canRepeat: boolean;
    canShuffle: boolean;
    canSeek: boolean;
    canPause: boolean;
    repeat: boolean;
    shuffle: boolean;
}

export interface UiClientConfig {
    uuid: string;
    clientName: string;
    defaultMediaServer: MediaServerDto;
    defaultMediaRenderer: MediaRendererDto;
}

export interface UmsServerApiKey {
    serverUuid: string;
    serverApiKey: string;
}

export interface UpdateStarRatingRequest {
    previousRating: number;
    newRating: number;
    mediaServerDevice: string;
    musicItemIdDto: MusicItemIdDto;
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
