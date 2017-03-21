import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, FormArray, FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { UserGroupService } from '../services/user-group.service';
import { UserService } from '../../user/services/user.service';
import { GroupService } from '../../group/services/group.service';

import { UserGroup } from '../models/user-group';
import { User } from '../../user/models/user';
import { Group } from '../../group/models/group';

import { MessageBarService } from '../../message-bar/services/message-bar.service';

@Component({
    selector: 'group-assignment',
    templateUrl: './group-assignment.component.html',
})
export class GroupAssignmentComponent implements OnInit {

    private userGroupForm: FormGroup;
    private user: User;
    private groups: Group[] = [];
    private userGroups: UserGroup[];

    constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private groupService: GroupService, private userGroupService: UserGroupService, private formBuilder: FormBuilder, private messageBarService: MessageBarService) { }

    ngOnInit() {
        this.userGroupForm = this.formBuilder.group({
            groupId: ['', Validators.required],
            statusCode: ['', Validators.required],
            roleCode: ['', Validators.required]
        });
        //
        this.groupService.getGroups(true, false).then(groups => {
            this.groups = groups;
        });
        //
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                let userId = +params['id'];
                this.userService.getUser(userId).then(user => {
                    this.user = user;
                    this.userService.getUserGroups(userId).then(userGroups => {
                        this.userGroups = userGroups;
                        for (let userGroup of this.userGroups) {
                            this.groups = this.groups.filter(function(obj) {
                                return obj.id !== userGroup.group.id;
                            });
                        }
                    });
                }, err => {
                    console.log(err);
                });
            }
        }, err => {
            console.log(err);
        });
    }

    onSubmit({value, valid}: {value: UserGroup, valid: boolean}) {
        if (valid) {
            value['userId'] = this.user.id;
            this.userGroupService.addUserGroup(value).then(data => {
                this.messageBarService.add('success', 'UserGroup successfully added!');
                this.userGroups.push(data);
            }, err => {
                this.messageBarService.add('danger', 'ERROR!');
                console.log(err);
            });
        }
    }

    removeGroupAssignment(userGroupId: number) {
        let userGroup = this.userGroups.filter(item => item.group.id === userGroupId)[0];
        this.userGroupService.deleteUserGroup(userGroup.group.id, userGroup.user.id).then(data => {
            this.groups.push(userGroup.group);
            this.userGroups = this.userGroups.filter(item => item.group.id !== userGroupId);
        }, err => {
            console.log(err);
        });
    }

}
