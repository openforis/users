import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { User } from '../models/user';
import { UserGroup } from '../../user-group/models/user-group';

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

    getUsers(): Promise<User[]> {
        return this.http.get(this.userUrl, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    getUser(id: number): Promise<User> {
        return this.http.get(this.userUrl + '/' + id, {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    addUser(user: User): Promise<User> {
        return this.http.post(this.userUrl, user, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    editUser(user: User): Promise<User> {
        let patch = {};
        if (user.username) patch['username'] = user.username;
        if (user.enabled) patch['enabled'] = user.enabled;
        return this.http.patch(this.userUrl + '/' + user.id, patch, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    deleteUser(id: number): Promise<any> {
        return this.http.delete(this.userUrl + '/' + id, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    getUserGroups(id: number): Promise<UserGroup[]> {
        return this.http.get(this.userUrl + '/' + id + '/groups', {headers: this.headers})
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

    changePassword(username: string, oldPassword: string, newPassword: string): Promise<any> {
        let patch = {
            username: username,
            oldPassword: oldPassword,
            newPassword: newPassword
        };
        return this.http.post(this.appConfiguration.apiUrl + 'change-password', patch, {headers: this.headers})
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'))
            .toPromise();
    }

}
