import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

export interface InputPopupData {
  title: string;
  okText: string;
  cancelText: string;
  inputText: string;
  labelInputText: string;
  inputTextExplanation: string;
}

@Component({
  selector: 'app-input-popup',
  standalone: true,
  imports: [MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    ],
  templateUrl: './input-popup.component.html',
  styleUrl: './input-popup.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InputPopupComponent {
  readonly dialogRef = inject(MatDialogRef<InputPopupComponent>);
  readonly data = inject<InputPopupData>(MAT_DIALOG_DATA);
  readonly inputText = model(this.data.inputText);

  onNoClick() {
    this.dialogRef.close();
  }

  onYesClick() {
    this.dialogRef.close(this.data.inputText);
  }
}
