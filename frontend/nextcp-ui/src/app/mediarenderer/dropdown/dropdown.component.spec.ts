import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RendererDropdownComponent } from './dropdown.component';

describe('DropdownComponent', () => {
  let component: RendererDropdownComponent;
  let fixture: ComponentFixture<RendererDropdownComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
    imports: [RendererDropdownComponent]
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
