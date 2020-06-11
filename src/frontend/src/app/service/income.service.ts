import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Income} from '../model/income';

@Injectable({
  providedIn: 'root'
})
export class IncomeService {

  constructor(private http: HttpClient) { }

  retrieveIncome() {
    return this.http.get<any>(`${environment.API_URL}/api/v1/income/userincome`);
  }

  postIncome(body: Income) {
    return this.http.post(`${environment.API_URL}/api/v1/income/userincome`, body, {responseType: 'text'});
  }


  deleteIncome(id: number) {
    return this.http.delete(`${environment.API_URL}/api/v1/income/userincome/${id}`, {responseType: 'text'});
  }

  putIncome(id: number, body: any) {
    return this.http.put(`${environment.API_URL}/api/v1/income/userincome/${id}`, body, {responseType: 'text'});
  }

}
