import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthServices} from '../services/auth-services';
import {catchError, throwError} from 'rxjs';
import {Router} from '@angular/router';

export const appHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthServices);
  const router = inject(Router);
  const token = authService.accessToken;
  const authToken = token|| localStorage.getItem('access_token');

  //console.log("url: ",req.url, " authToken: ", authToken);
  if(req.url.includes("/auth/login")){
    return next(req);
  }
  const request = req.clone({
    headers:req.headers.set('Authorization','Bearer '+authToken)
  });
  return next(request).pipe(
    catchError(err => {
      if (err.status === 401){
        authService.logout();
        router.navigateByUrl("/login");
      }
      return throwError(()=>err);
    })
  );
};
