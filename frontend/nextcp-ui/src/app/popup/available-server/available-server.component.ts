import { PersistenceService } from './../../service/persistence/persistence.service';
import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject, ElementRef, computed } from '@angular/core';
import { ConfigurationService } from 'src/app/service/configuration.service';

@Component({
    selector: 'app-available-server',
    templateUrl: './available-server.component.html',
    styleUrls: ['./available-server.component.scss'],
    standalone: true
})

export class AvailableServerComponent {

  private readonly _matDialogRef: MatDialogRef<AvailableServerComponent>;
  private readonly triggerElementRef: ElementRef;

  popupHeight = computed(() => this.deviceService.mediaServerList().length * 30 + 120);
  filteredMediaServerList = computed(() => {
    return this.deviceService.mediaServerList().filter(
      pl => {
        if (this.configurationService.findServerConfig(pl.udn)) {
          return this.configurationService.findServerConfig(pl.udn).enabled;
        } else {
          console.log("server config not found for : " + pl.friendlyName);
          return true;
        }
      })
  });


  constructor(
    _matDialogRef: MatDialogRef<AvailableServerComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    private popupService: PopupService,
    private persistenceService: PersistenceService,
    public deviceService: DeviceService,
    public configurationService : ConfigurationService,
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 400, this.popupHeight());
  }

  close(): void {
    this._matDialogRef.close();
  }

  selectServer(udn: string): void {
    // delete last stored path if server is selected manually
    this.persistenceService.clearLastMediaServerDevice();
    this.persistenceService.setCurrentObjectID("0");
    this.persistenceService.setNewMediaServerDevice(udn);
    this.deviceService.setMediaServerByUdn(udn);
    this.close();
  }

  getSelectedClass(udn: string): string {
    if (this.deviceService.isMediaServerSelected(udn)) {
      return "selected";
    }
  }
}