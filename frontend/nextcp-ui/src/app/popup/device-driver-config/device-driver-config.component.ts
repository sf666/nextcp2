import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { RendererDeviceConfiguration } from 'src/app/service/dto.d';

/**
 * The settings of the amplifier driver assigned to a renderer. The dialog edits a copy and hands it
 * back on apply, so closing it with cancel leaves the card untouched.
 */
export interface DeviceDriverConfigData {
  deviceDriverType: string;
  connectionString: string;
  powerOnVolPercent: number;
  powerOnBalance: number;
  setCoveredUpnpDeviceToMaxVolume: boolean;
}

@Component({
  selector: 'device-driver-config',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [FormsModule],
  templateUrl: './device-driver-config.component.html',
  styleUrl: './device-driver-config.component.scss',
})
export class DeviceDriverConfigComponent {
  dialogRef =
    inject<MatDialogRef<DeviceDriverConfigComponent>>(MatDialogRef);

  readonly driverType: string;
  connectionString = signal<string>('');
  powerOnVolPercent = signal<number>(0);
  powerOnBalance = signal<number>(0);
  setCoveredUpnpDeviceToMaxVolume = signal<boolean>(false);

  constructor() {
    const config = inject<RendererDeviceConfiguration>(MAT_DIALOG_DATA);
    this.driverType = config.deviceDriverType ?? '';
    this.connectionString.set(config.connectionString ?? '');
    this.powerOnVolPercent.set(config.powerOnVolPercent ?? 0);
    this.powerOnBalance.set(config.powerOnBalance ?? 0);
    this.setCoveredUpnpDeviceToMaxVolume.set(
      config.setCoveredUpnpDeviceToMaxVolume === true,
    );
  }

  /** A driver that cannot be reached is of no use, so the connection string is required. */
  applyDisabled(): boolean {
    return this.connectionString().trim().length === 0;
  }

  apply(): void {
    const result: DeviceDriverConfigData = {
      deviceDriverType: this.driverType,
      connectionString: this.connectionString().trim(),
      powerOnVolPercent: this.powerOnVolPercent(),
      powerOnBalance: this.powerOnBalance(),
      setCoveredUpnpDeviceToMaxVolume: this.setCoveredUpnpDeviceToMaxVolume(),
    };
    this.dialogRef.close(result);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
