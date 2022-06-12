import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AlertComponent} from './alert/alert.component';

import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {SidenavComponent} from './sidenav/sidenav.component';
import {RouterModule} from "@angular/router";
import {NotificationMenuComponent} from './notification-menu/notification-menu.component';
import {MatBadgeModule} from "@angular/material/badge";
import {MatIconModule} from "@angular/material/icon";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {MessageMenuComponent} from './message-menu/message-menu.component';
import {SpinCatComponent} from './spincat/spin-cat.component';


@NgModule({
  declarations: [
    AlertComponent,
    HeaderComponent,
    FooterComponent,
    SidenavComponent,
    NotificationMenuComponent,
    MessageMenuComponent,
    SpinCatComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule,
    MatBadgeModule,
    MatIconModule,
    MatCheckboxModule,
    FormsModule
  ],
  exports: [
    AlertComponent,
    HeaderComponent,
    FooterComponent,
    SidenavComponent,
    TranslateModule,
    SpinCatComponent,
  ]
})
export class SharedModule {
}
