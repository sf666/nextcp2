import { Directive, ElementRef, OnInit, inject } from '@angular/core';

@Directive({
  selector: '[domTreeChange]',
  standalone: true,
})
export class DomChangedDirective implements OnInit {
  private elRef = inject(ElementRef);

  ngOnInit() {
    this.registerDomChangedEvent(this.elRef.nativeElement);
  }

  registerDomChangedEvent(el) {
    const observer = new MutationObserver((list) => {
      const evt = new CustomEvent('dom-changed', {
        detail: list,
        bubbles: true,
      });
      el.dispatchEvent(evt);
    });

    const attributes = true;
    const childList = true;
    const subtree = true;
    observer.observe(el, { attributes, childList, subtree });
  }
}
