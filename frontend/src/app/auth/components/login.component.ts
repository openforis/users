import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

@Component({
    selector: 'login',
    templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

    private isLoggedIn: boolean;
    private loginCredentials: Object = {
        username: '',
        password: ''
    };

    constructor(private authService: AuthService, private router: Router) { }

    ngOnInit(): void {
        this.isLoggedIn = this.authService.isLoggedIn();
        this.authService.loggedIn$.subscribe((isLoggedIn: boolean) => {
            this.isLoggedIn = isLoggedIn;
        });
    };

    login(username, password) {
        this.authService.login(this.loginCredentials['username'], this.loginCredentials['password']).subscribe((result) => {
            if (result) {
                this.router.navigate(['/login']);
            }
        });
    }

    logout() {
        this.authService.logout();
    }

}
