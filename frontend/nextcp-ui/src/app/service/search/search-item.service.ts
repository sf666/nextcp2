import { MusicItemDto } from './../dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchItemService {

  musicItem: MusicItemDto;

  constructor() { }

}
