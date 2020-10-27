import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseResultComponent } from './browse-result.component';

describe('BrowseResultComponent', () => {
  let component: BrowseResultComponent;
  let fixture: ComponentFixture<BrowseResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BrowseResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
