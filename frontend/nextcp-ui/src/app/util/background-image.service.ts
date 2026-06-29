import { Injectable } from '@angular/core';
import { supportsBackdropFilter } from './browser-capabilities';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {


  public setBackgroundImageMainScreen(url: string): void {
    const element = document.getElementById('main-screen');
    if (supportsBackdropFilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setFooterBackgroundImage(url: string): void {
    const element = document.getElementById('footer-background');
    if (supportsBackdropFilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setDisplayContainerHeaderImage(url: string): void {
    if (supportsBackdropFilter && document.getElementById('header-background')) {
      let element = document.getElementById('header-background');
      if (element) {
        element.style.backgroundImage = 'url("' + url + '")';
      }
    }
  }
}

