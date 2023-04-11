import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(private authService: AuthService, private router: Router){};

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) : boolean {
    const isLoggedIn = this.authService.isAuthenticated();
    
    if (next.routeConfig && next.routeConfig.path == 'login' && isLoggedIn) {
      this.router.navigate(['/list']);
      return false;
    }

    if (next.routeConfig && next.routeConfig.path != 'login' && !isLoggedIn) {
      this.router.navigate(['/login']);
      return false;
    }

    return true
  }  

}
