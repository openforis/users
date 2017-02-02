import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { BackButtonComponent } from '../../backButton/components/back-button.component';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

import { UserGroup } from '../../userGroup/models/userGroup';

@Component({
    selector: 'group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit {

    private groupId: number;
    private group: Group;
    private userGroups: UserGroup[];

    constructor(private route: ActivatedRoute, private router: Router, private groupService: GroupService) { }

    ngOnInit(): void {
        this.group = new Group();
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                this.groupId = +params['id'];
                this.getGroupById(this.groupId);
                this.getUserGroupById(this.groupId);
            }
        }, err => {
            console.log(err);
        });
    };

    getUserGroupById(id: number) {
        this.groupService.getUserGroups(id).subscribe(userGroup => this.userGroups = userGroup, err => {
            console.log(err);
        });
    };

    getGroupById(id: number) {
        this.groupService.getGroup(id).subscribe(group => this.group = group, err => {
            console.log(err);
        });
    };

    deleteUser() {
        this.groupService.deleteGroup(this.groupId).subscribe(data => {
            this.router.navigate(["/groups"]);
        }, err => {
            console.log(err);
        });
    }

}
