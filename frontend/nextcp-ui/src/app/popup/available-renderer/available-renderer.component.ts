import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, ElementRef, computed, ChangeDetectionStrategy, inject } from '@angular/core';
import { ConfigurationService } from 'src/app/service/configuration.service';
import { MatDialog } from '@angular/material/dialog';
import { MediaRendererDto } from 'src/app/service/dto';
import { DeviceInfoComponent } from '../device-info/device-info.component';
import { LocalPlayerService } from 'src/app/service/local-player.service';
import { ToastService } from 'src/app/service/toast/toast.service';

@Component({
  selector: 'app-available-renderer',
  templateUrl: './available-renderer.component.html',
  styleUrls: ['./available-renderer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AvailableRendererComponent {
  private popupService = inject(PopupService);
  private readonly infoDialog = inject(MatDialog);
  deviceService = inject(DeviceService);
  configurationService = inject(ConfigurationService);
  readonly localPlayer = inject(LocalPlayerService);
  private readonly toastService = inject(ToastService);

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

  /** Opens the info dialog for one renderer without switching to it. */
  showInfo(device: MediaRendererDto): void {
    this.infoDialog.open(DeviceInfoComponent, {
      width: '620px',
      maxWidth: '92vw',
      height: '640px',
      maxHeight: '90vh',
      panelClass: ['popup-glass'],
      data: { udn: device.udn, friendlyName: device.friendlyName },
    });
    this.close();
  }

  selectRenderer(udn: string): void {
    // iOS can route audio without reporting a wireless target to the page. Always offer
    // the picker for the selected browser player so returning to the device remains possible.
    if (udn === this.deviceService.LOCAL_BROWSER_UDN && this.localPlayer.airPlaySupported &&
        (this.deviceService.isLocalBrowserSelected() || this.localPlayer.airPlaySelected())) {
      this.selectAirPlay('device');
      return;
    }
    this.deviceService.setMediaRendererByUdn(udn);
    this.close();
  }

  selectAirPlay(output: 'airplay' | 'device' = 'airplay'): void {
    if (!this.localPlayer.showAirPlayPicker(output)) {
      this.toastService.error('Could not open the AirPlay device selection. Please try again.', 'AirPlay');
      return;
    }
    this.deviceService.setMediaRendererByUdn(this.deviceService.LOCAL_BROWSER_UDN);
    this.close();
  }
}
