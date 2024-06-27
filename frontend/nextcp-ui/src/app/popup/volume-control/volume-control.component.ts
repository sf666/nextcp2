import { PopupService } from './../../util/popup.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RendererService } from './../../service/renderer.service';
import { Component, OnInit, ElementRef, Inject } from '@angular/core';
import { MatSlider, MatSliderThumb } from '@angular/material/slider';
import { MatIcon } from '@angular/material/icon';

@Component({
    selector: 'app-volume-control',
    templateUrl: './volume-control.component.html',
    styleUrls: ['./volume-control.component.scss'],
    standalone: true,
    imports: [MatIcon, MatSlider, MatSliderThumb]
})
export class VolumeControlComponent {

  private readonly _matDialogRef: MatDialogRef<VolumeControlComponent>;
  private readonly triggerElementRef: ElementRef;
  private closeOnMs: number;
  private myTimer : NodeJS.Timeout;

  constructor(
    _matDialogRef: MatDialogRef<VolumeControlComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    public rendererService: RendererService,
    private popupService: PopupService,
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 50, 482);
  }

  volChanged(event): void {
    this.rendererService.setVolume(event);
    this.closeOnMs = Date.now() + 3000;
    if (this.myTimer) {
      clearTimeout(this.myTimer);
    }
    this.myTimer = setTimeout(() => {
      this.closeWindow();
    }, 4000);
  }

  closeWindow() : void {
    if (Date.now() > this.closeOnMs) {
      this._matDialogRef.close();
    }
  }
}
