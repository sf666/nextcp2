import { TestBed } from '@angular/core/testing';

import { SearchItemService } from './search-item.service';

describe('SearchItemService', () => {
  let service: SearchItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchItemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
