import { Router } from '@angular/router';
import { RadioNetworksService } from './../../../service/radio-networks.service';
import { NetworkComponent } from './../network/network.component';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RadioNetwork } from 'src/app/service/dto';

@Component({
  selector: 'networks',
  standalone: true,
  imports: [],
  templateUrl: './networks.component.html',
  styleUrl: './networks.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class NetworksComponent {
  constructor(
    public radioNetworksService: RadioNetworksService,
    private router: Router
  ) { }

  public navigateToNetwork(network: string) {
    this.router.navigate(['/network', network]);
  }

  selectNetwork(network: RadioNetwork) {
    console.log("selecting network : " + network.network);
    const encoded = encodeURI(network.network);

    this.router.navigate(['/getNetworkChannels', encoded]);
  }

}
