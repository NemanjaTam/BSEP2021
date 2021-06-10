import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-requests',
  templateUrl: './user-requests.component.html',
  styleUrls: ['./user-requests.component.css']
})
export class UserRequestsComponent implements OnInit {

  public listOfData = [];

  constructor(private userService: UserService, 
    private message: NzMessageService, 
    private router: Router) { }

  ngOnInit(): void {
    this.setupUsers();
  }

  private setupUsers(): void {
    this.userService.getUserRegistrationRequests().subscribe(data => {
      this.listOfData = data;
    }, error => {
      this.message.info(error.error.message);
      this.router.navigateByUrl('dashboard');
    });
  }

  public approve(id): void {
    this.userService.approve(id).subscribe(data => {
      this.message.success("Successfully approved.");
    }, error => {
      this.message.info(error.error.message);
    });
    location.reload();
  }

  public deny(id): void {
    this.userService.deny(id).subscribe(data => {
      this.message.success("Successfully denied.");
    }, error => {
      this.message.info(error.error.message);
    });
    location.reload();
  }

}
