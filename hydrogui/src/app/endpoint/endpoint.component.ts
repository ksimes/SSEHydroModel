import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import 'rxjs/add/operator/switchMap';
import {DataOptionsService} from "../services/option.services";
import {Option} from "../model/Option";
import {DataServerService} from "../services/server.services";

@Component({
  selector: 'app-endpoint',
  templateUrl: './endpoint.component.html',
  styleUrls: ['./endpoint.component.css']
})
export class EndpointComponent implements OnInit {

  optionSelected: Option = {id: 'null', description: '', url: ''};

  constructor(private dataOptionService: DataOptionsService,
              private dataServerService: DataServerService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    let optionId: string;

    this.route.paramMap
      .switchMap((params: ParamMap) => {
        optionId = params.get('id');
        return this.dataOptionService.getURL(optionId);
      }).subscribe((data: Option) => this.optionSelected = data);
  }

  public refresh(): void {
    if (this.optionSelected != undefined) {
      this.dataServerService.executeOption(this.optionSelected.url);
    }
  }
}
