import { Component } from '@angular/core';

import { AlertModule } from 'ng2-bootstrap/alert';

import { MessageBarService } from '../services/message-bar.service';

@Component({
    selector: 'message-bar',
    templateUrl: './message-bar.component.html'
})
export class MessageBarComponent {

    public alerts: any = [];

    constructor(private messageBarService: MessageBarService) {
        this.alerts = messageBarService.alerts;
    }

}
