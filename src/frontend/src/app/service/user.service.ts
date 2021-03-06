import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUserDetails() {
    return this.http.get<User>(`${environment.API_URL}/api/v1/user/userdetails`);
  }

  postUserDetails(user: User) {
    return this.http.post(`${environment.API_URL}/api/v1/user/userdetails`, user, {responseType: 'text'});
  }


}
