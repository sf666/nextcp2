import { DeviceService } from 'src/app/service/device.service';
import { PopupService } from './../../util/popup.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogConfig } from '@angular/material/dialog';
import { Component, OnInit, Inject, ElementRef } from '@angular/core';

@Component({
  selector: 'app-available-renderer',
  templateUrl: './available-renderer.component.html',
  styleUrls: ['./available-renderer.component.scss']
})
export class AvailableRendererComponent implements OnInit {

  private readonly _matDialogRef: MatDialogRef<AvailableRendererComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    _matDialogRef: MatDialogRef<AvailableRendererComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    private popupService: PopupService,
    public deviceService: DeviceService
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }

  ngOnInit(): void {
    const popupHeight = this.deviceService.getMediaRendererList.length * 30 + 120;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 250, popupHeight);
  }

  close() : void {
    this._matDialogRef.close();
  }

  selectRenderer(udn : string): void {
    this.deviceService.setMediaRendererByUdn(udn);
  }

  getSelectedClass(udn: string): string {
    if (this.deviceService.isMediaRendererSelected(udn)) {
      return "selected";
    }
  }
}
