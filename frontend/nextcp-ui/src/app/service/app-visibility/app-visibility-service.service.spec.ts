import { TestBed } from '@angular/core/testing';

import { AppVisibilityServiceService } from './app-visibility-service.service';

describe('AppVisibilityServiceService', () => {
  let service: AppVisibilityServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppVisibilityServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
