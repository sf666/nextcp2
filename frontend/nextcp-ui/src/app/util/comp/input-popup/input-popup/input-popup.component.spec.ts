import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputPopupComponent } from './input-popup.component';

describe('InputPopupComponent', () => {
  let component: InputPopupComponent;
  let fixture: ComponentFixture<InputPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InputPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
