import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { User } from '../models/user';
import { UserGroup } from '../../userGroup/models/userGroup';

import { AppConfiguration } from '../../app-configuration';

@Injectable()
export class UserService {

    private userUrl = 'user';

    constructor(private http: Http, private appConfiguration: AppConfiguration) {
        this.userUrl = appConfiguration.apiUrl + this.userUrl;
    }

    getUsers(): Observable<User[]> {
        return this.http.get(this.userUrl)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    getUser(id: number): Observable<User> {
        return this.http.get(this.userUrl + '/' + id)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    addUser(user: User): Observable<User> {
        return this.http.post(this.userUrl, user)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    editUser(user: User): Observable<User> {
        let patch = {};
        if (user.username) patch['username'] = user.username;
        if (user.enabled) patch['enabled'] = user.enabled;
        return this.http.patch(this.userUrl + '/' + user.id, patch)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    deleteUser(id: number): Observable<any> {
        return this.http.delete(this.userUrl + '/' + id)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    getUserGroups(id: number): Observable<UserGroup[]> {
        return this.http.get(this.userUrl + '/' + id + '/groups')
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

}
