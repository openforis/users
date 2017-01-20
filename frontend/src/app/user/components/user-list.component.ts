import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    //moduleId: module.id,
    selector: 'user-list',
    templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit {

    private users: User[];
    private selectedUser: User;

    constructor(private userService: UserService) { }

    ngOnInit(): void {
        this.selectedUser = new User();
        this.getUsers();
    };

    getUsers() {
        this.userService.getUsers().subscribe(users => this.users = users, err => {
            console.log(err);
        });
    };

    onSelect(user: User): void {
        this.selectedUser = user;
    }

}