import { Component, OnInit, SecurityContext } from '@angular/core';
import { CertificateService } from 'src/app/services/certificate.service';
import * as moment from 'moment';
import { EndUserCertificateService } from './../../../services/end-user-certificate.service';
import { DomSanitizer } from '@angular/platform-browser';
import { NzMessageService } from 'ng-zorro-antd';
import { Router } from '@angular/router';
declare var require: any
const FileSaver = require('file-saver');

@Component({
  selector: 'app-ca-certificates',
  templateUrl: './ca-certificates.component.html',
  styleUrls: ['./ca-certificates.component.css']
})
export class CACertificatesComponent implements OnInit {

  public listOfData = [];

  constructor(private certificateService: CertificateService, private message: NzMessageService, private router: Router, 
              private endUserCertificateService: EndUserCertificateService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void {
    this.certificateService.getAllValidCACertificates().subscribe(data => {
      this.listOfData = data;
    }, error => {
      this.message.info(error.error.message);
      this.router.navigateByUrl('dashboard');
    });
  }

  public revoke(email): void {
    const body = {
      "email" : email
    }
    this.endUserCertificateService.revokeCertificate(body).subscribe(() => {
      this.message.info('Successfully revoked!');
      this.setupData();
      });

  }

  public async download(email): Promise<any> {
    const body = {
      "email" : email
    }
    let name:String = '';
    await this.certificateService.getFileName(body).subscribe(data => {
      name = data[0];
    })
    this.certificateService.downloadCertificate(body).subscribe(data => {
      this.downloadFile(data,name);
      this.message.info('Successfully downloaded!');
    })
  }

  public downloadFile(data: any, name:String) {
    const blob = new Blob([data], { type: 'application/octet-stream' });

   let fileUrl:string = this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob)));
   FileSaver.saveAs(fileUrl, name);
  }

  public nameAndSurname(name, surname): String {
    return name + " " + surname;
  }

  public formatDate(date): String {
    return moment(date).format('YYYY/MM/DD');
  }

}
