import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MessageBarService } from '../../message-bar/services/message-bar.service'
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

    constructor(private authService: AuthService, private router: Router, private messageBarService: MessageBarService) { }

    ngOnInit(): void {
        this.isLoggedIn = this.authService.isLoggedIn();
        this.authService.loggedIn$.subscribe((isLoggedIn: boolean) => {
            this.isLoggedIn = isLoggedIn;
        });
    };

    login(username, password) {
        this.authService.login(this.loginCredentials['username'], this.loginCredentials['password']).subscribe((res) => {
            if (res && res.status == 200) {
                this.messageBarService.add('success', 'Logged in');
                this.router.navigate(['/login']);
            } else {
                this.messageBarService.add('warning', `${res.status} - ${res.message}`);
            }
        }, err => {
            this.messageBarService.add('danger', 'ERROR!');
            console.log(err);
        });
    }

    logout() {
        this.authService.logout();
    }

}
