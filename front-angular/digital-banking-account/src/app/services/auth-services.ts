import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {jwtDecode} from 'jwt-decode';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthServices {
  isAuthenticated:boolean = false;
  roles:any;
  username:any;
  accessToken!:string;

  constructor(private http:HttpClient, private  router: Router) {

  }

  public login(username:string, password:string){
    let options={
      headers:new HttpHeaders().set("Content-type","application/x-www-form-urlencoded"),
    }
    let params = new HttpParams()
      .set("username",username).set("password",password)
  return this.http.post(environment.apiUrl+"/auth/login",params, options,);
  }

  loadProfile(data: any) {
    //console.log('loaaaadd ---------------')
    this.isAuthenticated = true;
    this.accessToken = data["access_token"];
    let decodedJwt:any = jwtDecode(this.accessToken);
    this.username = decodedJwt.sub;
    this.roles= decodedJwt.scope;
    localStorage.setItem('access_token', this.accessToken);
    //console.log("after load profile ", this.accessToken)
  }

  logout() {
      this.isAuthenticated = false;
    this.roles= undefined;
    this.username=undefined;
    this.accessToken= '';

    localStorage.removeItem("access_token");
  }

  loadJwtFromLocalStorage() {
    let token = localStorage.getItem("access_token");
    if(token){
      this.loadProfile({access_token:token});
      this.router.navigateByUrl("/admin/customers")
    }
  }
}
