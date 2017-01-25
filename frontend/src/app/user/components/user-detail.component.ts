import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    selector: 'user-detail',
    templateUrl: './user-detail.component.html'
})
export class UserDetailComponent implements OnInit {

    private userId: number;
    private user: User;

    constructor(private route: ActivatedRoute, private userService: UserService, private location: Location) { }

    ngOnInit(): void {
        this.user = new User();
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                this.userId = +params['id'];
                this.getUserById(this.userId);
            }
        }, err => {
            console.log(err);
        });
    };

    getUserById(id: number) {
        this.userService.getUser(id).subscribe(user => this.user = user, err => {
            console.log(err);
        });
    };

    goBack(): void {
        this.location.back();
    }

}
