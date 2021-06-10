import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  public getAllValidCACertificates(): Observable<any> {
    return this.http.get(`${this.baseUrl}certificates/ca`);
  }

  public downloadCertificate(body): Observable<Blob> {
    return this.http.post(`${this.baseUrl}certificates/download`, body, {responseType: 'blob'});
  }

  public getFileName(body): Observable<any> {
    return this.http.post(`${this.baseUrl}certificates/file-name`, body);
  }
  
}
