import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputFieldDialogComponent } from './input-field-dialog.component';

describe('InputFieldDialogComponent', () => {
  let component: InputFieldDialogComponent;
  let fixture: ComponentFixture<InputFieldDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputFieldDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputFieldDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
