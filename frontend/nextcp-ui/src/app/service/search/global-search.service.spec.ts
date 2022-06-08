import { TestBed } from '@angular/core/testing';

import { GlobalSearchService } from './global-search.service';

describe('GlobalSearchService', () => {
  let service: GlobalSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlobalSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
