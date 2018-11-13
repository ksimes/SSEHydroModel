import {Injectable} from '@angular/core';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class DataServerService {

  public Host:string = window.location.hostname;
  public Port:string = window.location.port; // Will be 4200 in this case.
  public Server:string = "http://" + this.Host + ":" + this.Port;

  constructor(private _http: HttpClient) {
  }

  public executeOption = (url: string): Observable<string> => {
    console.log("url name = [" + this.Server + url + "]");
    return this._http.get<string>(this.Server + url);
  };
}
