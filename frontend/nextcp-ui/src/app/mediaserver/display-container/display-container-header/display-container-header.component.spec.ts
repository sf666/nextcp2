import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayContainerHeaderComponent } from './display-container-header.component';

describe('DisplayContainerHeaderComponent', () => {
  let component: DisplayContainerHeaderComponent;
  let fixture: ComponentFixture<DisplayContainerHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisplayContainerHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DisplayContainerHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
