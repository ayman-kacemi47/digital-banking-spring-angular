import {CanActivateFn, Router} from '@angular/router';
import {AuthServices} from '../services/auth-services';
import {inject} from '@angular/core';

export const authenticationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthServices);
  const router = inject(Router);
  //console.log(" authenticated ? ",authService.isAuthenticated == true , " url : " ,route.url);
  if (authService.isAuthenticated == true) {
    return true;
  } else {
    router.navigateByUrl('/login');
    return false;
  }

  return true;
};
