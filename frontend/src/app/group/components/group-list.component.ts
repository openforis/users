import { Component, OnInit } from '@angular/core';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

@Component({
    selector: 'group-list',
    templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit {

    private groups: Group[];

    constructor(private groupService: GroupService) { }

    ngOnInit(): void {
        this.getGroups();
    };

    getGroups() {
        this.groupService.getGroups().then(groups => this.groups = groups, err => {
            console.log(err);
        });
    };

}
