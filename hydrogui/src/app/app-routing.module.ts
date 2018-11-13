import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {EndpointComponent} from "./endpoint/endpoint.component";

const routes: Routes = [
  { path: 'options/:id',  component: EndpointComponent },
  { path: '', redirectTo: '/options/', pathMatch: 'full'},
  { path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
