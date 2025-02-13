import { TestBed } from '@angular/core/testing';

import { RadioNetworksService } from './radio-networks.service';

describe('RadioNetworksService', () => {
  let service: RadioNetworksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RadioNetworksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
