import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {


  public setBackgroundImageMainScreen(url: string): void {
    if (Modernizr.backdropfilter && document.getElementById('main-screen')) {
      document.getElementById('main-screen').style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setFooterBackgroundImage(url: string): void {
    if (Modernizr.backdropfilter && document.getElementById('footer-background')) {
      document.getElementById('footer-background').style.backgroundImage = 'url("' + url + '")';
    }
  }

  public setDisplayContainerHeaderImage(url: string): void {
    if (Modernizr.backdropfilter && document.getElementById('header-background')) {
      document.getElementById('header-background').style.backgroundImage = 'url("' + url + '")';
    }
  }
}

