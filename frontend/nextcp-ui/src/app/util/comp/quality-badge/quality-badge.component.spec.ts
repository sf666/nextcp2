import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QualityBadgeComponent } from './quality-badge.component';

describe('QualityBadgeComponent', () => {
  let component: QualityBadgeComponent;
  let fixture: ComponentFixture<QualityBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QualityBadgeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QualityBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
