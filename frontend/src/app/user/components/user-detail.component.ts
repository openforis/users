import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    //moduleId: module.id,
    selector: 'user-detail',
    templateUrl: './user-detail.component.html'
})
export class UserDetailComponent implements OnInit {

    private id: number;
    private user: User;

    constructor(private route: ActivatedRoute, private userService: UserService, private location: Location) { }

    ngOnInit(): void {
        this.user = new User();
        this.route.params.subscribe(params => {
            this.id = +params['id'];
            this.getUser(this.id);
        }, err => {
            console.log(err);
        });
    };

    getUser(id: number) {
        this.userService.getUser(id).subscribe(users => this.user = users.find(user => user.id === id), err => {
            console.log(err);
        });
    };

    goBack(): void {
        this.location.back();
    }

}