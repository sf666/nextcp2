/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { MediaServerService } from './mediaServer.service';

describe('Service: MediaServer', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MediaServerService]
    });
  });

  it('should ...', inject([MediaServerService], (service: MediaServerService) => {
    expect(service).toBeTruthy();
  }));
});
