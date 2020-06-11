import {Component, OnInit, ViewChild} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {Debt} from '../model/debt';
import {SelectionModel} from '@angular/cdk/collections';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from '@angular/material/sort';
import {DebtService} from '../service/debt.service';
import {MatDialog} from '@angular/material/dialog';
import {AddDebtDialogComponent} from '../add-debt-dialog/add-debt-dialog.component';

@Component({
  selector: 'app-debt',
  templateUrl: './debt.component.html',
  styleUrls: ['./debt.component.css'],
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
export class DebtComponent implements OnInit {
  debts: Debt[] = [];

  displayedColumns: string[] = ['company', 'amount', 'dueDate', 'details'];
  selection = new SelectionModel<Debt>(true, []);
  dataSource = new MatTableDataSource(this.debts);
  @ViewChild(MatSort, {static: true}) sort: MatSort;


  deleteSelected = false;

  constructor(private debtService: DebtService, public dialog: MatDialog) {

  }

  getTotalCost() {
    return this.debts.map(t => t.amount).reduce((acc, value) => acc + value, 0);
  }

  ngOnInit(): void {
    this.debtService.retrieveDebts().subscribe(value => {

      if (value != null) {
        this.debts = value;
      }
      if (this.debts.length != 0) {
        this.dataSource.data = [...this.debts];
        this.dataSource.sort = this.sort;
      }
    });
  }

  openEditDebtDialog(debt: any): void {
    const dialogRef = this.dialog.open(AddDebtDialogComponent, {
      width: '300px',
      height: '365px',
      data: debt
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        const foundDebt = this.debts.find(value => value.id === debt.id);
        const number = this.debts.indexOf(foundDebt);
        this.debts.splice(number, 1);
        this.debts.push(result);
        this.dataSource.data = [...this.debts];
      }
    }, error => {
      console.log('error!' + error);
    });
  }

  openAddDebtDialog(debt: any): void {
    const dialogRef = this.dialog.open(AddDebtDialogComponent, {
      width: '300px',
      height: '450px',
      data: debt
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        if (this.debts != null) {
          this.debts.push(result);
          this.dataSource.data = [...this.debts];
        }
      }
    }, error => {
      console.log('error!' + error);
    });
  }

  toggleDeleteDebt() {
    this.deleteSelected = !this.deleteSelected;
    if (this.displayedColumns.includes('select')) {
    } else {
      this.displayedColumns.unshift('select');
    }

  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.debts.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.debts.forEach(row => this.selection.select(row));
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Debt): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.company + 1}`;
  }

  deleteDebts() {
    console.log(this.selection.selected);
    this.selection.selected.forEach(value => {
      this.debtService.deleteDebt(value.id).subscribe(() => {
        const index = this.debts.indexOf(value);
        console.log(index);
        this.debts.splice(index, 1);
        this.dataSource.data = [...this.debts];
        this.toggleDeleteDebt();
      }, error => {
        console.log('Couldn\'t delete item' + error);
      });
    });
  }

  removeColumn() {
    if (this.displayedColumns.includes('select') && !this.deleteSelected) {
      this.selection.clear();
      this.displayedColumns.splice(0, 1);
    }
  }
}
