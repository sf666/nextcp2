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

  public getCurrentMediaServerDevice(): string {
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

  public getCurrentMediaRendererDevice(): string {
    return localStorage.getItem('lastMediaRendererDevice');
  }

  public isCurrentMediaRenderer(udn: string) : boolean {
    const lastRendererUdn = localStorage.getItem('lastMediaRendererDevice');
    return lastRendererUdn === udn;
  }

  // Last object ID
  public setCurrentObjectID(oid : string) : void {
    localStorage.setItem('lastMediaServerPath', oid);    
  }

  public getLastObjectId(): string {
    return localStorage.getItem('lastMediaServerPath');
  }
}
