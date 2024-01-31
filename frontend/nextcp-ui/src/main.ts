import { enableProdMode, importProvidersFrom } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';


import { environment } from './environments/environment';
import { AppComponent } from './app/app.component';
import { ToastrModule } from 'ngx-toastr';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app/app-routing.module';
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

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
        // Material Design
        MatSliderModule, MatIconModule, MatProgressBarModule, MatFormFieldModule, MatSelectModule, MatButtonModule, MatInputModule, MatCardModule, TextFieldModule, MatSlideToggleModule, MatToolbarModule, MatListModule, MatSidenavModule, MatDialogModule, MatProgressSpinnerModule, 
        // Other
        BrowserModule, AppRoutingModule, FormsModule, ReactiveFormsModule, NgbModule, ToastrModule.forRoot({
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
        provideHttpClient(withInterceptorsFromDi())
    ]
})
  .catch(err => console.error(err));
