import {Component, OnInit} from '@angular/core';

import {AuthenticationService} from '../../core/services/authentication.service';
import {AlertService} from '../../core/services/alert.service';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-pass-recovery',
  templateUrl: './pass-recovery.component.html',
  styleUrls: ['./pass-recovery.component.css']
})
export class PassRecoveryComponent implements OnInit {

  constructor(
    public translate: TranslateService,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
  ) {
  }

  ngOnInit(): void {
  }

  reset(email: string) {
    email = email.trim();
    if (!email) {
      this.alertService.error('alert.enterMail');
      return;
    }

    this.authenticationService.resetPass(email)
      .subscribe(
        data => {
          this.alertService.success('alert.resetSuccessful', true);
        },
        error => {
          this.alertService.error('alert.errorReset');
        });
  }
}
