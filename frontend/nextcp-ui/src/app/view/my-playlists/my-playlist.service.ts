import { Subject } from 'rxjs';
import { Injectable, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MyPlaylistService {

  private activePlaylistId_: string;
  public activePlaylistId$: Subject<string> = new Subject();

  constructor(

  ) {
    this.activePlaylistId_ = localStorage.getItem('lastMyPlaylistId');
  }

  ngOnInit(): void {

  }

  selectPlaylist(id: string) {
    this.activePlaylistId_ = id;
    this.activePlaylistId$.next(id);
    localStorage.setItem('lastMyPlaylistId', id.toString());
  }

  get activePlaylistId() {
    return this.activePlaylistId_;
  }
}
