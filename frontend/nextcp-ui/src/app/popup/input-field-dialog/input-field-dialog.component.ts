import { InputFieldConfig } from './input-field-dialog.d';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-input-field-dialog',
  templateUrl: './input-field-dialog.component.html',
  styleUrls: ['./input-field-dialog.component.scss']
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
