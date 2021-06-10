import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd';
import { CertificateRequestService } from 'src/app/services/certificate-request.service';

@Component({
  selector: 'app-create-request',
  templateUrl: './create-request.component.html',
  styleUrls: ['./create-request.component.css']
})
export class CreateRequestComponent implements OnInit {
  public validateForm: FormGroup;
  public certificateType: any = null;
  public extension: any = null;
  public isVisible: boolean = false;
  public isRoot: boolean = false;

  constructor(private message: NzMessageService, private crService: CertificateRequestService, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.setupForm();
  }

  public setupForm(): void {
    this.validateForm = this.fb.group({
      firstName: [ null, [Validators.required]],
      lastName: [ null, [Validators.required]],
      email: [ null, [Validators.required, Validators.email]],
      country: [ null, [Validators.required]],
      organisation: [ null, [Validators.required]],
      organisationUnit: [ null, [Validators.required]]
    });
  }

  public create(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }

    {
      const body = {
        ...this.validateForm.value,
        extension: this.extension,
        certificateAuthority: this.isVisible,
        rootCert: this.isRoot
      }
      this.crService.createRequest(body).subscribe(data => {
        this.message.info('You have successfully created a certification request!');
        this.setupForm();
        this.extension = null;
        this.certificateType = null;
      },
      error => {
        this.message.info(error.error.message);
      });
    }
  }

  public setupVisibility(): void {
    if(this.certificateType == "certificationAuthority"){
      this.isVisible = true;
      this.isRoot = false;
    }else if(this.certificateType == "endUser") {
        this.isVisible = false;
        this.isRoot = false;
    } else if(this.certificateType == "rootCA") {
        this.isRoot = true;
        this.isVisible = true;
    }
  }

  public login(): void {
    this.router.navigateByUrl('login');
  }
}
