import { Component } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'nextcp-ui';
  
  opened: boolean;
  events: string[] = [];

  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    // Globally register SVG mat-icon 
    iconRegistry.addSvgIconSet(sanitizer.bypassSecurityTrustResourceUrl('assets/icon-set.svg'));
  }

}
