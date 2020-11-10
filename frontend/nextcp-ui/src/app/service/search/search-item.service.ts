import { MusicItemDto, QuickSearchResultDto } from './../dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchItemService {

  _searchQuery: string;

  private _musicItem: MusicItemDto;
  private _musicItemList: QuickSearchResultDto;

  constructor() { }

  get musicItem(): MusicItemDto {
    return this._musicItem;
  }

  get musicItemList(): QuickSearchResultDto {
    return this._musicItemList;
  }

  set musicItem(mi : MusicItemDto) {
    this.clear();
    this._musicItem = mi;
  }

  set musicItemList(mil : QuickSearchResultDto) {
    this.clear();
    this._musicItemList = mil;
  }

  private clear() {
    this._searchQuery = "";
    this._musicItem = null;
    this._musicItemList = null;
  }

}
