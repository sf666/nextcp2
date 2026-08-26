import { Injectable, signal, inject } from '@angular/core';
import { LayoutService } from 'src/app/service/layout.service';

@Injectable({
  providedIn: 'root',
})
export class MyPlaylistService {
  private layoutService = inject(LayoutService);

  private activePlaylistId_ = signal<string>('');

  constructor() {
    this.activePlaylistId_.set(localStorage.getItem('lastMyPlaylistId') ?? '');
  }

  selectPlaylist(id: string) {
    this.activePlaylistId_.set(id);
    localStorage.setItem('lastMyPlaylistId', id.toString());
  }

  get activePlaylistId() {
    return this.activePlaylistId_();
  }
}
