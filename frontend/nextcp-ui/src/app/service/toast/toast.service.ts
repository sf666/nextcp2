import { ToastrService } from 'ngx-toastr';
import { ToastMessage } from './toast.service.d';
import { Injectable, TemplateRef } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ToastService {
  constructor(private toastr: ToastrService) {}

  info(message, header : string) {
    this.toastr.info(message, header);
  }
  
  error(message, header : string) {
    this.toastr.error(message, header);
  }
  
  success(message, header : string) {
    this.toastr.success(message, header);
  }
}