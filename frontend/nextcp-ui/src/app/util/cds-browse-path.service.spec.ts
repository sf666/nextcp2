import { TestBed } from '@angular/core/testing';

import { CdsBrowsePathService } from './cds-browse-path.service';

describe('CdsBrowsePathService', () => {
  let service: CdsBrowsePathService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CdsBrowsePathService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
