import { LayoutService } from './service/layout.service';
import { Component, HostListener } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import * as _ from "lodash";
import { debounce } from 'lodash';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'nextCP/2';

  opened: boolean;
  events: string[] = [];

  showFiller = false;

  private throttleResize = _.throttle(this.resiseVh, 100);

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer, public layoutService: LayoutService) {
    // Globally register SVG mat-icon 
    iconRegistry.addSvgIconSet(sanitizer.bypassSecurityTrustResourceUrl('assets/icon-set.svg'));
  }

  public showBlur() {
    return true;
  }

  // Mobile Devices URL bar / view height fix.
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    // this.throttleResize();
    this.resiseVh();
  }

  private resiseVh() {
    const vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }

}
