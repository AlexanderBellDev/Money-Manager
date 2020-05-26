import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-editprofile',
  templateUrl: './editprofile.component.html',
  styleUrls: ['./editprofile.component.css']
})
export class EditprofileComponent implements OnInit {
  userForm = this.formBuilder.group({
    id: [''],
    firstName: ['Alex', [Validators.required]],
    lastName: ['Test', [Validators.required]],
    username: ['alex1234', [Validators.required]],
    password: ['', Validators.required]
  })

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

  }

}
