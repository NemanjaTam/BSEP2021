import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { RevokedCertificateService} from './../../../services/revoked-certificate.service';
import { NzMessageService } from 'ng-zorro-antd';
import { CheckCertificateStatusService } from './../../../services/check-certificate-status.service';

@Component({
  selector: 'app-revoked-certificates',
  templateUrl: './revoked-certificates.component.html',
  styleUrls: ['./revoked-certificates.component.css']
})
export class RevokedCertificatesComponent implements OnInit {

  public listOfData = [];
  inputValue: string | null;
  textValue: string | null;

  constructor(private revokedCertificateService: RevokedCertificateService,
              private message: NzMessageService,
              private checkService: CheckCertificateStatusService) { }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void {
    this.revokedCertificateService.getAllRevokeCertificates().subscribe(data => {
      this.listOfData = data;
    }, error => {
      this.message.info(error.error.message);
      // this.router.navigateByUrl('dashboard');
    });
  }

  onCheckClick() {
    this.checkService.checkStatus(+this.inputValue).subscribe(data => {
      this.message.info('Certificate status: ' + data);
    },
    error => {
      this.message.info(error.error.message);
    });
  }

}
