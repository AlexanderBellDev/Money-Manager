import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {UserService} from '../service/user.service';
import {User} from '../model/user';
import {Router} from '@angular/router';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from '@angular/material/snack-bar';

@Component({
  selector: 'app-editprofile',
  templateUrl: './editprofile.component.html',
  styleUrls: ['./editprofile.component.css']
})
export class EditprofileComponent implements OnInit {
  userDetails: User;
  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  userForm = this.formBuilder.group({
    id: [''],
    firstName: ['', [Validators.required]],
    surname: ['', [Validators.required]],
    username: ['', [Validators.required]],
    password: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder, private userService: UserService,  private router: Router , private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.userService.getUserDetails().subscribe(value => {
      if (value != null) {
        this.userForm.patchValue({
          id: value.id,
          firstName: value.firstName,
          surname: value.surname,
          username: value.username
        });
      }
    }, error => {
     this.openSnackBar(error);
    });
  }

  saveDetails() {
    this.userService.postUserDetails(this.userForm.value).subscribe(value => {
      this.openSnackBar(value);
    }, error => {
      this.openSnackBar(error);
    });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, '', {
      duration: 500,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

}
