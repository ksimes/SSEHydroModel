import {Injectable} from '@angular/core';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Observable} from 'rxjs/Observable';
import "rxjs/add/observable/of";
import {Option} from "../model/Option";

@Injectable()
export class DataOptionsService {

  private allActionUrls = {
    purple : { id: 'purple', url:'/hydromodel/api/purple', description: 'Hydro Purple lights' },
    colour : { id: 'colour', url:'/hydromodel/api/colour', description: 'Hydro Full colour lights' },
    cyan : { id: 'cyan', url:'/hydromodel/api/cyan', description: 'Hydro Cyan lights' },
    greenblue : { id: 'greenblue', url:'/hydromodel/api/greenblue', description: 'Hydro Green Blue lights' },
    off : { id: 'off', url:'/hydromodel/api/off', description: 'Hydro turn off lights' },
    wild : { id: 'wild', url:'/hydromodel/api/wild', description: 'Hydro wild lights' },
    redblue : { id: 'redblue', url:'/hydromodel/api/redblue', description: 'Hydro Red Blue lights' },
    shutdown : { id: 'shutdown', url:'/hydromodel/api/shutdown', description: 'Hydro shutdown' }
  };

  constructor() {
  }

  public getURL = (option: string): Observable<Option> => {
    return Observable.of(this.allActionUrls[option]);
  };
}
