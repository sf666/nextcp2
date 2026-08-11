import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface ConfirmPopupData {
  title: string;
  /** What will happen, in one sentence. */
  message: string;
  /** Optional subject of the action, e.g. the playlist name. */
  detail?: string;
  confirmText: string;
  cancelText: string;
  /** Marks the confirming action as destructive. */
  danger?: boolean;
}

/**
 * Small yes/no dialog for actions that cannot be undone. Closes with `true`
 * when confirmed and with `undefined` on cancel / escape.
 */
@Component({
  selector: 'app-confirm-popup',
  standalone: true,
  imports: [],
  templateUrl: './confirm-popup.component.html',
  styleUrl: './confirm-popup.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ConfirmPopupComponent {
  private readonly dialogRef = inject(MatDialogRef<ConfirmPopupComponent>);
  readonly data = inject<ConfirmPopupData>(MAT_DIALOG_DATA);

  cancel(): void {
    this.dialogRef.close();
  }

  confirm(): void {
    this.dialogRef.close(true);
  }
}
