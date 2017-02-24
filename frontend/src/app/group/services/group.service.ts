import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Group } from '../models/group';
import { UserGroup } from '../../userGroup/models/userGroup';

import { AppConfiguration } from '../../app-configuration';
import { AuthService } from '../../auth/services/auth.service';

@Injectable()
export class GroupService {

    private groupUrl = 'group';
    private headers: Headers;

    constructor(private http: Http, private appConfiguration: AppConfiguration, private authService: AuthService) {
        this.groupUrl = appConfiguration.apiUrl + this.groupUrl;
        let baa = authService.getBAA();
        this.headers = new Headers();
        this.headers.append('Authorization', baa);
    }

    getGroups(enabled?: boolean, systemDefined?: boolean): Promise<Group[]> {
        let url = this.groupUrl + '?';
        if (typeof(enabled) !== 'undefined') url += 'enabled=' + enabled + '&';
        if (typeof(systemDefined) !== 'undefined') url += 'systemDefined=' + systemDefined + '&';
        return this.http.get(url, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    getGroup(id: number): Promise<Group> {
        return this.http.get(this.groupUrl + '/' + id, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    addGroup(group: Group): Promise<Group> {
        return this.http.post(this.groupUrl, group, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    editGroup(group: Group): Promise<Group> {
        let patch = {};
        if (group.name) patch['name'] = group.name;
        if (group.label) patch['label'] = group.label;
        if (group.description) patch['description'] = group.description;
        if (group.systemDefined) patch['systemDefined'] = group.systemDefined;
        if (group.visibilityCode) patch['visibilityCode'] = group.visibilityCode;
        return this.http.patch(this.groupUrl + '/' + group.id, patch, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    deleteGroup(id: number): Promise<any> {
        return this.http.delete(this.groupUrl + '/' + id, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    getUserGroups(id: number): Promise<UserGroup[]> {
        return this.http.get(this.groupUrl + '/' + id + '/users', {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

}
