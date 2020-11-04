import { TestBed } from '@angular/core/testing';

import { SearchContainerService } from './search-container.service';

describe('SearchContainerService', () => {
  let service: SearchContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SearchContainerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
