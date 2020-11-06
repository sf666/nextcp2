import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BackgroundImageService {

  constructor() { }

  public setBackgroundImageMainScreen(url: string) : void {
    document.getElementById('browse-result-main').style.backgroundImage = 'url("' + url + '")';
  }
  
}

