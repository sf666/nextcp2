import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, ElementRef, computed, ChangeDetectionStrategy, inject } from '@angular/core';
import { ConfigurationService } from 'src/app/service/configuration.service';

@Component({
  selector: 'app-available-renderer',
  templateUrl: './available-renderer.component.html',
  styleUrls: ['./available-renderer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class AvailableRendererComponent {
  private popupService = inject(PopupService);
  deviceService = inject(DeviceService);
  configurationService = inject(ConfigurationService);

  private readonly _matDialogRef: MatDialogRef<AvailableRendererComponent>;
  private readonly triggerElementRef: ElementRef;

  // Popup chrome that is not part of the scrollable row area:
  // header (52px) + header border (1px) + list padding (2x8px) + panel borders (2px).
  private readonly CHROME_HEIGHT = 71;
  private readonly ROW_HEIGHT = 44;
  private readonly MAX_HEIGHT = 560;

  // Derive the exact popup height from the rendered rows so there is no
  // empty gap for few devices and no clipping for many.
  popupHeight = computed(() => {
    const rows = this.filteredMediaRendererList().length;
    return Math.min(rows * this.ROW_HEIGHT + this.CHROME_HEIGHT, this.MAX_HEIGHT);
  });

  filteredMediaRendererList = computed(() => {
    const discovered = this.deviceService.mediaRendererList().filter((pl) => {
      const rendererConfig = this.configurationService.findRendererConfig(
        pl.udn,
      );
      if (rendererConfig) {
        return rendererConfig.active;
      } else {
        console.log('renderer config not found for : ' + pl.friendlyName);
        return true;
      }
    });
    // Offer the synthetic "This Device" renderer as the first choice.
    return [this.deviceService.localBrowserRenderer, ...discovered];
  });

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<AvailableRendererComponent>>(MatDialogRef);
    const data = inject<{
      trigger: ElementRef;
      id: string;
    }>(MAT_DIALOG_DATA);

    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    this.popupService.configurePopupPosition(
      this._matDialogRef,
      this.triggerElementRef,
      360,
      this.popupHeight(),
    );
  }

  close(): void {
    this._matDialogRef.close();
  }

  selectRenderer(udn: string): void {
    this.deviceService.setMediaRendererByUdn(udn);
    this.close();
  }
}
