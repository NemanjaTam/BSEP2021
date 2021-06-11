import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  public validateForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private messageService: NzMessageService) { }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
    });
  }

  public submitForm() {
    this.authService.forgotPassword(this.validateForm.value).subscribe(data => {
      this.messageService.info(data);
    }, error => {
      this.messageService.error(error.error);
    });
  }

}
