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
  title = 'nextcp-ui';

  opened: boolean;
  events: string[] = [];

  showFiller = false;

  private debouceResize = _.debounce(this.resiseVh, 500);

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    // Globally register SVG mat-icon 
    iconRegistry.addSvgIconSet(sanitizer.bypassSecurityTrustResourceUrl('assets/icon-set.svg'));
  }

  public showBlur() {
    return true;
  }

  // Mobile Devices URL bar / view height fix.
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.debouceResize();
  }

  private resiseVh() {
    const vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
}

}
