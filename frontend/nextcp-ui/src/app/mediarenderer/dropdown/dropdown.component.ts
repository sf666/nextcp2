import { MediaRendererDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'mediaRendererDropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.scss']
})

export class RendererDropdownComponent {

  @Input() displayButtonText: boolean = true;
  @Output() selectedRenderer = new EventEmitter<MediaRendererDto>();

  constructor(private deviceService: DeviceService) { }

  public get mediaRendererList(): MediaRendererDto[] {
    return this.deviceService.getMediaRendererList();
  }

  public setCurrentMediaRenderer(device: MediaRendererDto) {
    this.selectedRenderer.emit(device);
  }

  public get currentMediaRenderer(): MediaRendererDto {
    return this.deviceService.selectedMediaRendererDevice;
  }

  public get labelName(): string {
    if (this.displayButtonText) {
      return this.currentMediaRenderer.friendlyName;
    }
    return "";
  }
}
