import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';

@Component({
  selector: 'device-view',
  templateUrl: './device-view.component.html',
  styleUrls: ['./device-view.component.scss']
})
export class DeviceViewComponent {

  constructor(private deviceService: DeviceService) { }

  mediaRendererChanged(event) {
    this.deviceService.selectedMediaRendererDevice = event;
  }

  mediaServerChanged(event) {
    this.deviceService.selectedMediaServerDevice = event;
  }
}
