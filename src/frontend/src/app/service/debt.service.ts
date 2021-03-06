import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Debt} from '../model/debt';

@Injectable({
  providedIn: 'root'
})
export class DebtService {
  constructor(private http: HttpClient) {
  }

  retrieveDebts() {
    return this.http.get<any>(`${environment.API_URL}/api/v1/debt/userdebt`);
  }

  postDebt(body: Debt) {
    return this.http.post(`${environment.API_URL}/api/v1/debt/userdebt`, body, {responseType: 'text'});
  }


  deleteDebt(id: number) {
    return this.http.delete(`${environment.API_URL}/api/v1/debt/userdebt/${id}`, {responseType: 'text'});
  }

  putDebt(id: number, body: any) {
    return this.http.put(`${environment.API_URL}/api/v1/debt/userdebt/${id}`, body, {responseType: 'text'});


  }
}
