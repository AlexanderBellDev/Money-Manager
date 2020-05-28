import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {UserService} from "../service/user.service";
import {User} from "../model/user";

@Component({
  selector: 'app-editprofile',
  templateUrl: './editprofile.component.html',
  styleUrls: ['./editprofile.component.css']
})
export class EditprofileComponent implements OnInit {
  userDetails: User;

  userForm = this.formBuilder.group({
    id: [''],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    username: ['', [Validators.required]],
    password: ['', Validators.required]
  })

  constructor(private formBuilder: FormBuilder, private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUserDetails().subscribe(value => {
      if (value != null) {
        this.userForm.patchValue({
          id: value.id,
          firstName: value.firstName,
          lastName: value.surname,
          username: value.username
        });
      }
    }, error => {
      console.log(error)
    })
  }

}
