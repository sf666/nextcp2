import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {

  constructor() { }

  public setBackgroundImageMainScreen(url: string) : void {
    document.getElementById('main-screen').style.backgroundImage = 'url("' + url + '")';
  }

  public setFooterBackgroundImage(url: string) : void {
    document.getElementById('footer-background').style.backgroundImage = 'url("' + url + '")';
  }  
}

