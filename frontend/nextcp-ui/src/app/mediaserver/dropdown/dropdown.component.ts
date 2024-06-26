import { MediaServerDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy, input, output } from '@angular/core';
import { NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgbDropdownButtonItem, NgbDropdownItem } from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'mediaServerDropdown',
    templateUrl: './dropdown.component.html',
    styleUrls: ['./dropdown.component.scss'],
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgbDropdownButtonItem, NgbDropdownItem]
})
export class ServerDropdownComponent {

  displayButtonText = input<boolean>(true);
  selectedServer = output<MediaServerDto>();

  constructor(private deviceService: DeviceService) { }

  public mediaServerList(): MediaServerDto[] {
    return this.deviceService.mediaServerList();
  }

  public setCurrentMediaServer(device: MediaServerDto) {
    this.selectedServer.emit(device);
  }
  
  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice();
  }
  
  public get labelName(): string {
    if (this.displayButtonText())
    {
      return this.currentMediaServer.friendlyName;
    }
    return "";
  }
}
