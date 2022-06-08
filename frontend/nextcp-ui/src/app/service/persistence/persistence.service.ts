import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor() {}

  public clearLastMediaServerDevice() : void {
    localStorage.setItem('lastMediaServerDevice', '');
  }

  public setCurrentObjectID(oid : string) : void {
    localStorage.setItem('lastMediaServerPath', oid);    
  }

  public setNewMediaServerDevice(udn: string): void {
    localStorage.setItem('lastMediaServerDevice', udn);
  }

  public getCurrentMediaServerDevice(): string {
    return localStorage.getItem('lastMediaServerDevice');
  }

  public getLastMediaServerPath(): string {
    return localStorage.getItem('lastMediaServerPath');
  }

  public isCurrentMediaServer(udn: string) : boolean {
    const lastServerUdn = localStorage.getItem('lastMediaServerDevice');
    return lastServerUdn === udn;
  }
}
