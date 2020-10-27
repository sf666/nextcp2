import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RendererDropdownComponent } from './dropdown.component';

describe('DropdownComponent', () => {
  let component: RendererDropdownComponent;
  let fixture: ComponentFixture<RendererDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RendererDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RendererDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
