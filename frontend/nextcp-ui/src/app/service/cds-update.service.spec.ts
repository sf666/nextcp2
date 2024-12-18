import { TestBed } from '@angular/core/testing';

import { CdsUpdateService } from './cds-update.service';

describe('CdsUpdateService', () => {
  let service: CdsUpdateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CdsUpdateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
