import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthServices} from '../services/auth-services';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {


  constructor(protected authService:AuthServices, private router:Router) {

  }

  handleLogout() {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }
}
