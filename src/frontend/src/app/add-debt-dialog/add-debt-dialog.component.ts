import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-add-debt-dialog',
  templateUrl: './add-debt-dialog.component.html',
  styleUrls: ['./add-debt-dialog.component.css']
})
export class AddDebtDialogComponent implements OnInit {
  debtForm = this.formBuilder.group({
    id: ['', [Validators.required]],
    owedAmount: ['', [Validators.required]],
    companyName: ['', [Validators.required]],
    owedByDate: ['', [Validators.required]],
    itemDetail: ['', []],
    indexNum: ['', [Validators.required]],
  });

  constructor(
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

  }

  resetDebtForm() {
    this.debtForm.reset();
  }
}
