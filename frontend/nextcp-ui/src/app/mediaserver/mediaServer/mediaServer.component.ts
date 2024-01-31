import { MediaServerDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { Component } from '@angular/core';
import { ServerDropdownComponent } from '../dropdown/dropdown.component';

@Component({
    selector: 'mediaServer',
    templateUrl: './mediaServer.component.html',
    styleUrls: ['./mediaServer.component.scss'],
    standalone: true,
    imports: [ServerDropdownComponent]
})
export class MediaServerComponent {

  constructor(private deviceService: DeviceService) { }
}
