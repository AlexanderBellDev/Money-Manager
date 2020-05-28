import {Component, OnInit, ViewChild} from '@angular/core';
import {animate, style, transition, trigger} from "@angular/animations";
import {Debt} from "../model/debt";
import {SelectionModel} from "@angular/cdk/collections";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatDialog} from "@angular/material/dialog";
import {Income} from "../model/income";
import {IncomeService} from "../service/income.service";
import {AddDebtDialogComponent} from "../add-debt-dialog/add-debt-dialog.component";
import {AddIncomeDialogComponent} from "../add-income-dialog/add-income-dialog.component";

@Component({
  selector: 'app-income',
  templateUrl: './income.component.html',
  styleUrls: ['./income.component.css'],
  animations: [
    trigger('easeInOut', [
      transition(':enter', [
        style({
          opacity: 0
        }),
        animate("0.5s ease-in-out", style({
          opacity: 1
        }))
      ]),
      transition(':leave', [
        style({
          opacity: 1
        }),
        animate("0.5s ease-in-out", style({
          opacity: 0
        }))
      ])
    ])
  ]
})
export class IncomeComponent implements OnInit {
  income: Income[] = [];

  displayedColumns: string[] = ['company', 'amount', 'paymentDate', 'details'];
  selection = new SelectionModel<Income>(true, []);
  dataSource = new MatTableDataSource(this.income);
  @ViewChild(MatSort, {static: true}) sort: MatSort;


  deleteSelected: boolean = false;

  constructor(private incomeService: IncomeService, public dialog: MatDialog) {
  }

  getTotalCost() {
    return this.income.map(t => t.amount).reduce((acc, value) => acc + value, 0);
  }

  ngOnInit(): void {
    this.incomeService.retrieveIncome().subscribe(value => {

      if (value != null) {
        this.income = value
      }
      if (this.income.length != 0) {
        this.dataSource.data = [...this.income];
        this.dataSource.sort = this.sort;
      }
    })
  }

  openEditIncomeDialog(income: any): void {
    const dialogRef = this.dialog.open(AddDebtDialogComponent, {
      width: '300px',
      height: '365px',
      data: income
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        let foundDebt = this.income.find(value => value.id === income.id);
        let number = this.income.indexOf(foundDebt);
        this.income.splice(number, 1);
        this.income.push(result);
        this.dataSource.data = [...this.income];
      }
    }, error => {
      console.log('error!' + error)
    });
  }

  openAddIncomeDialog(debt: any): void {
    const dialogRef = this.dialog.open(AddIncomeDialogComponent, {
      width: '300px',
      height: '365px',
      data: debt
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        if (this.income != null) {
          this.income.push(result)
          this.dataSource.data = [...this.income];
        }
      }
    }, error => {
      console.log('error!' + error)
    });
  }

  toggleDeleteIncome() {
    this.deleteSelected = !this.deleteSelected;
    if (this.displayedColumns.includes('select')) {
    } else {
      this.displayedColumns.unshift('select')
    }

  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.income.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.income.forEach(row => this.selection.select(row));
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Income): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.company + 1}`;
  }

  deleteIncome() {
    console.log(this.selection.selected)
    this.selection.selected.forEach(value => {
      this.incomeService.deleteIncome(value.id).subscribe(() => {
        const index = this.income.indexOf(value);
        console.log(index)
        this.income.splice(index, 1);
        this.dataSource.data = [...this.income];
        this.toggleDeleteIncome();
      }, error => {
        console.log('Couldn\'t delete item' + error);
      })
    })
  }

  removeColumn() {
    if (this.displayedColumns.includes('select') && !this.deleteSelected) {
      this.selection.clear();
      this.displayedColumns.splice(0, 1);
    }
  }
}


