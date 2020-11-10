import { TestBed } from '@angular/core/testing';

import { DtoGeneratorService } from './dto-generator.service';

describe('DtoGeneratorService', () => {
  let service: DtoGeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DtoGeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
