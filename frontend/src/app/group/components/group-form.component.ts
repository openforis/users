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
            name: new FormControl('', [Validators.required]),
            label: new FormControl('', [Validators.required]),
            description: new FormControl(''),
            enabled: new FormControl(''),
            systemDefined: new FormControl(''),
            visibilityCode: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(3)])
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
                    this.groupForm.controls['label'].setValue(this.group.label);
                    this.groupForm.controls['description'].setValue(this.group.description);
                    this.groupForm.controls['enabled'].setValue(this.group.enabled);
                    this.groupForm.controls['systemDefined'].setValue(this.group.systemDefined);
                    this.groupForm.controls['visibilityCode'].setValue(this.group.visibilityCode);
                }, err => {
                    console.log(err);
                });
            }
        }, err => {
            console.log(err);
        });
    }

    onSubmit({value, valid}: {value: Group, valid: boolean}) {
        if (valid) {
            if (this.isNew) {
                this.groupService.addGroup(value).subscribe(data => {
                    this.router.navigate(["/groups"]);
                }, err => {
                    console.log(err);
                });
            } else {
                value['id'] = this.groupId;
                this.groupService.editGroup(value).subscribe(data => {
                    this.router.navigate(["/groups"]);
                }, err => {
                    console.log(err);
                });
            }
        }
    }

    deleteUser() {
        this.groupService.deleteGroup(this.groupId).subscribe(data => {
            this.router.navigate(["/groups"]);
        }, err => {
            console.log(err);
        });
    }

    goBack(): void {
        this.location.back();
    }

}
