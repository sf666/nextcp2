import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject, ElementRef, computed } from '@angular/core';
import { ConfigurationService } from 'src/app/service/configuration.service';

@Component({
  selector: 'app-available-renderer',
  templateUrl: './available-renderer.component.html',
  styleUrls: ['./available-renderer.component.scss'],
  standalone: true
})
export class AvailableRendererComponent {

  private readonly _matDialogRef: MatDialogRef<AvailableRendererComponent>;
  private readonly triggerElementRef: ElementRef;

  rendererCount = computed(() => this.deviceService.mediaRendererList().length);
  popupHeight = computed(() => this.rendererCount() * 30 + 120);

  filteredMediaRendererList = computed(() => {
    return this.deviceService.mediaServerList().filter(
      pl => {
        if (this.configurationService.findRendererConfig(pl.udn)) {
          return this.configurationService.findRendererConfig(pl.udn).active;
        } else {
          console.log("renderer config not found for : " + pl.friendlyName);
          return true;
        }
      })
  });

  constructor(
    _matDialogRef: MatDialogRef<AvailableRendererComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    private popupService: PopupService,
    public deviceService: DeviceService,
    public configurationService : ConfigurationService,
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, this.popupHeight());
  }

  close(): void {
    this._matDialogRef.close();
  }

  selectRenderer(udn: string): void {
    this.deviceService.setMediaRendererByUdn(udn);
    this.close();
  }

  getSelectedClass(udn: string): string {
    if (this.deviceService.isMediaRendererSelected(udn)) {
      return "selected";
    }
  }
}
