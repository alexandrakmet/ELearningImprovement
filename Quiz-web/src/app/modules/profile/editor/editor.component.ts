import {Component, Injectable, OnInit, Output} from '@angular/core';
import {User} from "../../core/models/user";
import {ProfileService} from "../../core/services/profile.service";
import {AlertService} from "../../core/services/alert.service";
import {SecurityService} from "../../core/services/security.service";
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})

export class EditorComponent implements OnInit {

  id: number;
  userData: User;
  role: string;
  passwordNew:string;
  constructor(
    private profileService: ProfileService,
    private alertService: AlertService,
    private securityService: SecurityService,
    public translate: TranslateService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.getUser();
  }

  private getUser() {
    this.id = this.securityService.getCurrentId();
    this.role = this.securityService.getCurrentRole();
    this.profileService.getUser(this.id).subscribe(data => {
      this.userData = data;
      this.userData.id = this.id;

    });
  }

  edit(id: number, firstName: string, secondName: string, email: string, profile: string) {
    const editedUser: User = {
      id: id,
      firstName: firstName,
      secondName: secondName,
      login: this.userData.login,
      mail: email,
      password: this.userData.password,
      profile: profile,
      score: this.userData.score,
      role: this.userData.role,
      registrationDate: this.userData.registrationDate,
      imageId: this.userData.imageId,
      image: this.userData.image,
      status: this.userData.status,
      language: this.userData.language
    };

    this.profileService.updateUser(editedUser).subscribe(data => {
      this.alertService.success('alert.editSuccessful');
      this.router.navigate(['profile']).then();
    });
  }

  checkPasswords(oldPassword: string, newPassword: string, confirmPassword: string) {
    if (oldPassword == "" || newPassword == "" || confirmPassword == "") {
      this.alertService.error('alert.fillAllRequiredFields');
      return;
    }

    if (newPassword != confirmPassword) {
      this.alertService.error('alert.passwordsDontMatch');
      return;
    }

    this.profileService.checkPasswords(this.userData.login, oldPassword).subscribe(data => {
      console.log("check " + data);
      if (data) {
        this.changePassword(newPassword);
        this.alertService.success('alert.passwordChanged');
      }
      else {
        this.alertService.error('alert.currentPasswordIncorrect');
      }
    });
  }

  changePassword(newPassword: string) {
    this.profileService.changePassword(this.userData.login, newPassword).subscribe(data => {
      this.alertService.success('alert.editSuccessful');
      this.router.navigate(['profile']).then()
    });
  }

}
