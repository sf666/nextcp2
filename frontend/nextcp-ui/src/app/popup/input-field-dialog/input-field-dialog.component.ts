import { InputFieldConfig } from './input-field-dialog.d';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { MatFormField } from '@angular/material/form-field';

@Component({
    selector: 'app-input-field-dialog',
    templateUrl: './input-field-dialog.component.html',
    styleUrls: ['./input-field-dialog.component.scss'],
    standalone: true,
    imports: [FormsModule, ReactiveFormsModule, MatDialogTitle, MatDialogContent, MatFormField, MatInput, MatDialogActions, MatButton, MatDialogClose]
})

export class InputFieldDialogComponent implements OnInit {

  form: FormGroup;
  inputFieldConfig: InputFieldConfig = {
    cancelButtonText: "cancel",
    placeholder: "enter text",
    submitButtonText: "submit",
    title: "Enter data"
  }

  constructor(
    @Inject(MAT_DIALOG_DATA) data: InputFieldConfig,
    private dialogRef: MatDialogRef<InputFieldDialogComponent>,
    private formBuilder: FormBuilder,
  ) {
    if (data) {
      this.inputFieldConfig = data;
    }
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      myText: ''
    });
  }

  submit(form) {
    this.dialogRef.close(`${form.value.myText}`);
  }

  close(): void {
    this.dialogRef.close();
  }
}
