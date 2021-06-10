import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-registration-confirm',
  templateUrl: './registration-confirm.component.html',
  styleUrls: ['./registration-confirm.component.css']
})
export class RegistrationConfirmComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private message: NzMessageService,
              private router: Router) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
        const id = params['id'];
        this.userService.confirmAccount(id).subscribe(() => {
            this.message.success("Successfully confirmed!");
        }, error => {
            this.message.error(error.error.message);
        });
    });
  }

  public login(): void {
    this.router.navigateByUrl('login');
  }
}
