import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { DeviceDetailsDto, DeviceServiceDto } from './../../service/dto.d';
import { DeviceService } from './../../service/device.service';
import { ServerFeature } from './../../service/server-feature';
import { ToastService } from './../../service/toast/toast.service';

/**
 * What one device says about itself: its description, the UPnP services it announces and, for a
 * media server, the capabilities nextCP/2 switches its UI on.
 *
 * Read when the dialog opens, never cached - a device that answered a minute ago may be gone.
 */
@Component({
  selector: 'app-device-info',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [],
  templateUrl: './device-info.component.html',
  styleUrl: './device-info.component.scss',
})
export class DeviceInfoComponent {
  private readonly deviceService = inject(DeviceService);
  private readonly toastService = inject(ToastService);
  readonly dialogRef = inject<MatDialogRef<DeviceInfoComponent>>(MatDialogRef);

  private readonly data: { udn: string; friendlyName: string } =
    inject(MAT_DIALOG_DATA);

  readonly friendlyName = this.data.friendlyName;
  readonly details = signal<DeviceDetailsDto | undefined>(undefined);
  readonly failed = signal<boolean>(false);
  /** serviceId of the service whose actions are unfolded; only one at a time keeps the dialog short. */
  readonly openService = signal<string>('');

  /** Every capability there is, so the dialog also shows what this server does NOT offer. */
  readonly allFeatures = Object.values(ServerFeature);

  readonly loading = computed(() => !this.details() && !this.failed());

  constructor() {
    this.deviceService.deviceDetails(this.data.udn).subscribe({
      next: (details) => this.details.set(details),
      error: () => this.failed.set(true),
    });
  }

  hasFeature(feature: string): boolean {
    return this.details()?.features?.includes(feature) === true;
  }

  toggleService(service: DeviceServiceDto): void {
    this.openService.set(
      this.openService() === service.serviceId ? '' : service.serviceId,
    );
  }

  /** "12 actions", or what it means when a device announced a service we could not read. */
  actionSummary(service: DeviceServiceDto): string {
    const count = service.actions?.length ?? 0;
    if (count === 0) {
      return 'service description not read';
    }
    return count === 1 ? '1 action' : `${count} actions`;
  }

  /** The whole dialog as text, so it can go into a bug report instead of a screenshot. */
  copyAsText(): void {
    const d = this.details();
    if (!d) {
      return;
    }
    const lines: string[] = [
      `${d.friendlyName} (${d.mediaServer ? 'media server' : 'media renderer'})`,
      `type: ${d.deviceType}`,
      `manufacturer: ${d.manufacturer ?? '-'}`,
      `model: ${d.modelName ?? '-'} ${d.modelNumber ?? ''}`.trim(),
      `serial: ${d.serialNumber ?? '-'}`,
      `address: ${d.ipAddress ?? '-'}`,
      `udn: ${d.udn}`,
    ];
    if (d.mediaServer) {
      lines.push(`features: ${d.features?.join(', ') || '-'}`);
      lines.push(`searchCaps: ${d.searchCaps || '-'}`);
    }
    lines.push('services:');
    for (const service of d.services ?? []) {
      lines.push(`  ${service.serviceType} (${this.actionSummary(service)})`);
      if (service.actions?.length) {
        lines.push(`    ${service.actions.join(', ')}`);
      }
    }
    navigator.clipboard
      .writeText(lines.join('\n'))
      .then(() => this.toastService.success('device info', 'copied'))
      .catch(() => this.toastService.error('device info', 'could not copy'));
  }

  close(): void {
    this.dialogRef.close();
  }
}
