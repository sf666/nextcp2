import { Component } from '@angular/core';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent {
  
  navbarOpen = false;

  constructor() { }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

}

