import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayHeaderOptionsComponent } from './display-header-options.component';

describe('DisplayHeaderOptionsComponent', () => {
  let component: DisplayHeaderOptionsComponent;
  let fixture: ComponentFixture<DisplayHeaderOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisplayHeaderOptionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DisplayHeaderOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
