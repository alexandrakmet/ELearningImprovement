import { NgModule } from '@angular/core';
import { Routes, RouterModule, CanActivate } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { PassRecoveryComponent } from './pass-recovery/pass-recovery.component';
import { MailConfirmComponent } from './registration/mail-confirm/mail-confirm.component';
import { PassRecoveryConfirmComponent } from './pass-recovery/pass-recovery-confirm/pass-recovery-confirm.component';

const authenticationRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'pass-recovery', component: PassRecoveryComponent },
  { path: 'registration/:token', component: MailConfirmComponent },
  { path: 'pass-recovery/:token', component: PassRecoveryConfirmComponent }
];

@NgModule({
  imports: [
    RouterModule.forChild(authenticationRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class AuthRoutingModule { }
