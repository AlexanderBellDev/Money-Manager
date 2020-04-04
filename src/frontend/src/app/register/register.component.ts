import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterService} from "../service/register.service";
import {UserValidators} from "../validator/user-validators";
import {User} from "../model/user";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user: User;
  emailValid = false;
  submitted = false;
  constructor(private formBuilder: FormBuilder, private registerService: RegisterService,
              private router: Router, private service:UserValidators) { }



  regForm = this.formBuilder.group({
    // email: ['', [Validators.required, Validators.email],this.service.emailValidator()],
    // username: ['', Validators.required, this.service.usernameValidator()],
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    firstName: ['', [Validators.required]],
    surname: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]]
  }, {validator : [UserValidators.MatchPassword]});

  ngOnInit() {
  }
  // convenience getter for easy access to form fields
  get f() { return this.regForm.controls; }

  passwordValidator(form: FormGroup) {
    const condition = form.get('password').value !== form.get('confirmPassword').value;
    return condition ? { passwordsDoNotMatch: true} : null;
  }


  onSubmit() {
    this.user = this.regForm.value;
    this.user.username = this.user.username.toLowerCase();
    this.user.email = this.user.email.toLowerCase();
    console.log(this.user);
    this.registerService.register(this.user).subscribe(data  => {
        console.log("POST Request is successful ", data);
        this.submitted = true;
        setTimeout(() => {
          this.registerService.username = this.user.username;
          this.router.navigate(['login']);
        }, 1500);  //3s
      },
      error  => {
        console.log("Error", error);
      })

  }

}
