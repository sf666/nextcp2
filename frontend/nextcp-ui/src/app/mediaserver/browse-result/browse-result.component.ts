import { ScrollViewService } from './../../util/scroll-view.service';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { Component } from '@angular/core';
import { DeviceService } from 'src/app/service/device.service';

@Component({
  selector: 'mediaServer-browse-result',
  templateUrl: './browse-result.component.html',
  styleUrls: ['./browse-result.component.scss']
})
export class BrowseResultComponent {

  constructor(
    private deviceService: DeviceService,
    private cdsBrowsePathService: CdsBrowsePathService,
    private scrollViewService: ScrollViewService,
  ) {
  }

  isRendererSelected(): boolean {
    return this.deviceService.selectedMediaServerDevice.udn.length > 0;
  }

  domChange(event: any) {
    if (this.cdsBrowsePathService.scrollToID.length > 0) {
      this.scrollViewService.scrollIntoViewID(this.cdsBrowsePathService.scrollToID);
    }
  }
}
