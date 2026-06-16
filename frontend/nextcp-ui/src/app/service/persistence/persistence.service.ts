import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor() {}

  // Media Server Device
  public clearLastMediaServerDevice() : void {
    localStorage.setItem('lastMediaServerDevice', '');
  }

  public setNewMediaServerDevice(udn: string): void {
    localStorage.setItem('lastMediaServerDevice', udn);
  }

  public getCurrentMediaServerDevice(): string | null{
    return localStorage.getItem('lastMediaServerDevice');
  }

  public isCurrentMediaServer(udn: string) : boolean {
    const lastServerUdn = localStorage.getItem('lastMediaServerDevice');
    return lastServerUdn === udn;
  }

  // Media Renderer Device
  public clearLastMediaRendererDevice() : void {
    localStorage.setItem('lastMediaRendererDevice', '');
  }

  public setNewMediaRendererDevice(udn: string): void {
    localStorage.setItem('lastMediaRendererDevice', udn);
  }

  public getCurrentMediaRendererDevice(): string | null {
    return localStorage.getItem('lastMediaRendererDevice');
  }

  public isCurrentMediaRenderer(udn: string) : boolean {
    const lastRendererUdn = localStorage.getItem('lastMediaRendererDevice');
    return lastRendererUdn === udn;
  }

  // Last object ID reload last navigated page
  public setCurrentObjectID(oid : string) : void {
    localStorage.setItem('lastMediaServerPath', oid);    
  }

  public getLastObjectId(): string | null{
    return localStorage.getItem('lastMediaServerPath');
  }

  // Last object ID for the AudioAddict ("Radio Networks") view.
  // Kept separate from the media library path so the two views do not interfere on reload.
  public setLastAudioAddictObjectId(oid : string) : void {
    localStorage.setItem('lastAudioAddictPath', oid);
  }

  public getLastAudioAddictObjectId(): string | null {
    return localStorage.getItem('lastAudioAddictPath');
  }

  // Last focused item
  public setLastFocusID(oid : string) : void {
    localStorage.setItem('lastFocusId', oid);    
  }

  public getLastFocusId(): string | null {
    return localStorage.getItem('lastFocusId');
  }

}
