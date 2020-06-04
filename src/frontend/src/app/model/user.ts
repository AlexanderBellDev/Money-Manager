export class User {
  constructor(username: string, firstName: string, surname: string, email: string, password: string, confirmPassword: string, address1: string, address2: string, city: string, postalCode: string) {
    this.username = username;
    this.firstName = firstName;
    this.surname = surname;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.postalCode = postalCode;
  }

  id: number;
  username: string;
  firstName: string;
  surname: string;
  email: string;
  password: string;
  confirmPassword: string;
  address1: string;
  address2: string;
  city: string;
  postalCode: string
}
