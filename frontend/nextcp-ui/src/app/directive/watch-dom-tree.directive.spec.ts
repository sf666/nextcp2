import { ElementRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { DomChangedDirective } from './watch-dom-tree.directive';

describe('DomChangedDirective', () => {
  it('should create an instance', () => {
    TestBed.configureTestingModule({
      providers: [
        { provide: ElementRef, useValue: new ElementRef(document.createElement('div')) },
      ],
    });
    const directive = TestBed.runInInjectionContext(() => new DomChangedDirective());
    expect(directive).toBeTruthy();
  });
});
