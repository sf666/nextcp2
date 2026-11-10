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

  it('uses Remote Playback on Apple engines without the legacy picker', () => {
    Reflect.deleteProperty(audio, 'webkitShowPlaybackTargetPicker');
    const prompt = vi.fn().mockResolvedValue(undefined);
    Object.defineProperty(audio, 'remote', { value: { prompt }, configurable: true });
    expect(service.airPlaySupported).toBe(true);
    expect(service.showAirPlayPicker()).toBe(true);
    expect(prompt).toHaveBeenCalledOnce();
    expect(service.airPlaySelected()).toBe(true);
  });

  it('restores the selection after the standard picker is cancelled', async () => {
    Reflect.deleteProperty(audio, 'webkitShowPlaybackTargetPicker');
    Object.defineProperty(audio, 'remote', {
      value: { prompt: vi.fn().mockRejectedValue(new DOMException('Cancelled', 'AbortError')) },
      configurable: true,
    });
    service.showAirPlayPicker();
    await Promise.resolve();
    expect(service.airPlaySelected()).toBe(false);
  });

  it('does not label a generic Cast-capable browser as AirPlay', () => {
    Reflect.deleteProperty(audio, 'webkitShowPlaybackTargetPicker');
    Reflect.deleteProperty(audio, 'webkitCurrentPlaybackTargetIsWireless');
    vi.spyOn(navigator, 'vendor', 'get').mockReturnValue('Google Inc.');
    Object.defineProperty(audio, 'remote', { value: { prompt: vi.fn() }, configurable: true });
    expect(service.airPlaySupported).toBe(false);
    expect(service.showAirPlayPicker()).toBe(false);
  });
});
