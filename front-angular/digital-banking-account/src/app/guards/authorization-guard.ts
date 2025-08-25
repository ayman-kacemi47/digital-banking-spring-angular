import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthServices} from '../services/auth-services';

export const authorizationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthServices);
  const router = inject(Router);
  if(authService.roles.includes("ADMIN")){
    return true;
  }else {
  router.navigateByUrl("/admin/not-authorized")
  return false;
  }
};
