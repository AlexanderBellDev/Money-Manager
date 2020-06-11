import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {JwtToken} from '../model/jwt-token';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  checkUser(user){
    return this.http.post<JwtToken>(`${environment.API_URL}/api/v1/auth/login`, user);
  }

  logout() {
    sessionStorage.removeItem('TOKEN');
    sessionStorage.removeItem('authenticatedUser');
  }

  isUserLoggedIn() {
    const authUser = sessionStorage.getItem('authenticatedUser');
    const token = sessionStorage.getItem('TOKEN');
    return !(authUser === null && token === null);
  }
}
