import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NzMessageService, NzTreeHigherOrderServiceToken } from 'ng-zorro-antd';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html',
  styleUrls: ['./password-recovery.component.css']
})
export class PasswordRecoveryComponent implements OnInit {

  public validateForm: FormGroup;
  private user = '';

  constructor(private router: Router, private fb: FormBuilder, private authService: AuthService, private route: ActivatedRoute, private messageService: NzMessageService) { }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      password: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')]],
      repeatedPassword: ['', [Validators.required, Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$'), this.checkMatchingPasswords]]
    });
    this.user = this.route.snapshot.paramMap.get('token');
  }

  public checkMatchingPasswords = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validateForm.controls.password.value) {
      return { confirm: true, error: true };
    }
    return {};
  }

  public submitForm() {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
    let body = {
      ...this.validateForm.value,
      user: this.user
    }

    this.authService.recoverPassword(body).subscribe(data => {
      this.messageService.info(`${data}. You will be redirected to the login page in a few seconds`);
      this.validateForm.controls.password.setValue('');
      this.validateForm.controls.repeatedPassword.setValue('');
      setTimeout(() => {
        this.router.navigateByUrl('login');
      }, 2000);
    }, error => {
      this.messageService.error(error.error);
    });
  }
}
