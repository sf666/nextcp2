import { SpinnerService } from './service/spinner.service';
import { LayoutService } from './service/layout.service';
import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import * as _ from "lodash";
import { AsyncPipe } from '@angular/common';
import { FooterComponent } from './mediarenderer/footer/footer.component';
import { SidebarComponent } from './view/sidebar/sidebar.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: true,
    imports: [RouterOutlet, MatProgressSpinner, SidebarComponent, FooterComponent, AsyncPipe]
})
export class AppComponent implements OnInit {
  title = 'nextCP/2';

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
      return "mainContent";
    } else {
      return "mainContentNoHeader";
    }
  }

  /*
  // Mobile Devices URL bar / view height fix.
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    // this.throttleResize();
    // this.resiseVh();
  }
  private resiseVh() {
    const vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }
*/
}
