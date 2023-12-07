import { DeviceService } from 'src/app/service/device.service';
import { BackgroundImageService } from './../../util/background-image.service';
import { RadioService } from './../../service/radio.service';
import { TransportService } from '../../service/transport.service';
import { RadioStation, MusicItemDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component, OnInit } from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';

@Component({
  selector: 'app-radio',
  templateUrl: './radio.component.html',
  styleUrls: ['./radio.component.scss']
})
export class RadioComponent implements OnInit{

  constructor(
      public configurationService: ConfigurationService,
      public radioService: RadioService,
      private layoutService: LayoutService,
      public deviceService: DeviceService,
      private backgroundImageService: BackgroundImageService,
      private transportService: TransportService) { 
        console.log("[constructor] RadioComponent");
      }

  ngOnInit(): void {
    this.layoutService.setFramedViewWithoutNavbar();
  }
  
  play (radio: RadioStation) {
    this.backgroundImageService.setBackgroundImageMainScreen(radio.artworkUrl);
    this.transportService.playRadio(radio);    
  }

  playOh (radioStation: MusicItemDto)
  {
    this.backgroundImageService.setBackgroundImageMainScreen(radioStation.albumArtUrl);
    this.radioService.playOpenHomeStation(radioStation);
  }
}
