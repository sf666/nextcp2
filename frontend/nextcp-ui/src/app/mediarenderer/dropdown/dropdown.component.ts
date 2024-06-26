import { MediaRendererDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgbDropdownButtonItem, NgbDropdownItem } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'mediaRendererDropdown',
    templateUrl: './dropdown.component.html',
    styleUrls: ['./dropdown.component.scss'],
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgbDropdownButtonItem, NgbDropdownItem]
})

export class RendererDropdownComponent {

  @Input() displayButtonText: boolean = true;
  @Output() selectedRenderer = new EventEmitter<MediaRendererDto>();

  constructor(private deviceService: DeviceService) { }

  public get mediaRendererList(): MediaRendererDto[] {
    return this.deviceService.mediaRendererList();
  }

  public setCurrentMediaRenderer(device: MediaRendererDto) {
    this.selectedRenderer.emit(device);
  }

  public get currentMediaRenderer(): MediaRendererDto {
    return this.deviceService.selectedMediaRendererDevice();
  }

  public get labelName(): string {
    if (this.displayButtonText) {
      return this.currentMediaRenderer.friendlyName;
    }
    return "";
  }
}
