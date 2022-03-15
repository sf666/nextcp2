import { ToastService } from './../toast.service';
import { Component, TemplateRef } from '@angular/core';

@Component({
  selector: 'app-toasts',
  templateUrl: './toast-container.component.html',
  host: {'class': 'toast-container position-fixed top-0 end-0 p-3', 'style': 'z-index: 1200'},
  styleUrls: ['./toast-container.component.scss']
})
export class ToastContainerComponent {

  constructor(public toastService: ToastService) { }

  isTemplate(toast) { return toast.textOrTpl instanceof TemplateRef; }
}
