import { SidebarComponent } from './view/sidebar/sidebar.component';
import { RendererDropdownComponent } from './mediarenderer/dropdown/dropdown.component';
import { MediaServerComponent } from './mediaserver/mediaServer/mediaServer.component';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { ToastrModule } from 'ngx-toastr';
import { ServerDropdownComponent } from './mediaserver/dropdown/dropdown.component';
import { MediarendererComponent } from './mediarenderer/mediarenderer/mediarenderer.component';
import { BrowseResultComponent } from './mediaserver/browse-result/browse-result.component';
import { ContainerComponent } from './mediaserver/browse-result/container/container.component';
import { ItemComponent } from './mediaserver/browse-result/item/item.component';
import { AlbumComponent } from './mediaserver/browse-result/album/album.component';
import { DeviceViewComponent } from './view/device-view/device-view.component';
import { NavBarComponent } from './view/nav-bar/nav-bar.component';
import { MusicLibraryComponent } from './view/music-library/music-library.component';
import { FooterComponent } from './mediarenderer/footer/footer.component';
import { SettingsComponent } from './view/settings/settings.component';
import { RadioComponent } from './view/radio/radio.component';
import { PlaylistComponent } from './view/playlist/playlist.component';
import { ModalSearchResultComponent } from './view/search/modal-search-result/modal-search-result.component';

import { HashLocationStrategy, LocationStrategy } from '@angular/common';

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
import { SearchComponent } from './view/search/search.component';
import { MatDialogModule } from '@angular/material/dialog';
import { SearchResultItemMultiComponent } from './view/search/search-result-item-multi/search-result-item-multi.component';
import { SearchResultItemSingleComponent } from './view/search/search-result-item-single/search-result-item-single.component';
import { InputOutputSourceComponent } from './view/input-output-source/input-output-source.component';


@NgModule({
   declarations: [
      AppComponent,
      MediaServerComponent,
      MediarendererComponent,
      RendererDropdownComponent,
      ServerDropdownComponent,
      MediarendererComponent,
      BrowseResultComponent,
      ContainerComponent,
      ItemComponent,
      AlbumComponent,
      DeviceViewComponent,
      NavBarComponent,
      MusicLibraryComponent,
      FooterComponent,
      SettingsComponent,
      RadioComponent,
      PlaylistComponent,
      SidebarComponent,
      SearchComponent,
      ModalSearchResultComponent,
      SearchResultItemMultiComponent,
      SearchResultItemSingleComponent,
      InputOutputSourceComponent,

   ],
   imports: [
      // Toastr
      CommonModule,
      BrowserAnimationsModule,
      ToastrModule.forRoot(),

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

      // Other
      BrowserModule,
      AppRoutingModule,
      FormsModule,
      HttpClientModule,
      NgbModule
   ],
   schemas: [
      CUSTOM_ELEMENTS_SCHEMA
   ],
   providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
