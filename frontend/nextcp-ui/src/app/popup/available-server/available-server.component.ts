import { PersistenceService } from './../../service/persistence/persistence.service';
import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Component, ElementRef, computed, ChangeDetectionStrategy, inject } from '@angular/core';
import { ConfigurationService } from 'src/app/service/configuration.service';

@Component({
  selector: 'app-available-server',
  templateUrl: './available-server.component.html',
  styleUrls: ['./available-server.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class AvailableServerComponent {
  private popupService = inject(PopupService);
  private persistenceService = inject(PersistenceService);
  deviceService = inject(DeviceService);
  configurationService = inject(ConfigurationService);

  private readonly _matDialogRef: MatDialogRef<AvailableServerComponent>;
  private readonly triggerElementRef: ElementRef;

  /** Popup width in px. */
  private readonly POPUP_WIDTH = 400;
  filteredMediaServerList = computed(() => {
    return this.deviceService.mediaServerList().filter((pl) => {
      const serverConfig = this.configurationService.findServerConfig(pl.udn);
      if (serverConfig) {
        return serverConfig.enabled;
      } else {
        console.log('server config not found for : ' + pl.friendlyName);
        return true;
      }
    });
  });

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<AvailableServerComponent>>(MatDialogRef);
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

  selectServer(udn: string): void {
    // delete last stored path if server is selected manually
    this.persistenceService.clearLastMediaServerDevice();
    this.persistenceService.setCurrentObjectID('0');
    this.persistenceService.setNewMediaServerDevice(udn);
    this.deviceService.setMediaServerByUdn(udn);
    this.close();
  }
}
