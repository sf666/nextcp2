import { SpinnerService } from './service/spinner.service';
import { LayoutService } from './service/layout.service';
import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FooterComponent } from './mediarenderer/footer/footer.component';
import { SidebarComponent } from './view/sidebar/sidebar.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import { AppVisibilityService } from './service/app-visibility/app-visibility-service.service';
import { supportsBackdropFilter } from './util/browser-capabilities';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [
    RouterOutlet,
    MatProgressSpinner,
    SidebarComponent,
    FooterComponent,
  ],
})
export class AppComponent implements OnInit {
  layoutService = inject(LayoutService);
  spinnerService = inject(SpinnerService);

  title = 'nextCP/2';
  // AppVisibilityService instance is injected to track app visibility and trigger route reloads when the app becomes visible again
  readonly visibilityService = inject(AppVisibilityService);

  ngOnInit(): void {
    initFlowbite();
  }

  public showBlur(): boolean {
    return supportsBackdropFilter;
  }

  get headerVisibleClass(): string {
    if (this.layoutService.headerVisible()) {
      return 'navVisible';
    } else {
      return 'navHidden';
    }
  }
}
