import { Subject } from 'rxjs';
import { Injectable, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MyPlaylistService {

  private activePlaylistId_: number;
  public activePlaylistId$: Subject<number> = new Subject();

  constructor(

  ) {
    this.activePlaylistId_ = parseInt(localStorage.getItem('lastMyPlaylistId'));
  }

  ngOnInit(): void {

  }

  selectPlaylist(id: number) {
    this.activePlaylistId_ = id;
    this.activePlaylistId$.next(id);
    localStorage.setItem('lastMyPlaylistId', id.toString());
  }

  get activePlaylistId() {
    return this.activePlaylistId_;
  }
}
