import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public registration(body): Observable<any> {
    return this.http.post(`${this.baseUrl}auth/registration`, body);
  }

  public getUserRegistrationRequests(): Observable<any> {
    return this.http.get(`${this.baseUrl}users/PENDING`);
  }

  public approve(id): Observable<any> {
    return this.http.get(`${this.baseUrl}users/${id}/approve`);
  }

  public deny(id): Observable<any> {
    return this.http.get(`${this.baseUrl}users/${id}/deny`);
  }

  public confirmAccount(id): Observable<any> {
    return this.http.get(`${this.baseUrl}users/${id}/confirm`);
  }
}
