import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  validateForm: FormGroup;

  constructor(private message: NzMessageService, private fb: FormBuilder, private router: Router, private loginService: LoginService) { }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      username: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
    this.loginService.login(this.validateForm.value).subscribe(data => {
      const admin = data;
      localStorage.setItem('user', JSON.stringify(admin));
      this.router.navigateByUrl('dashboard');
    },
    error => {
      this.message.info(error.error.message);
    });
  }

  public createRequest(): void{
    this.router.navigateByUrl('create-request');
  }
  
  public createAccount(): void{
    this.router.navigateByUrl('registration');
  }

  public forgotPassword(): void {
    this.router.navigateByUrl('forgot-password');
  }
}
