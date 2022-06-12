import {BrowserModule} from '@angular/platform-browser';
import {NgModule, OnInit} from '@angular/core';
import { RouterModule } from '@angular/router';

import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClient} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthenticationModule} from './modules/authentication/authentication.module';
import {CoreModule} from './modules/core/core.module';
import {SharedModule} from './modules/shared/shared.module';
import {AuthRoutingModule} from './modules/authentication/auth-routing.module';
import {PagesRoutingModule} from './modules/core/pages/pages-routing.module';
import {QuizRoutingModule} from './modules/quiz/quiz-routing.module'
import {QuizModule} from './modules/quiz/quiz.module';
import {ProfileModule} from './modules/profile/profile.module';
import {ProfileRoutingModule} from './modules/profile/profile-routing.module';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {BasicAuthHtppInterceptorService} from "./modules/core/services/auth-http-interceptor.service";
import {AuthGuardService} from "./modules/core/services/auth-guard.service";
import {RoleGuardService} from "./modules/core/services/role-guard.service";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {AnnouncementModule} from "./modules/announcement/announcement.module";
import {AnnouncementRoutingModule} from "./modules/announcement/announcement-routing.module";
import {PlayQuizModule} from "./modules/play-quiz/play-quiz.module";
import {PlayQuizRoutingModule} from "./modules/play-quiz/play-quiz-routing.module";

import localeEnGb from '@angular/common/locales/en-GB';
import localeUa from '@angular/common/locales/uk';

/*import {WebsocketModule} from "./modules/websocket/websocket.module";*/

import {ChatModule} from "./modules/chat/chat.module";
import {ChatRoutingModule} from "./modules/chat/chat-routing.module";
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';


// AoT requires an exported function for factories
export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}
import {NotificationMenuComponent} from "./modules/shared/notification-menu/notification-menu.component";
import {registerLocaleData} from "@angular/common";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RouterModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AuthenticationModule,
    CoreModule,
    SharedModule,
    AuthRoutingModule,
    PagesRoutingModule,
    QuizRoutingModule,
    QuizModule,
    ProfileRoutingModule,
    ProfileModule,
    MatFormFieldModule,
    MatDatepickerModule,
    BrowserAnimationsModule,
    ProfileModule,
    QuizModule,
    AnnouncementModule,
    AnnouncementRoutingModule,
    ProfileModule,
    DashboardModule,
    DashboardRoutingModule,
    PlayQuizModule,
    PlayQuizRoutingModule,
    ActivitiesModule,
    ActivitiesRoutingModule,
    AchievementModule,
    AchievementRoutingModule,
    ChatModule,
    ChatRoutingModule,
    AchievementRoutingModule,
    CreateAdminModule,
    CreateAdminRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    {provide:HTTP_INTERCEPTORS, useClass:BasicAuthHtppInterceptorService, multi:true},
    AuthGuardService, RoleGuardService, { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService, NotificationMenuComponent],
  bootstrap: [AppComponent]
})

export class AppModule {
}
