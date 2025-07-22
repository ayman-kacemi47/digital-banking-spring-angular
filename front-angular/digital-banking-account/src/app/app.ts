import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Navbar} from './navbar/navbar';
import {HttpClient} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar,ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'digital-banking-account';
}
