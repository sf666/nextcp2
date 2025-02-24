import { ChangeDetectionStrategy, Component, model, OnInit, signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ActivatedRoute } from '@angular/router';
import { AudioAddictChannelDto, RadioNetwork } from 'src/app/service/dto';
import { HttpService } from 'src/app/service/http.service';
import { RadioNetworksService } from 'src/app/service/radio-networks.service';
import { TransportService } from 'src/app/service/transport.service';

@Component({
  selector: 'app-network',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatLabel,
    MatSelectModule,
    FormsModule,
    MatOptionModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatSlideToggleModule,
    ReactiveFormsModule
  ],

  templateUrl: './network.component.html',
  styleUrl: './network.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NetworkComponent implements OnInit {

  public channels = signal<AudioAddictChannelDto[]>([]);
  public network = signal<RadioNetwork>({ albumArt: '', displayName: '', network: '', quality: [] });
  public filters = signal<string[]>([]);
  public filter = model<string>("");

  RadioNetworksService

  constructor(
    private route: ActivatedRoute,
    private httpService: HttpService,
    public radioNetworksService: RadioNetworksService,
    private transportService: TransportService,
  ) {
    toObservable(this.filter).subscribe(newFilter => this.filterSelected(newFilter))
  }

  ngOnInit(): void {
    const network = this.route.snapshot.paramMap.get('network');
    this.network.set(this.radioNetworksService.getNetwork(network));
    this.readFilter();
  }

  filterSelected(newFilter: string): void {
    if (newFilter) {
      const path = "/getFilteredChannels/" + encodeURI(this.network().network);
      this.httpService.post<AudioAddictChannelDto[]>("/RadioNetworksService", path, newFilter).subscribe(f => this.updateChannelsForFilter(f));
    }
  }

  readFilter(): void {
    const path = "/getNetworkFilter/" + encodeURI(this.network().network);
    console.log("reading available filter ... " + path);
    this.httpService.get<string[]>("/RadioNetworksService", path).subscribe(f => this.updateFilters(f));
  }

  updateFilters(f: string[]): void {
    this.filters.set(f);
    this.filter.set(f[0]);
  }

  updateChannelsForFilter(c: AudioAddictChannelDto[]): void {
    this.channels.set(c);
  }

  selectChannel(channel: AudioAddictChannelDto) {
    console.log("select channel : " + channel.name);
    this.transportService.playUrl(channel.streamUrl);
  }
}

