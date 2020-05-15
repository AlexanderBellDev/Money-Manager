import {Component, OnInit} from '@angular/core';
import {Debt} from "../model/debt";
import {DebtService} from "../service/debt.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  debts: Debt[] = [];

  displayedColumns: string[] = ['company', 'amount'];

  constructor(private debtService: DebtService) {
  }

  ngOnInit(): void {
    this.debtService.retrieveDebts().subscribe(value => {
      this.debts = value
    })
  }

  getTotalCost() {
    return this.debts.map(t => t.amount).reduce((acc, value) => acc + value, 0);
  }
}
