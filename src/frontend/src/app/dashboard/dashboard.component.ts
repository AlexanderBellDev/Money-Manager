import {Component, OnInit} from '@angular/core';
import {Debt} from "../model/debt";
import {DebtService} from "../service/debt.service";
import {MatDialog} from "@angular/material/dialog";
import {AddDebtDialogComponent} from "../add-debt-dialog/add-debt-dialog.component";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  debts: Debt[] = [];

  displayedColumns: string[] = ['select', 'company', 'amount', 'dueDate', 'details', 'update'];
  selection = new SelectionModel<Debt>(true, []);

  ngOnInit(): void {
    this.debtService.retrieveDebts().subscribe(value => {
      this.debts = value
    })
  }

  getTotalCost() {
    return this.debts.map(t => t.amount).reduce((acc, value) => acc + value, 0);
  }

  deleteSelected: boolean = false;

  constructor(private debtService: DebtService, public dialog: MatDialog) {
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddDebtDialogComponent, {
      width: '300px',
      height: '320px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  toggleDeleteDebt() {
    this.deleteSelected = !this.deleteSelected;
    // if(this.displayedColumns.includes('select')){
    //   this.displayedColumns.splice(0,1);
    // }else{
    //   this.displayedColumns.unshift('select')
    // }

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
    console.log(this.selection.selected)
  }
}

