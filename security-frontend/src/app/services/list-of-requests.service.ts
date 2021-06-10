import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ListOfRequestsService {

  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public getAllRequests(): Observable<any> {
    return this.http.get(`${this.baseUrl}certificate-requests`);
  }

  public denyRequest(body): Observable<any> {
    return this.http.post(`${this.baseUrl}certificate-requests/deny`, body);
  }
}
