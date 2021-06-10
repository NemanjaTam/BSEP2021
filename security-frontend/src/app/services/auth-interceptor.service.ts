  
import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    var user = JSON.parse(localStorage.getItem('user'));
    if(user == null) {
      return next.handle(request);
    }
    
    const modifiedRequest = request.clone({
      headers: request.headers.append(
        "Auth-Token", user.token
      )
    });
    return next.handle(modifiedRequest);
  }
}
