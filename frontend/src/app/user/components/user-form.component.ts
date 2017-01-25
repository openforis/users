import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    selector: 'user-form',
    templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

    private userId: number;
    private user: User;
    private isNew: boolean;

    constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private location: Location) { }

    ngOnInit(): void {
        this.user = new User();
        this.route.params.subscribe((params: any) => {
            if (!params.hasOwnProperty('id')) {
                this.isNew = true;
            } else {
                this.isNew = false;
                this.userId = +params['id'];
                this.getUser(this.userId);
            }
        }, err => {
            console.log(err);
        });
    }

    getUser(id: number) {
        this.userService.getUser(id).subscribe(user => this.user = user, err => {
            console.log(err);
        });
    }

    onSubmit(event) {
        event.stopPropagation();
        if (this.isNew) {
            this.userService.addUser(this.user).subscribe(data => {
                this.router.navigate(["/users"]);
            }, err => {
                console.log(err);
            });
        } else {
            this.userService.editUser(this.user).subscribe(data => {
                this.router.navigate(["/users"]);
            }, err => {
                this.router.navigate(["/users"]);
                console.log(err);
            });
        }
    }

    deleteUser() {
        this.userService.deleteUser(this.user.id).subscribe(data => {
            this.router.navigate(["/users"]);
        }, err => {
            console.log(err);
        });
    }

    goBack(): void {
        this.location.back();
    }

}
