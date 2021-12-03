import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject, ElementRef } from '@angular/core';

@Component({
  selector: 'app-available-server',
  templateUrl: './available-server.component.html',
  styleUrls: ['./available-server.component.scss']
})

export class AvailableServerComponent implements OnInit {

  private readonly _matDialogRef: MatDialogRef<AvailableServerComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    _matDialogRef: MatDialogRef<AvailableServerComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    private popupService: PopupService,
    public deviceService: DeviceService
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const popupHeight = this.deviceService.getMediaServerList().length * 30 + 120;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 400, popupHeight);
  }

  close(): void {
    this._matDialogRef.close();
  }

  selectServer(udn: string): void {
    this.deviceService.setMediaServerByUdn(udn);
  }

  getSelectedClass(udn: string): string {
    if (this.deviceService.isMediaServerSelected(udn)) {
      return "selected";
    }
  }
}