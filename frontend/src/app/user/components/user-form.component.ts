import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { BackButtonComponent } from '../../back-button/components/back-button.component';

import { User } from '../models/user';
import { UserService } from '../services/user.service';

import { MessageBarService } from '../../message-bar/services/message-bar.service'

@Component({
    selector: 'user-form',
    templateUrl: './user-form.component.html'
})
export class UserFormComponent implements OnInit {

    private userId: number;
    private user: User;
    private userForm: FormGroup;
    private isNew: boolean;

    constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private messageBarService: MessageBarService) { }

    ngOnInit(): void {
        this.user = new User();
        this.userForm = new FormGroup({
            username: new FormControl('', [Validators.required]),
            rawPassword: new FormControl('', [Validators.required]),
            enabled: new FormControl('')
        });
        this.route.params.subscribe((params: any) => {
            if (!params.hasOwnProperty('id')) {
                this.isNew = true;
            } else {
                this.isNew = false;
                this.userId = +params['id'];
                this.userService.getUser(this.userId).then(user => {
                    this.user = user;
                    this.userForm.controls['rawPassword'].disable();
                    this.userForm.controls['username'].setValue(this.user.username);
                    this.userForm.controls['enabled'].setValue(this.user.enabled);
                }, err => {
                    console.log(err);
                });
            }
        }, err => {
            console.log(err);
        });
    }

    onSubmit({value, valid}: {value: User, valid: boolean}) {
        if (valid) {
            if (this.isNew) {
                this.userService.addUser(value).then(data => {
                    this.router.navigate(["/users"]);
                }, err => {
                    this.messageBarService.add('danger', 'ERROR!');
                    console.log(err);
                });
            } else {
                value['id'] = this.userId;
                this.userService.editUser(value).then(data => {
                    this.router.navigate(["/users"]);
                }, err => {
                    this.messageBarService.add('danger', 'ERROR!');
                    console.log(err);
                });
            }
        }
    }

}
