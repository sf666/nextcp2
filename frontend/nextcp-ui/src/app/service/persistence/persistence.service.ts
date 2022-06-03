import { CdsBrowsePathService } from './../../util/cds-browse-path.service';
import { DeviceService } from 'src/app/service/device.service';
import { LastVisitedPath } from './lastVisitedPath.d';
import { Injectable } from '@angular/core';
import { MediaServerDto } from '../dto';

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor(private cdsBrowsePathService: CdsBrowsePathService) {}

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
