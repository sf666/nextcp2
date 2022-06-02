import { CustomHttpInterceptor } from './service/http-interceptor';
import { MinimTagComponent } from './mediaserver/popup/minim-tag/minim-tag.component';
import { SidebarComponent } from './view/sidebar/sidebar.component';
import { RendererDropdownComponent } from './mediarenderer/dropdown/dropdown.component';
import { MediaServerComponent } from './mediaserver/mediaServer/mediaServer.component';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ServerDropdownComponent } from './mediaserver/dropdown/dropdown.component';
import { MediarendererComponent } from './mediarenderer/mediarenderer/mediarenderer.component';
import { DeviceViewComponent } from './view/device-view/device-view.component';
import { NavBarComponent } from './view/nav-bar/nav-bar.component';
import { MusicLibraryComponent } from './view/music-library/music-library.component';
import { FooterComponent } from './mediarenderer/footer/footer.component';
import { SettingsComponent } from './view/settings/settings.component';
import { RadioComponent } from './view/radio/radio.component';
import { PlaylistComponent } from './view/playlist/playlist.component';
import { ModalSearchResultComponent } from './view/search/modal-search-result/modal-search-result.component';


import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// Material Design
import { MatSliderModule } from '@angular/material/slider';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatDialogModule } from '@angular/material/dialog';
import { SearchResultItemMultiComponent } from './view/search/search-result-item-multi/search-result-item-multi.component';
import { SearchResultItemSingleComponent } from './view/search/search-result-item-single/search-result-item-single.component';
import { DomChangedDirective } from './directive/watch-dom-tree.directive';
import { StarRatingComponent } from './view/star-rating/star-rating.component';
import { DefautPlaylistsComponent } from './mediaserver/popup/defaut-playlists/defaut-playlists.component';
import { SongOptionsComponent } from './mediaserver/popup/song-options/song-options/song-options.component';
import { QualityBadgeComponent } from './util/comp/quality-badge/quality-badge.component';
import { AvailableRendererComponent } from './popup/available-renderer/available-renderer.component';
import { VolumeControlComponent } from './popup/volume-control/volume-control.component';
import { AvailableServerComponent } from './popup/available-server/available-server.component';
import { MyAlbumComponent } from './view/my-album/my-album.component';
import { MySongsComponent } from './view/my-songs/my-songs.component';
import { ToastContainerComponent } from './service/toast/toast-container/toast-container.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DisplayContainerComponent } from './mediaserver/display-container/display-container.component'

@NgModule({
   declarations: [
      AppComponent,
      MediaServerComponent,
      MediarendererComponent,
      RendererDropdownComponent,
      ServerDropdownComponent,
      MediarendererComponent,
      DeviceViewComponent,
      NavBarComponent,
      MusicLibraryComponent,
      FooterComponent,
      SettingsComponent,
      RadioComponent,
      PlaylistComponent,
      SidebarComponent,
      ModalSearchResultComponent,
      SearchResultItemMultiComponent,
      SearchResultItemSingleComponent,
      DomChangedDirective,
      MinimTagComponent,
      StarRatingComponent,
      DefautPlaylistsComponent,
      SongOptionsComponent,
      QualityBadgeComponent,
      AvailableRendererComponent,
      VolumeControlComponent,
      AvailableServerComponent,
      MyAlbumComponent,
      MySongsComponent,
      ToastContainerComponent,
      DisplayContainerComponent,
   ],
   imports: [
      // Material Design
      MatSliderModule,
      MatIconModule,
      MatProgressBarModule,
      MatFormFieldModule,
      MatSelectModule,
      MatButtonModule,
      MatInputModule,
      MatCardModule,
      TextFieldModule,
      MatSlideToggleModule,
      MatToolbarModule,
      MatListModule,
      MatSidenavModule,
      MatDialogModule,
      MatProgressSpinnerModule,

      // Other
      BrowserModule,
      BrowserAnimationsModule,
      AppRoutingModule,
      FormsModule,
      HttpClientModule,
      NgbModule
   ],
   schemas: [
      CUSTOM_ELEMENTS_SCHEMA
   ],
   providers: [{ 
      provide: LocationStrategy, useClass: HashLocationStrategy 
   }, {
      provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptor, multi: true
   }],

   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
