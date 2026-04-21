import { Subject } from 'rxjs';
import { Injectable, signal } from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';

@Injectable({
  providedIn: 'root'
})
export class MyPlaylistService {

  private activePlaylistId_ = signal<string>('');
  public activePlaylistId$: Subject<string> = new Subject();

  constructor(
    private layoutService: LayoutService
  ) {
    this.activePlaylistId_.set(localStorage.getItem('lastMyPlaylistId') ?? '');
  }

  selectPlaylist(id: string) {
    this.activePlaylistId_.set(id);
    this.activePlaylistId$.next(id);
    localStorage.setItem('lastMyPlaylistId', id.toString());
  }

  get activePlaylistId() {
    return this.activePlaylistId_();
  }
}
