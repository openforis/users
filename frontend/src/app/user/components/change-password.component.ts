import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { BackButtonComponent } from '../../back-button/components/back-button.component';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
    selector: 'change-password-form',
    templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent implements OnInit {

    private userId: number;
    private user: User;
    private changePasswordForm: FormGroup;

    constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) { }

    ngOnInit(): void {
        this.user = new User();
        this.changePasswordForm = new FormGroup({
            oldPassword: new FormControl('', [Validators.required]),
            newPassword1: new FormControl('', [Validators.required]),
            newPassword2: new FormControl('', [Validators.required])
        });
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                this.userId = +params['id'];
                this.userService.getUser(this.userId).then(user => {
                    this.user = user;
                }, err => {
                    console.log(err);
                });
            }
        }, err => {
            console.log(err);
        });
    }

    onSubmit({value, valid}: {value: any, valid: boolean}) {
        if (valid) {
            this.userService.changePassword(this.user.username, value.oldPassword, value.newPassword1).then(data => {
                if (!data.success) {
                    console.log(data);
                } else {
                    this.router.navigate(['/users', this.userId]);
                }
            }, err => {
                console.log(err);
            });
        }
    }

}
