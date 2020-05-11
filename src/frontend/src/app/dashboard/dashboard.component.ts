import { Component, OnInit } from '@angular/core';
import {Debt} from "../model/debt";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  debts: Debt[] = [];

  displayedColumns: string[] = ['description', 'amount'];

  constructor() {
    this.debts.push(new Debt('Paypal', 200))
  }

  ngOnInit(): void {
  }

}
