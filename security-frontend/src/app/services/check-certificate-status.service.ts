import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CheckCertificateStatusService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public checkStatus(serialNumber: number): Observable<any> {
    return this.http.get(`${this.baseUrl}ocsp/`+ serialNumber +`/check`);
  }
}
