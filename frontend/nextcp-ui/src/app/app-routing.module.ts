import { InputOutputSourceComponent } from './view/input-output-source/input-output-source.component';
import { SearchResultItemMultiComponent } from './view/search/search-result-item-multi/search-result-item-multi.component';
import { SearchResultItemSingleComponent } from './view/search/search-result-item-single/search-result-item-single.component';
import { PlaylistComponent } from './view/playlist/playlist.component';
import { RadioComponent } from './view/radio/radio.component';
import { SettingsComponent } from './view/settings/settings.component';
import { MediarendererComponent } from './mediarenderer/mediarenderer/mediarenderer.component';
import { MusicLibraryComponent } from './view/music-library/music-library.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  { path: 'music-library', component: MusicLibraryComponent },
  { path: 'player', component: MediarendererComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'radio', component: RadioComponent },
  { path: 'playlist', component: PlaylistComponent },  
  { path: 'input-output', component: InputOutputSourceComponent },    
  { path: 'searchResultSingleItem', component: SearchResultItemSingleComponent },  
  { path: 'searchResultContainer', component: SearchResultItemMultiComponent },  
  { path: '', redirectTo: '/music-library', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }


/*

const routes: Routes = [
  { path: 'settings', component: SettingsComponent },
  { path: 'logging', component: LoggingComponent },
  { path: 'filesystem', component: FilesystemComponent },
  { path: 'app-main-screen', component: MainScreenComponent },
  { path: 'app-playlist-editor', component: PlaylistEditorComponent },
  { path: '', redirectTo: '/app-main-screen', pathMatch: 'full' },
];

*/