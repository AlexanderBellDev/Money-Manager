export class Income {

  incomeSource: string;
  incomeAmount: number;
  paymentDate: string;
  id: number;
  recurringIncome: boolean
  durationOfRecurrence: number


  constructor(incomeSource: string, incomeAmount: number, paymentDate: string, recurringIncome: boolean,
              durationOfRecurrence: number) {
    this.incomeSource = incomeSource;
    this.incomeAmount = incomeAmount;
    this.paymentDate = paymentDate;
    this.recurringIncome = recurringIncome;
    this.durationOfRecurrence = durationOfRecurrence;
  }
}
