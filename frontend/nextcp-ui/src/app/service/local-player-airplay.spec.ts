import { TestBed } from '@angular/core/testing';
import { LocalPlayerService } from './local-player.service';
import { ConfigurationService } from './configuration.service';
import { PersistenceService } from './persistence/persistence.service';
import { ToastService } from './toast/toast.service';
import { WebStreamNowPlayingService } from './web-stream-now-playing.service';

describe('Browser player AirPlay selection', () => {
  let audio: HTMLAudioElement & {
    webkitShowPlaybackTargetPicker: ReturnType<typeof vi.fn>;
    webkitCurrentPlaybackTargetIsWireless: boolean;
  };
  let service: LocalPlayerService;

  beforeEach(() => {
    audio = Object.assign(document.createElement('audio'), {
      webkitShowPlaybackTargetPicker: vi.fn(),
      webkitCurrentPlaybackTargetIsWireless: false,
    });
    vi.stubGlobal('Audio', class { constructor() { return audio; } });
    vi.spyOn(Storage.prototype, 'getItem').mockReturnValue(null);
    vi.spyOn(window, 'addEventListener').mockImplementation(() => {});
    TestBed.configureTestingModule({ providers: [
      { provide: ConfigurationService, useValue: {} },
      { provide: PersistenceService, useValue: {} },
      { provide: ToastService, useValue: {} },
      { provide: WebStreamNowPlayingService, useValue: {} },
    ] });
    service = TestBed.inject(LocalPlayerService);
  });

  afterEach(() => {
    TestBed.resetTestingModule();
    vi.restoreAllMocks();
    vi.unstubAllGlobals();
  });

  it('keeps AirPlay selected when iOS plays audio without reporting a remote route', () => {
    expect(service.showAirPlayPicker()).toBe(true);
    audio.dispatchEvent(new Event('playing'));
    expect(service.airPlaySelected()).toBe(true);
    expect(service.airPlayActive()).toBe(false);
  });

  it('opens the same native picker for returning to this device', () => {
    service.showAirPlayPicker();
    service.showAirPlayPicker('device');
    expect(audio.webkitShowPlaybackTargetPicker).toHaveBeenCalledTimes(2);
    expect(service.airPlaySelected()).toBe(false);
  });

  it('also follows actual route changes when the browser reports them', () => {
    audio.webkitCurrentPlaybackTargetIsWireless = true;
    audio.dispatchEvent(new Event('webkitcurrentplaybacktargetiswirelesschanged'));
    expect(service.airPlaySelected()).toBe(true);
    audio.webkitCurrentPlaybackTargetIsWireless = false;
    audio.dispatchEvent(new Event('webkitcurrentplaybacktargetiswirelesschanged'));
    expect(service.airPlaySelected()).toBe(false);
  });

  it('preserves the previous selection when the picker cannot open', () => {
    service.showAirPlayPicker();
    audio.webkitShowPlaybackTargetPicker.mockImplementation(() => { throw new Error('unavailable'); });
    vi.spyOn(console, 'warn').mockImplementation(() => {});
    expect(service.showAirPlayPicker('device')).toBe(false);
    expect(service.airPlaySelected()).toBe(true);
  });
});
