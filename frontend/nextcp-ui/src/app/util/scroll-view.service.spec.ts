import { TestBed } from '@angular/core/testing';

import { ScrollViewService } from './scroll-view.service';

describe('ScrollViewService', () => {
  let service: ScrollViewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScrollViewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
