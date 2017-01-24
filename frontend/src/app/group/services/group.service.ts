import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { Group } from '../models/group';

import { AppConfiguration }    from '../../app-configuration';

@Injectable()
export class GroupService {

    private groupUrl = 'group';

    constructor(private http: Http, private appConfiguration: AppConfiguration) {
        this.groupUrl = appConfiguration.apiUrl + this.groupUrl;
    }

    getGroup(id: number): Observable<Group> {
        return this.http.get(this.groupUrl + '/' + id)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

}
