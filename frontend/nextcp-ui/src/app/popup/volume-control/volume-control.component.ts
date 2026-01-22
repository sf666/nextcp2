import { PopupService } from './../../util/popup.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RendererService } from './../../service/renderer.service';
import { Component, OnInit, ElementRef, Inject, computed, ViewEncapsulation } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
    selector: 'app-volume-control',
    templateUrl: './volume-control.component.html',
    styleUrls: ['./volume-control.component.scss'],
    standalone: true,
    imports: [ReactiveFormsModule],
    encapsulation: ViewEncapsulation.None
})
export class VolumeControlComponent {

  private readonly _matDialogRef: MatDialogRef<VolumeControlComponent>;
  private readonly triggerElementRef: ElementRef;
  private closeOnMs: number;
  private myTimer : NodeJS.Timeout;

  minVal = 0;
  maxVal = 90;
  expVal = 2;
  volControl = new FormControl(0);
  sliderPos = computed(() => this.toSliderPos(this.rendererService.deviceDriverState().volume ?? 0));

  constructor(
    _matDialogRef: MatDialogRef<VolumeControlComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef, id: string },
    public rendererService: RendererService,
    private popupService: PopupService,
  ) {
    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 50, 300);
  }

  ngOnInit(): void {
    this.volControl.setValue(this.sliderPos(), { emitEvent: false });

    this.volControl.valueChanges.pipe(
      debounceTime(120),
      distinctUntilChanged()
    ).subscribe(val => {
      if (val === null || val === undefined) { return; }
      const volume = this.fromSliderPos(val);
      this.rendererService.setVolume(volume);
      this.resetCloseTimer();
    });
  }

  onSliderInput(val: number | string): void {
    const numVal = typeof val === 'string' ? Number(val) : val;
    this.volControl.setValue(numVal);
  }

  private resetCloseTimer(): void {
    this.closeOnMs = Date.now() + 3000;
    if (this.myTimer) {
      clearTimeout(this.myTimer);
    }
    this.myTimer = setTimeout(() => {
      this.closeWindow();
    }, 4000);
  }

  private toSliderPos(volume: number): number {
    const norm = (volume - this.minVal) / (this.maxVal - this.minVal);
    const toSlidePos = Math.round(Math.pow(Math.max(0, norm), 1 / this.expVal) * 100);
    console.log("to slider pos " + volume + " to " + toSlidePos);
    return toSlidePos;
  }

  private fromSliderPos(pos: number): number {
    const norm = pos / 100;
    const fromSlidePos = Math.round(this.minVal + (this.maxVal - this.minVal) * Math.pow(norm, this.expVal));
    console.log("from slider pos " + pos + " to " + fromSlidePos);
    return fromSlidePos;
  }

  closeWindow() : void {
    if (Date.now() > this.closeOnMs) {
      this._matDialogRef.close();
    }
  }
}
