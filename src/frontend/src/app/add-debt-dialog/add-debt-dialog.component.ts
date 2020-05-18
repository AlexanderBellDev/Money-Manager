import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, Validators} from "@angular/forms";
import {DebtService} from "../service/debt.service";

@Component({
  selector: 'app-add-debt-dialog',
  templateUrl: './add-debt-dialog.component.html',
  styleUrls: ['./add-debt-dialog.component.css']
})
export class AddDebtDialogComponent implements OnInit {
  debtForm = this.formBuilder.group({
    id: ['', [Validators.required]],
    amount: ['', [Validators.required]],
    company: ['', [Validators.required]],
    dueDate: ['', [Validators.required]]
  });

  constructor(
    private debtService: DebtService,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<AddDebtDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
  }

  saveDebt() {
    this.debtService.postDebt(this.debtForm.value).subscribe(() => {
      this.dialogRef.close(this.debtForm.value);
    }, error => {
      console.log(error)
    })
  }

  resetDebtForm() {
    this.debtForm.reset();
  }
}
