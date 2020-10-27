import { TestBed } from '@angular/core/testing';

import { AvtransportService } from './avtransport.service';

describe('AvtransportService', () => {
  let service: AvtransportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AvtransportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
