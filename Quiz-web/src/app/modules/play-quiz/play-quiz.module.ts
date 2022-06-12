import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsComponent} from './settings/settings.component';
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {PlayQuizRoutingModule} from "./play-quiz-routing.module";
import {WaitingRoomComponent} from './game/waiting-room/waiting-room.component';
import {PlayQuestionComponent} from './game/play-question/play-question.component';
import {MatSortModule} from "@angular/material/sort";
import {TimerPipe} from "./game/play-question/timer-pipe";
import {GameComponent} from './game/game.component';
import {ResultsComponent} from './game/results/results.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [
    SettingsComponent,
    WaitingRoomComponent,
    PlayQuestionComponent,
    TimerPipe,
    GameComponent,
    ResultsComponent
  ],
  imports: [
    CommonModule,
    PlayQuizRoutingModule,
    RouterModule,
    FormsModule,
    MatSortModule,
    TranslateModule,
    SharedModule
  ]
})
export class PlayQuizModule {
}
