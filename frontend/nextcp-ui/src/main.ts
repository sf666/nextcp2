import { enableProdMode, importProvidersFrom } from '@angular/core';
import { environment } from './environments/environment';
import { AppComponent } from './app/app.component';
import { ToastrModule } from 'ngx-toastr';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideAnimations } from '@angular/platform-browser/animations';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { CustomHttpInterceptor } from './app/service/http-interceptor';
import { HTTP_INTERCEPTORS, withInterceptorsFromDi, provideHttpClient } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { provideClientHydration, BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { Routes, provideRouter, withComponentInputBinding, withRouterConfig } from '@angular/router';
import { MusicLibraryComponent } from './app/view/music-library/music-library.component';
import { MediarendererComponent } from './app/mediarenderer/mediarenderer/mediarenderer.component';
import { SettingsComponent } from './app/view/settings/settings.component';
import { MyAlbumComponent } from './app/view/my-album/my-album.component';
import { MySongsComponent } from './app/view/my-songs/my-songs.component';
import { MyPlaylistsComponent } from './app/view/my-playlists/my-playlists.component';
import { RadioComponent } from './app/view/radio/radio.component';
import { PlaylistComponent } from './app/view/playlist/playlist.component';


const ROUTES: Routes = [
    { path: 'music-library', component: MusicLibraryComponent },
    { path: 'player', component: MediarendererComponent },
    { path: 'settings', component: SettingsComponent },
    { path: 'myAlbums', component: MyAlbumComponent },
    { path: 'myTracks', component: MySongsComponent },
    { path: 'myPlaylists', component: MyPlaylistsComponent },
    { path: 'radio', component: RadioComponent },
    { path: 'playlist', component: PlaylistComponent },
    { path: '', redirectTo: '/music-library', pathMatch: 'full' },
  ];

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(ROUTES, withComponentInputBinding(), withRouterConfig({
            onSameUrlNavigation: "reload"            
          })
        ),
        importProvidersFrom(
        // Material Design
        MatSliderModule, MatIconModule, MatProgressBarModule, MatFormFieldModule, MatSelectModule, MatButtonModule, MatInputModule, MatCardModule, TextFieldModule, MatSlideToggleModule, MatToolbarModule, MatListModule, MatSidenavModule, MatDialogModule, MatProgressSpinnerModule, 
        // Other
        BrowserModule, FormsModule, ReactiveFormsModule, NgbModule, ToastrModule.forRoot({
            timeOut: 3000,
            countDuplicates: true,
            maxOpened: 10,
            preventDuplicates: true,
        })),
        provideClientHydration(),
        {
            provide: LocationStrategy, useClass: HashLocationStrategy
        }, {
            provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true
        },
        provideAnimations(),
        provideHttpClient(withInterceptorsFromDi()),
    ]
})
  .catch(err => console.error(err));

