import { DeviceService } from 'src/app/service/device.service';
import { BackgroundImageService } from './../../util/background-image.service';
import { RadioService } from './../../service/radio.service';
import { TransportService } from '../../service/transport.service';
import { RadioStation, MusicItemDto } from './../../service/dto.d';
import { ConfigurationService } from './../../service/configuration.service';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';

@Component({
  selector: 'app-radio',
  templateUrl: './radio.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./radio.component.scss'],
  standalone: true,
})
export class RadioComponent implements OnInit {
  configurationService = inject(ConfigurationService);
  radioService = inject(RadioService);
  private layoutService = inject(LayoutService);
  deviceService = inject(DeviceService);
  private backgroundImageService = inject(BackgroundImageService);
  private transportService = inject(TransportService);

  constructor() {
    console.log('[constructor] RadioComponent');
  }

  ngOnInit(): void {
    this.layoutService.setFramedViewWithoutNavbar();
  }

  play(radio: RadioStation) {
    this.backgroundImageService.setBackgroundImageMainScreen(radio.artworkUrl);
    this.transportService.playRadio(radio);
  }

  playOh(radioStation: MusicItemDto) {
    this.backgroundImageService.setBackgroundImageMainScreen(
      radioStation.albumArtUrl,
    );
    this.radioService.playOpenHomeStation(radioStation);
  }

  hasRadioStations(): boolean {
    return (
      (this.configurationService.serverConfig?.radioStation?.length ?? 0) > 0 ||
      (this.radioService.radioItems()?.length ?? 0) > 0
    );
  }
}
