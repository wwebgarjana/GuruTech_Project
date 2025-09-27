


import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar {
  mobileMenuOpen = false;
  dropdownOpen = false;

  toggleMobileMenu() {
    this.mobileMenuOpen = !this.mobileMenuOpen;
    if (!this.mobileMenuOpen) {
      this.dropdownOpen = false; }
  }

  toggleDropdown(event: Event) {
    event.stopPropagation(); 
    this.dropdownOpen = !this.dropdownOpen;
  }
}
