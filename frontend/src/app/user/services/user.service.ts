import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { User } from '../models/user';

import { AppConfiguration }    from '../../app-configuration';

@Injectable()
export class UserService {

    private usersUrl = 'user';

    constructor(private http: Http, private appConfiguration: AppConfiguration) {
        this.usersUrl = appConfiguration.apiUrl + this.usersUrl;
    }

    getUsers(): Observable<User[]> {
        return this.http.get(this.usersUrl)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    getUser(id: number): Observable<User[]> {
        return this.http.get(this.usersUrl)
            .map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error || 'Server error'));
    }

    addUser(user: User): Observable<User> {
        return this.http.post(this.usersUrl, user)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    editUser(user: User): Observable<User> {
        let userToEdit = {
            id: user.id,
            username: user.username,
            enabled: user.enabled
        }
        return this.http.patch(this.usersUrl, userToEdit)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

    deleteUser(id: number): Observable<any> {
        return this.http.delete(this.usersUrl + '/' + id)
            .map((res:Response) => res.json())
            .catch((error:any) => Observable.throw(error || 'Server error'));
    }

}
