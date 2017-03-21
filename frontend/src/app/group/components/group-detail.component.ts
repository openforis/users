import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { BackButtonComponent } from '../../back-button/components/back-button.component';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

import { UserGroup } from '../../user-group/models/user-group';

import { ModalDirective } from 'ng2-bootstrap/modal';

@Component({
    selector: 'group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit {

    @ViewChild('confirmDelete') public confirmDelete: ModalDirective;

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
        this.groupService.getUserGroups(id).then(userGroup => this.userGroups = userGroup, err => {
            console.log(err);
        });
    };

    getGroupById(id: number) {
        this.groupService.getGroup(id).then(group => this.group = group, err => {
            console.log(err);
        });
    };

    deleteGroup() {
        this.groupService.deleteGroup(this.groupId).then(data => {
            this.router.navigate(["/groups"]);
        }, err => {
            console.log(err);
        });
        this.hideConfirmDeleteModal();
    }

    public showConfirmDeleteModal():void {
        this.confirmDelete.show();
    }

    public hideConfirmDeleteModal():void {
        this.confirmDelete.hide();
    }

}
