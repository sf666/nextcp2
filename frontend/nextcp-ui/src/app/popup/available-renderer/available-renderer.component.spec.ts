import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableRendererComponent } from './available-renderer.component';

describe('AvailableRendererComponent', () => {
  let component: AvailableRendererComponent;
  let fixture: ComponentFixture<AvailableRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AvailableRendererComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AvailableRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
