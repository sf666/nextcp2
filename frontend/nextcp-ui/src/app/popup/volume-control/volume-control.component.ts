import { PopupService } from './../../util/popup.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RendererService } from './../../service/renderer.service';
import {
  Component,
  ElementRef,
  computed,
  signal,
  ChangeDetectionStrategy,
  inject,
} from '@angular/core';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-volume-control',
  templateUrl: './volume-control.component.html',
  styleUrls: ['./volume-control.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class VolumeControlComponent {
  rendererService = inject(RendererService);
  private popupService = inject(PopupService);

  private readonly _matDialogRef: MatDialogRef<VolumeControlComponent>;
  private readonly triggerElementRef: ElementRef;
  private closeOnMs: number = 0;
  private myTimer!: ReturnType<typeof setTimeout>;

  minVal = 0;
  maxVal = 100;
  expVal = 2;
  /** One click on the step buttons, in slider positions. */
  private readonly stepSize = 3;

  volControl = new FormControl(0);
  sliderPos = computed(() =>
    this.toSliderPos(this.rendererService.deviceDriverState().volume ?? 0),
  );

  /** Slider position while dragging — drives the fill and the readout. */
  sliderValue = signal<number>(0);
  /** Volume the readout shows: the real value, not the eased slider position. */
  displayVolume = computed(() => this.fromSliderPos(this.sliderValue()));

  constructor() {
    const _matDialogRef =
      inject<MatDialogRef<VolumeControlComponent>>(MatDialogRef);
    const data = inject<{
      trigger: ElementRef;
      id: string;
    }>(MAT_DIALOG_DATA);

    this.triggerElementRef = data.trigger;
    this._matDialogRef = _matDialogRef;
    // The column belongs to the volume button: centred right above it.
    this.popupService.configurePopupAboveTrigger(
      this._matDialogRef,
      this.triggerElementRef,
      74,
      320,
    );
  }

  ngOnInit(): void {
    this.volControl.setValue(this.sliderPos(), { emitEvent: false });
    this.sliderValue.set(this.sliderPos());

    this.volControl.valueChanges
      .pipe(debounceTime(120), distinctUntilChanged())
      .subscribe((val) => {
        if (val === null || val === undefined) {
          return;
        }
        const volume = this.fromSliderPos(val);
        this.rendererService.setVolume(volume);
        this.resetCloseTimer();
      });
  }

  /** Live feedback while dragging; the control itself is bound via formControl. */
  onSliderInput(val: number | string): void {
    const numVal = typeof val === 'string' ? Number(val) : val;
    this.sliderValue.set(numVal);
    this.resetCloseTimer();
  }

  /** Fine adjustment via the two buttons above and below the column. */
  stepBy(delta: number): void {
    const next = Math.min(100, Math.max(0, this.sliderValue() + delta));
    this.sliderValue.set(next);
    this.volControl.setValue(next);
  }

  stepUp(): void {
    this.stepBy(this.stepSize);
  }

  stepDown(): void {
    this.stepBy(-this.stepSize);
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
    return Math.round(Math.pow(Math.max(0, norm), 1 / this.expVal) * 100);
  }

  private fromSliderPos(pos: number): number {
    const norm = pos / 100;
    return Math.round(
      this.minVal + (this.maxVal - this.minVal) * Math.pow(norm, this.expVal),
    );
  }

  closeWindow(): void {
    if (Date.now() > this.closeOnMs) {
      this._matDialogRef.close();
    }
  }
}
