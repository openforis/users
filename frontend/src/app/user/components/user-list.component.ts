import { Component, OnInit } from '@angular/core';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    selector: 'user-list',
    templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit {

    private users: User[];

    constructor(private userService: UserService) { }

    ngOnInit(): void {
        this.getUsers();
    };

    getUsers() {
        this.userService.getUsers().then(users => this.users = users, err => {
            console.log(err);
        });
    };

}
