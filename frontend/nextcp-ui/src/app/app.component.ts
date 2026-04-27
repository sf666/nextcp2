import { SpinnerService } from './service/spinner.service';
import { LayoutService } from './service/layout.service';
import { ChangeDetectionStrategy, Component, HostListener, inject, OnInit } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { FooterComponent } from './mediarenderer/footer/footer.component';
import { SidebarComponent } from './view/sidebar/sidebar.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import { AppVisibilityService } from './service/app-visibility/app-visibility-service.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: true,
    imports: [RouterOutlet, MatProgressSpinner, SidebarComponent, FooterComponent]
})
export class AppComponent implements OnInit {
  title = 'nextCP/2';
  // AppVisibilityService instance is injected to track app visibility and trigger route reloads when the app becomes visible again
  readonly visibilityService = inject(AppVisibilityService);
  
  ngOnInit(): void {
    initFlowbite();
  }

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer, public layoutService: LayoutService, public spinnerService: SpinnerService) {
    // SVG icon set was removed. This is for future documentation, how to register icon sets
    //
    // Globally register SVG mat-icon 
    // iconRegistry.addSvgIconSet(sanitizer.bypassSecurityTrustResourceUrl('assets/icon-set.svg'));
  }

  public showBlur(): boolean {
    if (Modernizr.backdropfilter) {
      return true;
    } else {
      return false;
    }
  }

  get headerVisibleClass() : string {
    if (this.layoutService.headerVisible) {
      return "navVisible";
    } else {
      return "navHidden";
    }
  }
}
