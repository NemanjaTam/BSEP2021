import { Component, OnInit, SecurityContext } from '@angular/core';
import { EndUserCertificateService } from './../../../services/end-user-certificate.service';
import * as moment from 'moment';
import { CertificateService } from './../../../services/certificate.service';
import 'rxjs' ;
import {DomSanitizer} from '@angular/platform-browser';
import { NzMessageService } from 'ng-zorro-antd';
import { Router } from '@angular/router';
declare var require: any
const FileSaver = require('file-saver');

@Component({
  selector: 'app-end-user-certificates',
  templateUrl: './end-user-certificates.component.html',
  styleUrls: ['./end-user-certificates.component.css']
})
export class EndUserCertificatesComponent implements OnInit {
  public listOfData = [];

  constructor(private router: Router, private message: NzMessageService, private endUserCertificateService: EndUserCertificateService, private certificateService: CertificateService, private sanitizer:DomSanitizer) { }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void {
    this.endUserCertificateService.getAllValidEndUserCertificates().subscribe(data => {
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
      this.message.info('Successfully downoladed!');
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
