import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { UserGroup } from '../models/user-group';
import { User } from '../../user/models/user';
import { Group } from '../../group/models/group';

import { AppConfiguration } from '../../app-configuration';
import { AuthService } from '../../auth/services/auth.service';

@Injectable()
export class UserGroupService {

    private headers: Headers;

    constructor(private http: Http, private appConfiguration: AppConfiguration, private authService: AuthService) {
        let baa = authService.getBAA();
        this.headers = new Headers();
        this.headers.append('Authorization', baa);
    }

    addUserGroup(userGroup: UserGroup): Promise<UserGroup> {
        return this.http.post(this.appConfiguration.apiUrl + 'group/' + userGroup.groupId + '/user/' + userGroup.userId, userGroup, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    deleteUserGroup(groupId: number, userId: number): Promise<any> {
        return this.http.delete(this.appConfiguration.apiUrl + 'group/' + groupId + '/user/' + userId, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

}
