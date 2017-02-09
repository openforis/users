import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { User } from '../models/user';
import { UserGroup } from '../../userGroup/models/userGroup';

import { AppConfiguration } from '../../app-configuration';
import { AuthService } from '../../auth/services/auth.service';

@Injectable()
export class UserService {

    private userUrl = 'user';
    private headers: Headers;

    constructor(private http: Http, private appConfiguration: AppConfiguration, private authService: AuthService) {
        this.userUrl = appConfiguration.apiUrl + this.userUrl;
        let baa = authService.getBAA();
        this.headers = new Headers();
        this.headers.append('Authorization', baa);
    }

    getUsers(): Observable<User[]> {
        return this.http.get(this.userUrl, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    getUser(id: number): Observable<User> {
        return this.http.get(this.userUrl + '/' + id, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    addUser(user: User): Observable<User> {
        return this.http.post(this.userUrl, user, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    editUser(user: User): Observable<User> {
        let patch = {};
        if (user.username) patch['username'] = user.username;
        if (user.enabled) patch['enabled'] = user.enabled;
        return this.http.patch(this.userUrl + '/' + user.id, patch, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    deleteUser(id: number): Observable<any> {
        return this.http.delete(this.userUrl + '/' + id, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    getUserGroups(id: number): Observable<UserGroup[]> {
        return this.http.get(this.userUrl + '/' + id + '/groups', {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    changePassword(username: string, oldPassword: string, newPassword: string): Observable<any> {
        let patch = {
            username: username,
            oldPassword: oldPassword,
            newPassword: newPassword
        };
        return this.http.post(this.appConfiguration.apiUrl + 'change-password', patch, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

}
