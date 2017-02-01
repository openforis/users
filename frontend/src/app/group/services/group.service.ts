import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { Group } from '../models/group';
import { UserGroup } from '../../userGroup/models/userGroup';

import { AppConfiguration } from '../../app-configuration';

@Injectable()
export class GroupService {

    private groupUrl = 'group';

    constructor(private http: Http, private appConfiguration: AppConfiguration) {
        this.groupUrl = appConfiguration.apiUrl + this.groupUrl;
    }

    getGroups(): Observable<Group[]> {
        return this.http.get(this.groupUrl)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    getGroup(id: number): Observable<Group> {
        return this.http.get(this.groupUrl + '/' + id)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    addGroup(group: Group): Observable<Group> {
        return this.http.post(this.groupUrl, group)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    editGroup(group: Group): Observable<Group> {
        let patch = {};
        console.log(group);
        if (group.name) patch['name'] = group.name;
        if (group.label) patch['label'] = group.label;
        if (group.description) patch['description'] = group.description;
        if (group.systemDefined) patch['systemDefined'] = group.systemDefined;
        if (group.visibilityCode) patch['visibilityCode'] = group.visibilityCode;
        console.log(patch);
        return this.http.patch(this.groupUrl + '/' + group.id, patch)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    deleteGroup(id: number): Observable<any> {
        return this.http.delete(this.groupUrl + '/' + id)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    getUserGroups(id: number): Observable<UserGroup[]> {
        return this.http.get(this.groupUrl + '/' + id + '/users')
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

}
