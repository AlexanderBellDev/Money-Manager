import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Debt} from "../model/debt";

@Injectable({
  providedIn: 'root'
})
export class DebtService {
  constructor(private http: HttpClient) {
  }

  retrieveDebts() {
    return this.http.get<any>(`${environment.API_URL}/api/v1/debt/userdebt`)
  }

  postDebt(body: Debt) {
    return this.http.post(`${environment.API_URL}/api/v1/debt/userdebt`, body, {responseType: 'text'})
  }
}
