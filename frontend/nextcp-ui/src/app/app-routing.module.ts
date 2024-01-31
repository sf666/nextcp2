import { SearchResultComponent } from './view/search-result/search-result.component';
import { MyPlaylistsComponent } from './view/my-playlists/my-playlists.component';
import { MyAlbumComponent } from './view/my-album/my-album.component';
import { MySongsComponent } from './view/my-songs/my-songs.component';
import { PlaylistComponent } from './view/playlist/playlist.component';
import { RadioComponent } from './view/radio/radio.component';
import { SettingsComponent } from './view/settings/settings.component';
import { MediarendererComponent } from './mediarenderer/mediarenderer/mediarenderer.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MusicLibrary2Component } from './view/music-library2/music-library2.component';
import { MusicLibraryComponent } from './view/music-library/music-library.component';


const routes: Routes = [
  { path: 'music-library', component: MusicLibrary2Component },
  { path: 'music-library-search-item', component: MusicLibraryComponent },
  { path: 'player', component: MediarendererComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'myAlbums', component: MyAlbumComponent },
  { path: 'myTracks', component: MySongsComponent },
  { path: 'myPlaylists', component: MyPlaylistsComponent },
  { path: 'radio', component: RadioComponent },
  { path: 'playlist', component: PlaylistComponent },
  { path: 'searchResult', component: SearchResultComponent },
  { path: '', redirectTo: '/music-library', pathMatch: 'full' },
];

// SearchResultComponent

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
