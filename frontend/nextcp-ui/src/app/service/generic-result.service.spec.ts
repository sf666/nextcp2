import { TestBed } from '@angular/core/testing';

import { GenericResultService } from './generic-result.service';

describe('GenericResultService', () => {
  let service: GenericResultService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenericResultService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
