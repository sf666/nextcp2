import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {


  public setBackgroundImageMainScreen(url: string): void {
    const element = document.getElementById('main-screen');
    if (Modernizr.backdropfilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setFooterBackgroundImage(url: string): void {
    const element = document.getElementById('footer-background');
    if (Modernizr.backdropfilter && element) {
      element.style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setDisplayContainerHeaderImage(url: string): void {
    if (Modernizr.backdropfilter && document.getElementById('header-background')) {
      let element = document.getElementById('header-background');
      if (element) {
        element.style.backgroundImage = 'url("' + url + '")';
      }
    }
  }
}

