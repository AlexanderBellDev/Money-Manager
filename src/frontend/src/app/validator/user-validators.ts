import {AbstractControl, AsyncValidatorFn} from "@angular/forms";
import {environment} from "../../environments/environment";
import {map, switchMap} from "rxjs/operators";
import {Observable, timer} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class UserValidators {

  constructor(private http: HttpClient) {
  }

  searchUsername(text) {
    text = text.toLowerCase();
    // debounce
    return timer(200)
      .pipe(
        switchMap(() => {
          // Check if username is available
          return this.http.get<any>(`${environment.API_URL}/api/v1/auth/checkusername/${text}`)
        })
      );
  }

  usernameValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any } | null> => {
      return this.searchUsername(control.value)
        .pipe(
          map(res => {
            // if username is already taken
            if (res.length) {
              // return error
              return { 'usernameExists': true};
            }
          })
        );
    };
  }

  searchUserEmail(text) {
    text = text.toLowerCase();
    // debounce
    return timer(200)
      .pipe(
        switchMap(() => {
          // Check if username is available
          return this.http.get<any>(`${environment.API_URL}/api/v1/auth/checkemail/${text}`)
        })
      );
  }

  emailValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any } | null> => {
      return this.searchUserEmail(control.value)
        .pipe(
          map(res => {
            // if username is already taken
            if (res.length) {
              // return error
              return { 'emailExists': true};
            }
          })
        );
    };
  }




  static MatchPassword(AC: AbstractControl) {
    let password = AC.get('password').value;
    if(AC.get('confirmPassword').touched || AC.get('confirmPassword').dirty) {
      let verifyPassword = AC.get('confirmPassword').value;

      if(password != verifyPassword) {
        AC.get('confirmPassword').setErrors( {MatchPassword: true} )
      } else {
        return null
      }
    }
  }
}
