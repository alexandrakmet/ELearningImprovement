import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {NotFoundComponent} from "./not-found/not-found.component";

const pagesRoutes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: '404', component: NotFoundComponent }
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(pagesRoutes)
  ],
  exports: [
  RouterModule
]
})
export class PagesRoutingModule { }
