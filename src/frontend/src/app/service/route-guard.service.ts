import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from './login.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService {

  constructor(
    private router: Router, private loginService: LoginService) {

  }

  canActivate() {

    if (this.loginService.isUserLoggedIn()){
      return true;
    }

    this.router.navigate(['login']);

    return false;
  }
}
