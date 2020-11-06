import { BackgroundImageService } from './../../util/background-image.service';
import { RadioService } from './../../service/radio.service';
import { AvtransportService } from './../../service/avtransport.service';
import { RadioStation, MusicItemDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-radio',
  templateUrl: './radio.component.html',
  styleUrls: ['./radio.component.scss']
})
export class RadioComponent {

  constructor(
      public configurationService: ConfigurationService,
      public radioService: RadioService,
      private backgroundImageService: BackgroundImageService,
      private avtransportService: AvtransportService) { 
        console.log("[constructor] RadioComponent");
      }

  play (radio: RadioStation) {
    this.backgroundImageService.setBackgroundImageMainScreen(radio.artworkUrl);
    this.avtransportService.playRadio(radio);    
  }

  playOh (radioStation: MusicItemDto)
  {
    this.backgroundImageService.setBackgroundImageMainScreen(radioStation.albumArtUrl);
    this.radioService.playOpenHomeStation(radioStation);
  }
}
