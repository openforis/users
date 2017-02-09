import { Component, OnInit } from '@angular/core';

import { AuthService } from '../../auth/services/auth.service';

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {

    private isLoggedIn: boolean;

    constructor(private authService: AuthService) { }

    ngOnInit(): void {
        this.isLoggedIn = this.authService.isLoggedIn();
        this.authService.loggedIn$.subscribe((isLoggedIn: boolean) => {
            this.isLoggedIn = isLoggedIn;
        });
    };

}
