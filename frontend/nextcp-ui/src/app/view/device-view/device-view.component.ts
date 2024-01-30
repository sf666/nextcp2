import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';
import { RendererDropdownComponent } from '../../mediarenderer/dropdown/dropdown.component';
import { ServerDropdownComponent } from '../../mediaserver/dropdown/dropdown.component';

@Component({
    selector: 'device-view',
    templateUrl: './device-view.component.html',
    styleUrls: ['./device-view.component.scss'],
    standalone: true,
    imports: [ServerDropdownComponent, RendererDropdownComponent]
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
