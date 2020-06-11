import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Injectable} from '@angular/core';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {


  constructor(private router: Router) {
  }

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {

    const idToken = sessionStorage.getItem('TOKEN');

    if (idToken) {
      req = req.clone({
        headers: req.headers.set('Authorization', idToken)
      });
      return next.handle(req).pipe(
        catchError(
          (err) => {
            if (err.status === 401){
              this.handleAuthError();
              return of(err);
            }
            throw err;
          }
        )
      );
    }
    else {
      return next.handle(req);
    }


  }
  private handleAuthError() {
    sessionStorage.removeItem('TOKEN');
    sessionStorage.removeItem('authenticatedUser');
    this.router.navigateByUrl('login');
  }
}
