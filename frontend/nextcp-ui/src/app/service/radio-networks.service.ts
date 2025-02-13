import { Injectable, signal } from '@angular/core';
import { HttpService } from './http.service';
import { AudioAddictChannelDto, RadioNetwork } from './dto';

@Injectable({
  providedIn: 'root'
})
export class RadioNetworksService {

  private baseUri = '/RadioNetworksService';
  public radioNetworks = signal<RadioNetwork[]>([]);

  constructor(
    private httpService: HttpService
  ) { 
    this.httpService.get<RadioNetwork[]>(this.baseUri, `/getNetworks`).subscribe(rn => this.radioNetworks.set(rn));
  }

  public getNetwork(nw : string) : RadioNetwork{
    return this.radioNetworks().find(network => network.network === nw);
  }
}
