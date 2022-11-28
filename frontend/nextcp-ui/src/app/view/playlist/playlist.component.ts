import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { BackgroundImageService } from './../../util/background-image.service';
import { SseService } from './../../service/sse/sse.service';
import { DeviceService } from './../../service/device.service';
import { MusicItemDto } from './../../service/dto.d';
import { PlaylistService } from '../../service/playlist.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss'],
  providers: [CdsBrowsePathService, { provide: 'uniqueId', useValue: 'cds_playlist' }]
})
export class PlaylistComponent implements OnInit {

  constructor(
    public deviceService: DeviceService,
    private sseService: SseService,
    private backgroundImageService: BackgroundImageService,
    scrollViewService: CdsBrowsePathService,
    public playlistService: PlaylistService) {

    sseService.mediaRendererPlaylistStateChanged$.subscribe(data => {
      if (deviceService.isMediaRendererSelected(data.udn)) {
        scrollViewService.scrollIntoViewID(`PL-${data.Id}`);
      }
    });
  }

  ngOnInit(): void {
    this.playlistService.updatePlaylistItems();
    this.backgroundImageService.setBackgroundImageMainScreen("/assets/images/playlist_bg.png");
  }

  getActiveClass(item: MusicItemDto) : string{
    const id: number = +item.objectID;
    if (id === this.playlistService.playlistState.Id) {
      return "active";
    }
  }

  get hasPlaylistItems(): boolean {
    return this.playlistService.playlistItems?.length > 0;
  }
}
