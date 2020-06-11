import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, Validators} from '@angular/forms';
import {IncomeService} from '../service/income.service';

@Component({
  selector: 'app-add-income-dialog',
  templateUrl: './add-income-dialog.component.html',
  styleUrls: ['./add-income-dialog.component.css']
})
export class AddIncomeDialogComponent implements OnInit {

  public recurringPayment: boolean;

  incomeForm = this.formBuilder.group({
    id: [''],
    incomeAmount: ['', [Validators.required]],
    incomeSource: ['', [Validators.required]],
    paymentDate: ['', [Validators.required]],
    recurringIncome: [false],
    durationOfRecurrence: ['', [Validators.required]]
  });

  constructor(
    private incomeService: IncomeService,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<AddIncomeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
    if (this.data != null) {
      this.incomeForm.patchValue({
        id: this.data.id,
        incomeAmount: this.data.amount,
        incomeSource: this.data.company,
        paymentDate: this.data.paymentDate,
        recurringIncome: this.data.recurringPayment,
        durationOfRecurrence: this.data.recurringIncome
      });
    }
  }

  saveIncome() {
    if (this.data != null) {
      console.log('putting data');
      this.incomeService.putIncome(this.data.id, this.incomeForm.value).subscribe(() => {
        this.closeDialog();
      });
    }

    this.incomeService.postIncome(this.incomeForm.value).subscribe(() => {
      this.closeDialog();
    }, error => {
      console.log(error);
    });
  }

  resetIncomeForm() {
    this.incomeForm.reset();
  }

  closeDialog() {
    if (this.incomeForm.value != null) {
      this.dialogRef.close(this.incomeForm.value);
    } else {
      this.dialogRef.close();
    }
  }
}
