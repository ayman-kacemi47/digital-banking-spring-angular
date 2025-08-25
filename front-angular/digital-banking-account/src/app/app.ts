import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Navbar} from './navbar/navbar';
import {HttpClient} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {AuthServices} from './services/auth-services';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar,ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{

  constructor(private authService:AuthServices) {

  }

  ngOnInit(): void {
     this.authService.loadJwtFromLocalStorage();
  }
  protected title = 'digital-banking-account';
}
