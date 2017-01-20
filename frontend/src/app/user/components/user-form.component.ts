import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    //moduleId: module.id,
    selector: 'user-form',
    templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

    private id: number;
    private user: User;

    private mode: string;
    private submitted = false;

    constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private location: Location) { }

    ngOnInit(): void {
        this.user = new User();
        //this.user.username = "";
        this.route.params.subscribe(params => {
            if (params['id'] !== undefined) {
                this.mode = 'edit';
                this.id = +params['id'];
                this.getUser(this.id);
            } else {
                this.mode = 'add';
            }
        }, err => {
            console.log(err);
        });
    };

    getUser(id: number) {
        this.userService.getUser(id).subscribe(users => this.user = users.find(user => user.id === id), err => {
            console.log(err);
        });
    };

    onSubmit() {
        this.submitted = true;
        if (this.mode == 'add') {
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