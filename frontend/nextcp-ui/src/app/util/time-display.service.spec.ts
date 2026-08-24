import { TestBed } from '@angular/core/testing';

import { TimeDisplayService } from './time-display.service';

describe('TimeDisplayService', () => {
  let service: TimeDisplayService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeDisplayService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('convertLongToDateStringShort', () => {
    it('formats below an hour as mm:ss', () => {
      expect(service.convertLongToDateStringShort(0)).toBe('00:00');
      expect(service.convertLongToDateStringShort(9)).toBe('00:09');
      expect(service.convertLongToDateStringShort(204)).toBe('03:24');
      expect(service.convertLongToDateStringShort(3599)).toBe('59:59');
    });

    it('formats an hour and beyond as hh:mm:ss', () => {
      expect(service.convertLongToDateStringShort(3600)).toBe('01:00:00');
      expect(service.convertLongToDateStringShort(3725)).toBe('01:02:05');
    });

    it('truncates fractional seconds instead of rounding up', () => {
      // The local player reports a fractional currentTime; 59.9s must not display as 01:00.
      expect(service.convertLongToDateStringShort(59.9)).toBe('00:59');
    });

    it('falls back to 00:00 for values that cannot be displayed', () => {
      // A stream of unknown length reports Infinity, and NaN shows up before metadata is loaded.
      expect(service.convertLongToDateStringShort(Infinity)).toBe('00:00');
      expect(service.convertLongToDateStringShort(NaN)).toBe('00:00');
      expect(service.convertLongToDateStringShort(-5)).toBe('00:00');
    });
  });
});
