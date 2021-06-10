import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public recoverPassword(body): Observable<any> {
    return this.http.put(`${this.baseUrl}auth/password-recovery`, body, {responseType: 'text'});
  }

  public forgotPassword(body: any) {
    console.log("EVO ME");
    console.log(body);
    console.log(this.baseUrl);
    return this.http.post(`${this.baseUrl}auth/forgot-password`, body, {responseType: 'text'});
  }
}
