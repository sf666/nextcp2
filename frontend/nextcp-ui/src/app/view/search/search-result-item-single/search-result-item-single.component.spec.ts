import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchResultItemSingleComponent } from './search-result-item-single.component';

describe('SearchResultItemSingleComponent', () => {
  let component: SearchResultItemSingleComponent;
  let fixture: ComponentFixture<SearchResultItemSingleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchResultItemSingleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchResultItemSingleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
