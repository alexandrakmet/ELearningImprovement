import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {QuizzesComponent} from './quizzes/quizzes.component'
import {UserQuizzesComponent} from './user-quizzes/user-quizzes.component'
import {UpdateQuizComponent} from './update-quiz/update-quiz.component'
import {ViewQuizComponent} from './view-quiz/view-quiz.component'
import {AuthGuardService as AuthGuard} from "../core/services/auth-guard.service";

const quizRoutes: Routes = [
  {path: 'quizzes', component: QuizzesComponent},
  {path: 'userQuizzes', component: UserQuizzesComponent, canActivate: [AuthGuard]},
  {path: 'quiz/:quizId/edit', component: UpdateQuizComponent, canActivate: [AuthGuard]},
  {path: 'quiz/:quizId', component: ViewQuizComponent},
  {path: 'createQuiz', component: UpdateQuizComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [
    RouterModule.forChild(quizRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class QuizRoutingModule {
}
