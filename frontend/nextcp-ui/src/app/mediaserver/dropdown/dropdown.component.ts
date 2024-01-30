import { MediaServerDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgFor } from '@angular/common';
import { NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgbDropdownButtonItem, NgbDropdownItem } from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'mediaServerDropdown',
    templateUrl: './dropdown.component.html',
    styleUrls: ['./dropdown.component.scss'],
    standalone: true,
    imports: [NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgFor, NgbDropdownButtonItem, NgbDropdownItem]
})
export class ServerDropdownComponent {

  @Input() displayButtonText: boolean = true;
  @Output() selectedServer = new EventEmitter<MediaServerDto>();

  constructor(private deviceService: DeviceService) { }

  public mediaServerList(): MediaServerDto[] {
    return this.deviceService.getMediaServerList();
  }

  public setCurrentMediaServer(device: MediaServerDto) {
    this.selectedServer.emit(device);
  }
  
  public get currentMediaServer(): MediaServerDto {
    return this.deviceService.selectedMediaServerDevice;
  }
  
  public get labelName(): string {
    if (this.displayButtonText)
    {
      return this.currentMediaServer.friendlyName;
    }
    return "";
  }
}
