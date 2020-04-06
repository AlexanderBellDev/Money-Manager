import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";
import {FormBuilder, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {RegisterService} from "../service/register.service";
import {LoginService} from "../service/login.service";
import {JwtToken} from "../model/jwt-token";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User;
  jwtToken: JwtToken;
  usererror = false;
  constructor(private formBuilder: FormBuilder, private http:HttpClient, private route:Router, private registerService: RegisterService,
              private loginService: LoginService) { }
  loginForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });
  ngOnInit(
  ) {
    if(this.registerService.username){
      this.loginForm.patchValue({
        username: this.registerService.username
      });
    }
  }


  onSubmit() {
    this.user = this.loginForm.value;
    console.log(this.loginForm);
    this.loginService.checkUser(this.user).subscribe(data  => {
        this.jwtToken = data;

        sessionStorage.setItem('TOKEN', 'Bearer ' + data.accessToken);
        sessionStorage.setItem('authenticatedUser', this.user.username);
        this.route.navigate(['dashboard']);
      },
      error  => {
        console.log("Error", error);
        this.usererror = true;
      })
  }

}
