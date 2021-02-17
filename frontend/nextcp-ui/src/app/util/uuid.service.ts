import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UuidService {

  constructor() { }

  public isValidUuid(uuid : string) {
    return "00000000-0000-0000-0000-000000000000" !== uuid;
  }
}
