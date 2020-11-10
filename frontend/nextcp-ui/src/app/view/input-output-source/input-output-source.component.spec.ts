import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputOutputSourceComponent } from './input-output-source.component';

describe('InputOutputSourceComponent', () => {
  let component: InputOutputSourceComponent;
  let fixture: ComponentFixture<InputOutputSourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputOutputSourceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputOutputSourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
