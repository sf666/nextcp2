import { MatSliderChange } from '@angular/material/slider';
import { PopupService } from './../../util/popup.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RendererService } from './../../service/renderer.service';
import { Component, OnInit, ElementRef, Inject } from '@angular/core';

@Component({
  selector: 'app-volume-control',
  templateUrl: './volume-control.component.html',
  styleUrls: ['./volume-control.component.scss']
})
export class VolumeControlComponent implements OnInit {

  private readonly _matDialogRef: MatDialogRef<VolumeControlComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    _matDialogRef: MatDialogRef<VolumeControlComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    public rendererService: RendererService,
    private popupService: PopupService,
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
  }


  ngOnInit(): void {
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 50, 450);
  }

  volChanged(event: MatSliderChange): void {
    this.rendererService.setVolume(event.value);
  }
}
