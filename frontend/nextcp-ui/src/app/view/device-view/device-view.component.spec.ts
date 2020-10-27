import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceViewComponent } from './device-view.component';

describe('DeviceViewComponent', () => {
  let component: DeviceViewComponent;
  let fixture: ComponentFixture<DeviceViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeviceViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeviceViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
