import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private router: Router, private message: NzMessageService) { }

  ngOnInit(): void {
  }

  public clearStorage(): void {
    localStorage.clear();
    this.router.navigateByUrl('');
  }
}
