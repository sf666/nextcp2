import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchResultItemMultiComponent } from './search-result-item-multi.component';

describe('SearchResultItemMultiComponent', () => {
  let component: SearchResultItemMultiComponent;
  let fixture: ComponentFixture<SearchResultItemMultiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchResultItemMultiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchResultItemMultiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
