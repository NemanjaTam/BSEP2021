import { Component, OnInit } from '@angular/core';
import { ListOfRequestsService } from './../../../services/list-of-requests.service';
import * as moment from 'moment';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';

@Component({
  selector: 'app-certificate-request',
  templateUrl: './certificate-request.component.html',
  styleUrls: ['./certificate-request.component.css']
})
export class CertificateRequestComponent implements OnInit {
  public listOfData = [];

  constructor(private listOfRequestsService: ListOfRequestsService, private router: Router, private message: NzMessageService) { }

  ngOnInit(): void {
    this.setupData();
  }

  private setupData(): void {
    this.listOfRequestsService.getAllRequests().subscribe(data => {
      this.listOfData = data;
    }, error => {
      this.message.info(error.error.message);
      this.router.navigateByUrl('dashboard');
    });
  }

  public approve(data): void {
    this.router.navigateByUrl('dashboard/approve');
    localStorage.setItem('dataToApprove', JSON.stringify(data));
  }

  public deny(id): void {
    const body = {
      "uuid" : id
    }
    this.listOfRequestsService.denyRequest(body).subscribe();
    this.message.info('Successfully denied!');
    this.setupData();
  }

  public nameAndSurname(name, surname): String {
    return name + " " + surname;
  }

  public formatDate(date): String {
    return moment(date).format('YYYY/MM/DD');
  }

}
