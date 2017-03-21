import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { BackButtonComponent } from '../../back-button/components/back-button.component';

import { Group } from '../models/group';
import { GroupService } from '../services/group.service';

import { MessageBarService } from '../../message-bar/services/message-bar.service';

@Component({
    selector: 'group-form',
    templateUrl: './group-form.component.html'
})
export class GroupFormComponent implements OnInit {

    private groupId: number;
    private group: Group;
    private groupForm: FormGroup;
    private isNew: boolean;

    constructor(private route: ActivatedRoute, private router: Router, private groupService: GroupService, private messageBarService: MessageBarService) { }

    ngOnInit(): void {
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
                this.groupService.getGroup(this.groupId).then(group => {
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
                this.groupService.addGroup(value).then(data => {
                    this.messageBarService.add('success', 'Group ' + value.name + ' successfully added!');
                    this.router.navigate(["/groups"]);
                }, err => {
                    this.messageBarService.add('danger', 'ERROR!');
                    console.log(err);
                });
            } else {
                value['id'] = this.groupId;
                this.groupService.editGroup(value).then(data => {
                    this.messageBarService.add('success', 'Group ' + value.name + ' successfully modified!');
                    this.router.navigate(["/groups"]);
                }, err => {
                    this.messageBarService.add('danger', 'ERROR!');
                    console.log(err);
                });
            }
        }
    }

}
