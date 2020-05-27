export class Debt {

  company: string;
  amount: number;
  dueDate: string;
  id: number;


  constructor(company: string, amount: number, dueDate: string) {
    this.company = company;
    this.amount = amount;
    this.dueDate = dueDate;
  }


}
