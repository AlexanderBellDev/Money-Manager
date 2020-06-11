import {Component, OnInit, ViewChild} from '@angular/core';
import {Debt} from '../model/debt';
import {DebtService} from '../service/debt.service';
import {MatDialog} from '@angular/material/dialog';
import {AddDebtDialogComponent} from '../add-debt-dialog/add-debt-dialog.component';
import {SelectionModel} from '@angular/cdk/collections';
import {animate, style, transition, trigger} from '@angular/animations';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  animations: [
    trigger('easeInOut', [
      transition(':enter', [
        style({
          opacity: 0
        }),
        animate('0.5s ease-in-out', style({
          opacity: 1
        }))
      ]),
      transition(':leave', [
        style({
          opacity: 1
        }),
        animate('0.5s ease-in-out', style({
          opacity: 0
        }))
      ])
    ])
  ]
})
export class DashboardComponent implements OnInit {


  constructor() {

  }


  ngOnInit(): void {
  }


}
