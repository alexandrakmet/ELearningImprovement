import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AuthGuardService as AuthGuard} from "../core/services/auth-guard.service";
import {SettingsComponent} from "./settings/settings.component";
import {GameComponent} from "./game/game.component";


const routes: Routes = [
  {path: 'quiz/:quizId/gameSettings', component: SettingsComponent, canActivate: [AuthGuard]},
  {path: 'game/:gameId/play', component: GameComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PlayQuizRoutingModule {
}
