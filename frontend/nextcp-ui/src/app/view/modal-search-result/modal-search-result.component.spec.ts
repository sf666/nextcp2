import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalSearchResultComponent } from './modal-search-result.component';

describe('ModalSearchResultComponent', () => {
  let component: ModalSearchResultComponent;
  let fixture: ComponentFixture<ModalSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalSearchResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
