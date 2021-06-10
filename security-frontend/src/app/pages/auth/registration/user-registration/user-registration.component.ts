import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  validateForm: FormGroup;
  htmlTagRegExp = '^(?!<.+?>).*$';    // stitim se od <script> tagova

  constructor(private fb: FormBuilder,
              private router: Router,
              private userService: UserService,
              private message: NzMessageService) {
    this.validateForm = this.fb.group({
      email: ['', [Validators.email, Validators.required, Validators.minLength(8), Validators.pattern(this.htmlTagRegExp)]],
      password: ['', [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&.]).{9,}'), Validators.pattern(this.htmlTagRegExp)]],
      confirm: ['', [Validators.required, this.confirmValidator, Validators.pattern(this.htmlTagRegExp)]],
      firstName: ['', [Validators.required, Validators.minLength(3), Validators.pattern(this.htmlTagRegExp)]],
      lastName: ['', [Validators.required, Validators.minLength(3), Validators.pattern(this.htmlTagRegExp)]],
    });
  }

  ngOnInit(): void {
  }

  submitForm(value: { email: string; password: string; confirm: string; firstName: string; lastName: string; address: string; ssn: string; securityAnswer: string }): void {
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }

    const registrationRequest = {
      username: value.email,
      password: value.password,
      firstName: value.firstName,
      lastName: value.lastName
    }
    
    this.userService.registration(registrationRequest).subscribe(data => {
      this.message.success(data.message);
    },
    error => {
      this.message.error(error.error.message);
    });

    this.resetForm(new MouseEvent('click'));
  }

  resetForm(e: MouseEvent): void {
    e.preventDefault();
    this.validateForm.reset();
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsPristine();
      this.validateForm.controls[key].updateValueAndValidity();
    }
  }

  validateConfirmPassword(): void {
    setTimeout(() => this.validateForm.controls.confirm.updateValueAndValidity());
  }

  confirmValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { error: true, required: true };
    } else if (control.value !== this.validateForm.controls.password.value) {
      return { confirm: true, error: true };
    }
    return {};
  };

  onLogin(): void {
    this.router.navigateByUrl('login');
  }

  onPasswordChange(passwordInput): void {
    // console.log("password change: ", passwordInput);
  }

  onPasswordConfirmChange(passwordInput): void {
    // console.log("password confirm change: ", passwordInput);
  }
}