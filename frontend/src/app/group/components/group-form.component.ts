import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

@Component({
    selector: 'group-form',
    templateUrl: './group-form.component.html'
})
export class GroupFormComponent implements OnInit {

    private groupId: number;
    private group: Group;
    private groupForm: FormGroup;
    private isNew: boolean;

    constructor(private route: ActivatedRoute, private router: Router, private groupService: GroupService, private location: Location) { }

    ngOnInit(): void {
        this.group = new Group();
        this.groupForm = new FormGroup({
            name: new FormControl('', [Validators.required, Validators.minLength(2)]),
            label: new FormControl('', [Validators.required, Validators.minLength(2)]),
            description: new FormControl(''),
            enabled: new FormControl(''),
            system_defined: new FormControl(''),
            visibility_code: new FormControl('', [Validators.required, Validators.minLength(2)])
        });
        this.route.params.subscribe((params: any) => {
            if (!params.hasOwnProperty('id')) {
                this.isNew = true;
            } else {
                this.isNew = false;
                this.groupId = +params['id'];
                this.groupService.getGroup(this.groupId).subscribe(group => {
                    this.group = group;
                    this.groupForm.controls['name'].setValue(this.group.name);
                }, err => {
                    console.log(err);
                });
            }
        }, err => {
            console.log(err);
        });
    }

    onSubmit({value, valid}: {value: Group, valid: boolean}) {
        console.log(value, valid);
    }

}
