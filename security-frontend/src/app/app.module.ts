import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { IconDefinition } from '@ant-design/icons-angular';
import { NzIconModule, NZ_ICON_DEFAULT_TWOTONE_COLOR, NZ_ICONS } from 'ng-zorro-antd/icon';
import * as AllIcons from '@ant-design/icons-angular/icons';

import { AppComponent } from './app.component';
import { NgZorroAntdModule, NZ_I18N, en_US } from 'ng-zorro-antd';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { CreateRequestComponent } from './pages/create-request/create-request.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { CACertificatesComponent } from './pages/lists/ca-certificates/ca-certificates.component';
import { EndUserCertificatesComponent } from './pages/lists/end-user-certificates/end-user-certificates.component';
import { RevokedCertificatesComponent } from './pages/lists/revoked-certificates/revoked-certificates.component';
import { CertificateRequestComponent } from './pages/lists/certificate-request/certificate-request.component';
import { ApproveRequestComponent } from './pages/approve-request/approve-request.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { UserRegistrationComponent } from './pages/auth/registration/user-registration/user-registration.component';
import { UserRequestsComponent } from './pages/auth/registration/user-requests/user-requests.component';
import { RegistrationConfirmComponent } from './pages/auth/registration/registration-confirm/registration-confirm.component';
import { PasswordRecoveryComponent } from './pages/password-recovery/password-recovery.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';

registerLocaleData(en);

const antDesignIcons = AllIcons as {
  [key: string]: IconDefinition;
};
const icons: IconDefinition[] = Object.keys(antDesignIcons).map(key => antDesignIcons[key])


@NgModule({
  declarations: [
    AppComponent,
    CreateRequestComponent,
    DashboardComponent,
    LoginComponent,
    CACertificatesComponent,
    EndUserCertificatesComponent,
    RevokedCertificatesComponent,
    CertificateRequestComponent,
    ApproveRequestComponent,
    UserRegistrationComponent,
    UserRequestsComponent,
    RegistrationConfirmComponent,
    PasswordRecoveryComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    NgZorroAntdModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    NzIconModule
  ],
  providers: [{ provide: NZ_I18N, useValue: en_US }, { provide: NZ_ICON_DEFAULT_TWOTONE_COLOR, useValue: '#00ff00' },
  { provide: NZ_ICONS, useValue: icons }, { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
