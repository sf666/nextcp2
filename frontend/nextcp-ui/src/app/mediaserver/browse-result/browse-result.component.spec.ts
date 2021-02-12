import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BrowseResultComponent } from './browse-result.component';

describe('BrowseResultComponent', () => {
  let component: BrowseResultComponent;
  let fixture: ComponentFixture<BrowseResultComponent>;

  beforeEach(waitForAsync(() => {
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
