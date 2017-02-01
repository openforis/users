import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { BackButtonComponent } from '../../backButton/components/back-button.component';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

import { UserGroup } from '../../userGroup/models/userGroup';

@Component({
    selector: 'user-detail',
    templateUrl: './user-detail.component.html'
})
export class UserDetailComponent implements OnInit {

    private userId: number;
    private user: User;
    private userGroups: UserGroup[];

    constructor(private route: ActivatedRoute, private userService: UserService) { }

    ngOnInit(): void {
        this.user = new User();
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                this.userId = +params['id'];
                this.getUserById(this.userId);
                this.getUserGroupById(this.userId);
            }
        }, err => {
            console.log(err);
        });
    };

    getUserGroupById(id: number) {
        this.userService.getUserGroups(id).subscribe(userGroup => this.userGroups = userGroup, err => {
            console.log(err);
        });
    };

    getUserById(id: number) {
        this.userService.getUser(id).subscribe(user => this.user = user, err => {
            console.log(err);
        });
    };

}
