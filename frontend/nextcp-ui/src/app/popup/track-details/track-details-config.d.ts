import { MusicItemDto } from './../../service/dto.d';
export interface TrackDetailsData {
    albumartUri: string;
    inputSource: string;
    currentTrack: MusicItemDto
}