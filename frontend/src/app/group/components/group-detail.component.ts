import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

@Component({
    selector: 'group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit {

    private groupId: number;
    private group: Group;

    constructor(private route: ActivatedRoute, private groupService: GroupService, private location: Location) { }

    ngOnInit(): void {
        this.group = new Group();
        this.route.params.subscribe((params: any) => {
            if (params.hasOwnProperty('id')) {
                this.groupId = +params['id'];
                this.getGroupById(this.groupId);
            }
        }, err => {
            console.log(err);
        });
    };

    getGroupById(id: number) {
        this.groupService.getGroup(id).subscribe(group => this.group = group, err => {
            console.log(err);
        });
    };

    goBack(): void {
        this.location.back();
    }

}
