import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { AppConfiguration } from '../../app-configuration';

@Injectable()
export class AuthService {

    private loggedIn: boolean;
    public loggedIn$ = new Subject<boolean>();

    constructor(private http: Http, private appConfiguration: AppConfiguration) {
        this.loggedIn = !!localStorage.getItem('loggedId');
        this.loggedIn$.next(!!localStorage.getItem('loggedId'));
    }

    login(username: string, password: string) {
        let authUrl = this.appConfiguration.apiUrl;
        let headers = new Headers();
        headers.append("Authorization", "Basic " + btoa(username + ":" + password));
        let login = {
            username: username,
            rawPassword: password
        };
        return this.http.post(authUrl + 'login', login, {headers: headers})
            .map((res: Response) => res.json())
            .map((res) => {
                if (res && res.status == 200) {
                    localStorage.setItem('loggedId', 'true');
                    localStorage.setItem('username', username);
                    localStorage.setItem('password', password);
                    this.loggedIn = true;
                    this.loggedIn$.next(true);
                }
            return res;
        });
    }

    logout() {
        localStorage.clear();
        this.loggedIn = false;
        this.loggedIn$.next(false);
    }

    isLoggedIn(): boolean {
        return this.loggedIn;
    }

    getBAA(): string {
        let username = localStorage.getItem('username');
        let password = localStorage.getItem('password');
        return "Basic " + btoa(username + ":" + password);
    }

}
