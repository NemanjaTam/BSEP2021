import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EndUserCertificateService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public getAllValidEndUserCertificates(): Observable<any> {
    return this.http.get(`${this.baseUrl}certificates/end-user`);
  }

  public revokeCertificate( mailCertificate ): Observable<any> {
    return this.http.post(`${this.baseUrl}certificates/revoke`, mailCertificate);
  }
}
