import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableServerComponent } from './available-server.component';

describe('AvailableServerComponent', () => {
  let component: AvailableServerComponent;
  let fixture: ComponentFixture<AvailableServerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvailableServerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AvailableServerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
