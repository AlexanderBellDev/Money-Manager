export class Income {

  company: string;
  amount: number;
  paymentDate: string;
  id: number;


  constructor(company: string, amount: number, paymentDate: string) {
    this.company = company;
    this.amount = amount;
    this.paymentDate = paymentDate;
  }
}
