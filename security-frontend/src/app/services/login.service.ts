import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public login(body): Observable<any> {
    return this.http.post(`${this.baseUrl}auth/login`, body);
  }
}
