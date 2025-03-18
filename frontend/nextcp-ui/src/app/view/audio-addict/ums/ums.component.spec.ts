import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UmsComponent } from './ums.component';

describe('UmsComponent', () => {
  let component: UmsComponent;
  let fixture: ComponentFixture<UmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UmsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
