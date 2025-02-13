import { ChangeDetectionStrategy, Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AudioAddictChannelDto, RadioNetwork } from 'src/app/service/dto';
import { HttpService } from 'src/app/service/http.service';
import { RadioNetworksService } from 'src/app/service/radio-networks.service';

@Component({
  selector: 'app-network',
  standalone: true,
  imports: [],
  templateUrl: './network.component.html',
  styleUrl: './network.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NetworkComponent implements OnInit {

  public channels = signal<AudioAddictChannelDto[]>([]);
  public network = signal<RadioNetwork>({ albumArt: '', displayName: '', network: '', quality: [] });
  RadioNetworksService

  constructor(
    private route: ActivatedRoute,
    private httpService: HttpService,
    public radioNetworksService: RadioNetworksService,
  ) { }

  ngOnInit(): void {
    const network = this.route.snapshot.paramMap.get('network');
    this.network.set(this.radioNetworksService.getNetwork(network));
    this.updateNetwork();
  }

  updateNetwork(): void {
    const path = "/getNetworkChannels/" + encodeURI(this.network().network);
    console.log("updating network path : " + path);
    this.httpService.get<AudioAddictChannelDto[]>("/RadioNetworksService", path).subscribe(nc => this.channels.set(nc));
  }

  selectChannel(channel: AudioAddictChannelDto) {
    console.log("select channel : " + channel.name);
  }

}
