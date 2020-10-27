import { TestBed } from '@angular/core/testing';

import { ContentDirectoryService } from './content-directory.service';

describe('ContentDirectoryService', () => {
  let service: ContentDirectoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContentDirectoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
