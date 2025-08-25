import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {AuthServices} from '../services/auth-services';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnInit{

  formLoginGroup! : FormGroup ;
  errorMessage!:string;
  constructor(private  fb: FormBuilder, private authService: AuthServices, private  router: Router) {

  }

  ngOnInit(): void {
    this.formLoginGroup = this.fb.group({
      username:this.fb.control(''),
      password:this.fb.control('')
    })
    }

  handleLogin() {
    let username = this.formLoginGroup.value.username;
    let password = this.formLoginGroup.value.password;
    this.authService.login(username,password).subscribe({
      next:data => {
        this.authService.loadProfile(data);
        this.router.navigateByUrl("/admin")
      },
      error:err => {
        this.errorMessage = err.error;
        console.log(err);
      }
    })
  }
}
