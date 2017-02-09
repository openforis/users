import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

import { AuthService } from '../services/auth.service';

@Injectable()
export class LoggedInGuard implements CanActivate {

    private isLoggedIn: boolean;

    constructor(private authService: AuthService) { }

    canActivate(): boolean {
        return this.authService.isLoggedIn();
    }

}
