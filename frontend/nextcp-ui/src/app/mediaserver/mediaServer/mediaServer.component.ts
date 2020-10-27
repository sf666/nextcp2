import { MediaServerDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';

@Component({
  selector: 'mediaServer',
  templateUrl: './mediaServer.component.html',
  styleUrls: ['./mediaServer.component.scss']
})
export class MediaServerComponent {

  constructor(private deviceService: DeviceService) { }
}
