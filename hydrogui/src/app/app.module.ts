import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import {EndpointComponent} from './endpoint/endpoint.component';
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {DataOptionsService} from "./services/option.services";
import {DataServerService} from "./services/server.services";


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    EndpointComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
  ],
  providers: [DataOptionsService, DataServerService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
