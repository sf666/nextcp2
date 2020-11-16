import { Component } from '@angular/core';
import { DeviceService } from 'src/app/service/device.service';

@Component({
  selector: 'mediaServer-browse-result',
  templateUrl: './browse-result.component.html',
  styleUrls: ['./browse-result.component.scss']
})
export class BrowseResultComponent {

  constructor(private deviceService: DeviceService) {
  }

  isRendererSelected() : boolean {
    return this.deviceService.selectedMediaServerDevice.udn.length > 0;
  }
}