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

  /** Popup width in px. */
  private readonly POPUP_WIDTH = 360;

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
    // Height comes from the rendered rows, capped by the list's own max-height
    // (see the SCSS) - no row arithmetic to keep in step with the styling.
    this.popupService.configurePopupAtTrigger(
      this._matDialogRef,
      this.triggerElementRef,
      this.POPUP_WIDTH,
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
